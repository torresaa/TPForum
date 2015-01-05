package Intervenant;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.lang.*;
import java.math.BigDecimal;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import forum.Forum;
import irc.IrcGui;


/**
 * Cette classe définit le traitant de communication du programme client.
 * Elle est utilisée par les classes connectListener,writeListener,whoListener,leaveListener 
 * du GUI pour effectuer les communications distante avec le forum.
*/

public class IntervenantImpl  implements Intervenant, Serializable {
  private static String server_host = "localhost";
  private static IrcGui gui;
  private HashMap client_forum;
  private LinkedList list_client;
  private Registry registry;
  private Intervenant stub;
  
  /**
  * Référence distante vers un forum.
  */
  private static Forum forum=null; //ref vers forum   
  


/**
 * Identification du client dans le forum. Cet identifiant est retournï¿½ 
 * lors de l'appel ï¿½ la mï¿½thode enter sur le forum distant.
 */
  private int id=0;
  
  public int getId() {
	return id;
}



public void setId(int id) {
	this.id = id;
}

/**
 * nom de l'intervenant
 */
  private String nom;
  /**
 * @return the nom
 */
public String getNom() {
	return nom;
}



/**
 * @param nom the name to set
 */
public void setNom(String nom) {
	this.nom = nom;
}



/**
 * @return the prenom
 */
public String getPrenom() {
	return prenom;
}



/**
 * @param prenom the prenom to set
 */
public void setPrenom(String prenom) {
	this.prenom = prenom;
}

/**
 * prenom de l'intervenant
 */
  private String prenom;
private String forumName;
  
  /**
 * constructeur de la classe IntervenantImpl. Le nom et le prenom de l'intervenant sont passés
 * en parametre du programme client (irc.java)
 * @param nom nom de l'intervenant
 * @param prenom prenom de l'intervenant
 */
  public  IntervenantImpl (String nom, String prenom) throws RemoteException {
  	super();
  	 registry = LocateRegistry.getRegistry(server_host);
   stub = (Intervenant) UnicastRemoteObject.exportObject(this, 0);
    
  	registry.rebind(nom+"."+prenom, stub);
  	System.out.println(nom+"."+prenom+ " bound");
  	
  	this.nom = nom;
  	this.prenom = prenom;
  	
  }
  
 
    
  /**
 * Fixe une reference directe vers le gui (IrcGui). Cette reference est utilisé 
 * par le traitant de communication pour imprimer des message de chat dans le gui 
 * via la methode print definie dans IrcGui.
 * @param gui le GUI
 */
  public void setGUI(IrcGui gui){
  	IntervenantImpl.gui = gui;
  }
  





/**
 * Execute la methode enter sur le forum. Cette methode est appelée par le traitant 
 * writeListener défini dans IrcGui. Cette mï¿½thode doit utiliser un serveur de nom 
 * pour obtenir une rï¿½fï¿½rence distante vers le forum et exï¿½cuter la mï¿½thode enter 
 * dessus.
 * @param forum_name nom du forum 
 */
  public void enter (String forum_name) throws Exception {
	  int i=0;
  	
	  
	  try{
			
      forumName=forum_name;
      registry.rebind(nom+"."+prenom, stub);
      IntervenantImpl.forum=  (Forum) registry.lookup(forum_name);
      client_forum=IntervenantImpl.forum.enter(this,this.prenom, this.nom);
      IntervenantImpl.gui.PrintStatus("connecte  a: "+forum_name);
      Thread thread = new Thread(veille_forum);  
      thread.start();                           // lancement du thread qui effectue la veille sur le forum
	  }
	  catch(Exception e){
		  
		  IntervenantImpl.gui.Print("le forum n'existe pas");
		  e.printStackTrace();
	  }
    
  
  }
	
  




/**
 * Execute la methode say sur le forum. Cette methode est appelï¿½ par le traitant 
 * writeListener dï¿½fini dans IrcGui. Cette mï¿½thode doit utilise une rï¿½fï¿½rence distante 
 * vers le forum et exï¿½cuter la mï¿½thode say dessus.
 * @param msg message ï¿½ envoyer aux intervenants enregistrer dans le forum. 
 */
  public void say (String msg) throws Exception {
  	  
	  if (System.getSecurityManager() == null) {
			
	}
	  String name= this.nom+"."+this.prenom;
	  Set set = client_forum.keySet();
		
		Iterator i = set.iterator();
		
		while (i.hasNext()) {
			IntervenantDescriptor intervenant = (IntervenantDescriptor) i.next();
			
			Intervenant inter=  intervenant.getIntervenant();
				inter.listen(name+":"+msg);
			
		}
		
  	
	
  }
  
