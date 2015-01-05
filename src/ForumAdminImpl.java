
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.Serializable;
import java.lang.*;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;


/**
 * cette classe rep�sente la fabrique d'objets forums
 *
 */
public class ForumAdminImpl implements ForumAdmin,Serializable{
 /**
  * le constructeur de la classe 
  * @param type
  * @throws RemoteException
  * @throws NotBoundException
  */
	
	public ForumAdminImpl(String type) throws RemoteException, NotBoundException {
	     super();
	     this.type=type;
	     registry = LocateRegistry.getRegistry();
	     
	     if(type=="dupliqu�"){
	    	/* on r�cup�re la r�f�rence distante vers le serveur principal */
	    	
	     serveur_principal1= (ForumAdmin) registry.lookup("admin_forum");
	    	
	    }}
	
	
	/**
	 * structure qui m�morise les forums cr��s par la fabrique
	 */
	protected HashMap<String,Forum> nom_forum = new HashMap();
	private Registry registry;
	
	/**
	 * thread qui execute la veille sur le serveur principal
	 */
	
	private Thread thread1;
	
	/**
	 * structure qui m�morise les forums avec leurs listes de clients  � jour.
	 */
	
	private HashMap <String, HashMap> update_forum = new HashMap();
	
	/**
	 * Runnable permettant la veille sur le serveur principal
	 */
	private Runnable veille_serveur;
	
	/**
	 * Runnable permettant la veille sur les diff�rents forums 
	 */
	private Runnable veille_forum;
	
    /**
     * Reference distante vers le serveur duplique
     */
	private ForumAdmin serv_duplique1;
	
	protected HashMap id_forum = new HashMap();
	
	/**
	 * R�ference distante vers le serveur principal
	 */
    private ForumAdmin serveur_principal1;
    
    String forum_name_veille;
    
    public void setServeur_principal1(ForumAdmin serveur_principal1) {
		this.serveur_principal1 = serveur_principal1;
	}
    
    /**
     * indicateur de panne du serveur principal
     */
    
	private boolean panne =false;
	
	/**
	 * prise en charge ou non de la veille sur les forums
	 */
	
	private boolean veille_forum_bool;
	
	/**
	 * prise en charge ou non de la veille sur les serveurs
	 */
	
	private boolean veille_serveur_bool=true;
	/**
	 * type du serveur : principal ou dupliqu�
	 */
	
	private String type;
	
	public void setVeille_serveur_bool(boolean veille_serveur_bool) {
		this.veille_serveur_bool = veille_serveur_bool;
	}

	
	public boolean isPanne() {
		return panne;
	}

	protected static Integer id=1100;
	
		public Integer getIdForumId()
		{
			return ForumAdminImpl.id;
		}
		
	/**
	 * Cette m�thode cr�� un forum, et l'enregistre dans la structure de memorisaton correspondante
	 */
		
       public void Create(String name_forum) throws NotBoundException
       {
    	   Forum f=null;
       
       try {
    	   
		if (!nom_forum.containsKey(name_forum)){
		f = new ForumImpl(name_forum);
		nom_forum.put(name_forum,f);
		id_forum.put(name_forum,ForumAdminImpl.id);
		
		Registry registry = LocateRegistry.getRegistry();     
		
	    Remote stub = UnicastRemoteObject.exportObject(f, ForumAdminImpl.id) ; 
	    registry.rebind(name_forum, stub);
	   if(veille_serveur_bool==true){ 
		 ForumAdmin serv_duplique = (ForumAdmin) registry.lookup("serveur_duplique");
	    serv_duplique.updateForum1(name_forum,f);  // mise � jour de la liste des forums du serveur dupliqu�
	   }
	    if(!update_forum.containsKey(name_forum)){
	    update_forum.put(name_forum, new HashMap()); // enregistrement du forum dans la structure m�morisant les forums avec 
	                                                 // leurs clients respectifs 
	    }
	    
		try {Forum foru;
			foru = (Forum) registry.lookup(name_forum);
			foru.update(update_forum.get(name_forum));      // mise � jour des forums recr��s ( on leur envoie la liste de clients
			                                                // qu'ils poss�daient avant leur panne
		} catch (NotBoundException e) {
			
			e.printStackTrace();
		}
	    
	    System.out.println("Forum  "+name_forum+ "   cr��");
    	ForumAdminImpl.id = ForumAdminImpl.id +1;
	    if(veille_forum_bool==true){
	    	start_veille_forum(name_forum);   // commencer la veille sur le forum
	    
	    }

			}
		else
		{
			System.out.println("ce nom de forum est deja choisi");
		}
       	} catch (RemoteException e) {
		e.printStackTrace();
       	}
    	   
       	}

       /**
        * M�thode permettant de d�truire le forum : elle le supprime de la liste des forums ainsi que du registry
        */
       
       public void Destroy(String nom_forum){
       try {
       {Forum f;
       	
    	   f= (Forum) this.nom_forum.get(nom_forum);
     this.nom_forum.remove(nom_forum);
       	UnicastRemoteObject.unexportObject(f, true);
       	System.out.println("forum d�truit");
       }}
       catch (Exception e)
       {
    	   System.out.println("ERROR Destroy : " + e) ;
    		  e.printStackTrace(System.out);
       }
}

	/**
	 * cette m�thode renvoie la liste des forums cr��s
	 */
       
       
	public String List_forum() {
		String s = "";
		Set cle = this.nom_forum.entrySet();
		Iterator i = cle.iterator();
		while (i.hasNext()) {
			Map.Entry me = (Map.Entry) i.next();
			s = s + " " +me.getKey();
		}
		return s;
	}

	/**
	 * cette m�thode renvoie la liste des clients d'un forum donn�
	 * @param
	 * nom du forum
	 */
	
	public String List_client(String nom_forum) {
		String s="";
		try{
		s=((Forum)this.nom_forum.get(nom_forum)).who();
		}
		catch (Exception e)
	       {
	    	   System.out.println("ERROR list_client : " + e) ;
	    		  e.printStackTrace(System.out);
	       }
		return s;
		}

	/**
	 * cette m�thode interdit � un client de se connecter sur un forum donn�
	 * @param
	 * nom du client
	 * @param
	 * nom du forum
	 */
	
	public void Ban_client(String client_name, String forum_name) {
	{Forum f;
     try {
		f= (Forum) this.nom_forum.get(forum_name);
		f.getBanned(client_name);
		
		}
	 catch (Exception e) {
		e.printStackTrace();
	 	}
	}
	
}
	/**
	 * cette m�thode autoriset  un client d�j� banni, de se connecter sur un forum donn�
	 * @param
	 * nom du client
	 * @param
	 * nom du forum
	 */
	
	public void Auth_client(String client_name, String forum_name) throws RemoteException {
		{Forum f;
			f=(Forum) nom_forum.get(forum_name);
			f.auth(client_name);
		}
	}


	/**
	 * cette m�thode v�rifie l'�tat d'activit� d'un forum donn�
	 */
	
	public String Ping(String forum_name) throws InterruptedException {
		
		String rapport = "deconnecte";
		try {
			Forum forum = (Forum)  registry.lookup(forum_name);
			Thread.sleep(100);
			if (forum.check_forum().equals("actif")){
				rapport= "connecte";
			}
			else rapport= "deconnecte";
		} catch (RemoteException | NotBoundException e) {
			
			e.printStackTrace();
		}
		return rapport;
}
/**
 * cette m�thode arrete la veille sur le serveur principall
 */
	
public void stop_veille(){thread1.stop();}
/**
 * cette m�thode lance la veille sur le serveur principal
 */

public void start_veille(){ 
	veille_serveur= new Runnable(){

	@Override
	public void run() {
		
		while (true){
			
			
			try {
				serveur_principal1.check_serv().equals("actif");
					 
						Thread.currentThread().sleep(1000);
						
					} catch (InterruptedException | RemoteException e) {
						
						e.printStackTrace();
						panne=true;
						return;
					}						
		}

				
					

			 
	
	 
 }};
	thread1 = new Thread(veille_serveur);
thread1.start();}


/**
 * cette m�thode lance la veille sur un forum donn�
 * @param forum_name
 */

public void start_veille_forum(String forum_name){
	forum_name_veille=forum_name;
	Runnable veille_forum= new Runnable(){
Forum forum;
	
	public void run() {
		
		try {
			 forum = (Forum) registry.lookup(forum_name_veille);
		} catch (RemoteException | NotBoundException e1) {
			
			e1.printStackTrace();
		}
		while (true){
			
			
			try {
				if(!forum.check_forum().equals("actif")){
				
				}
						Thread.currentThread();
						Thread.sleep(5000);
					} catch (InterruptedException | RemoteException e) {
					
						try {						
					
							while(nom_forum.containsKey(forum_name_veille)){
								
							}
							Create(forum_name_veille);
						} catch (NotBoundException e1) {
							
							e1.printStackTrace();
						}
						
						Thread.currentThread().stop();
						e.printStackTrace();
		}

				
					

			 
	
	 
 }}};
 Thread thread = new Thread(veille_forum);
// thread_forum.put(forum_name, thread);
 thread.start();
	}



	public String check_serv() {
		
		return "actif";
	}

	/**
	 * cette m�thode permet � un forum d'envoyer sa liste de client � jour au serveur de forums
	 * elle est acc�d�e � distance par le forum
	 * 
	 */
	
	
	public void updateForum(String name,HashMap map) throws RemoteException, NotBoundException {
		
		update_forum.remove(name);
		update_forum.put(name, map);
		if(type=="principal"&&veille_serveur_bool==true){
	    ForumAdmin f= (ForumAdmin)		registry.lookup("serveur_duplique");
	    f.updateForum(name, map);       // mise � jour du serveur dupliqu�
			 
		}
	}
	public void veille_forum(boolean k){
		if(k==true)
			veille_forum_bool= true;
		else
			veille_forum_bool=false;
	}

	/**
	 * cette m�thode permet au serveur principal d'envoyer la liste des forums � jour, au serveur duppliqu�
	 * elle est acc�d�e � distance
	 */
	public void updateForum1(String forum_name, Forum forum) throws RemoteException {
		
		nom_forum.put(forum_name, forum);
	}

/**
 * cette m�thode permet de r� enregistrer les forums tomb�s en panne sur le registry
 * @throws AccessException
 * @throws RemoteException
 * @throws AlreadyBoundException
 * @throws NotBoundException
 */
	public void rebindAll() throws AccessException, RemoteException, AlreadyBoundException, NotBoundException {
		
		HashMap map= new HashMap(nom_forum);
		Iterator iterator= map.keySet().iterator();
		while(iterator.hasNext()){
			String forum =(String) iterator.next();
			Destroy(forum);
			Create(forum);
		}
	}

}