  /**
 * Cette methode est appelee par le forum pour imprimer un nouveau message de 
 * chat a l'intervenant. Cette impression est dï¿½lï¿½guï¿½e ï¿½ la mï¿½thode print dï¿½finie 
 * dans IrcGui. 
 * @param msg nouveau message ï¿½ imprimer dans le gui.
 */
  public void listen (String msg) throws RemoteException // throws PreconditionException 
  {
	  if (System.getSecurityManager() == null) {
		
		}
 
	 IntervenantImpl.gui.Print(msg);
	  
	  
  } 
  
/**
 * cette méthode ajoute 
 */

  public void  addNewClient(IntervenantDescriptor i) throws RemoteException{
  
	  client_forum.put(i, this.id);
	  id=id+1;
  } 
 
  public void delNewClient(IntervenantDescriptor i) throws RemoteException{
  	  IntervenantDescriptor interv_supp = null;
		 Set set = this.client_forum.keySet();
			// Get an iterator
			Iterator j = set.iterator();
			// Display elements
			while (j.hasNext()) {
				
				IntervenantDescriptor intervenant = (IntervenantDescriptor)  j.next();
				
				if((intervenant.equals(i))){
					interv_supp=intervenant;
				}
						
			}
	this.client_forum.remove(interv_supp);
	IntervenantImpl.gui.Print("le client : "+interv_supp.getNom()+"."+interv_supp.getPrenom()+"\t a quitté le forum ");
  } 

  /**
 * Execute la methode leave sur le forum. Cette methode est appelï¿½ par le traitant 
 * leaveListener dï¿½fini dans IrcGui. Cette mï¿½thode doit utilise une rï¿½fï¿½rence distante 
 * vers le forum et exï¿½cuter la mï¿½thode leave dessus.
 */
  public void leave() throws Exception {
	
	  IntervenantDescriptor interdes = new IntervenantDescriptor(this,
				this.prenom, this.nom);
	 ;
	
	
	  registry.unbind(nom+"."+prenom);
	  IntervenantImpl.forum.leave(interdes);
	  IntervenantImpl.forum= null;
	  IntervenantImpl.gui.PrintStatus("Déconnecté");
	  forumName=null;
	  
  }
  
  /**
 * Execute la methode who sur le forum. Cette methode est appelï¿½ par le traitant 
 * whoListener dï¿½fini dans IrcGui. Cette mï¿½thode doit utilise une rï¿½fï¿½rence distante 
 * vers le forum et exï¿½cuter la methode who dessus.
 */
  public String who() throws Exception {
	return  IntervenantImpl.forum.who(); 
  }
/** 
 * ce Runnable rprésente la veille du client sur le forum, en effet le client vérifie périodiquement
 * si la réference distante du forum auquel il est connecte est  toujours la même  sur le registry, dans le cas d'un changement de réference 
 *  il obtient la nouvelle.
 */
 Runnable  veille_forum= new Runnable(){

	
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			
			 try {
				if(forumName!=null){
				IntervenantImpl.forum=  (Forum) registry.lookup(forumName);}
			} catch (RemoteException | NotBoundException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
	}
	  
  };
}