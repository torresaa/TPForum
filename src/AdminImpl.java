
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.lang.*;
import java.math.BigDecimal;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * cette classe repr�sente l'administrateur des forums
 */
public class AdminImpl implements Intervenant,Serializable{

	private static Forum_Gui gui;
	private static ForumAdmin forum_admin=null;
	private String nom;
	private String prenom;
	private Registry registry;
	
	public  AdminImpl (String nom, String prenom) throws RemoteException {
	  	super();
	  	this.nom = nom;
	  	this.prenom = prenom;
	  	registry=LocateRegistry.getRegistry();
	  }
	
	public String getPrenom() {
		return prenom;
	}
	public String getNom() {
		return nom;
	}
	public void setGUI(Forum_Gui gui){
	  	AdminImpl.gui = gui;
	}
	public void setForumAdmin(ForumAdmin f)
	{
		AdminImpl.forum_admin = f;
	}
	
	/**
	 * m�thode de connexion de l'admin � la fabrique de forums
	 * @throws Exception
	 */
	
	public void connect() throws Exception{
		if(forum_admin==null){
		 Registry registry = LocateRegistry.getRegistry();
		 AdminImpl.forum_admin=  (ForumAdmin) registry.lookup("admin_forum");
		 String name = this.getNom()+"."+this.getPrenom();
		 Intervenant stub = (Intervenant) UnicastRemoteObject.exportObject(this, 0);
		 registry.rebind(name, stub);
		 AdminImpl.gui.Print("Bonjour Admin !");
		Thread thread = new Thread(veille_forum);
		thread.start();
		}
	}
	
	/**
	 * cette m�thode cr�� un nouveau forum
	 * @param forum_name
	 * @throws NotBoundException
	 */
	
	public void create(String forum_name) throws NotBoundException{
		
		try {
			AdminImpl.forum_admin.Create(forum_name);
			 AdminImpl.gui.Print("Le forum : "+forum_name+" a �t� cr��");
		} catch (RemoteException e) {
			
			e.printStackTrace();
		}}
	
	/**
	 * cette m�thode d�truit un forum
	 * @param forum_name
	 */
	
	public void destroy(String forum_name){
		
		try {
			AdminImpl.forum_admin.Destroy(forum_name);
		} catch (RemoteException e) {
			
			e.printStackTrace();
		}
		
	}
	 /**
	  * cette methode renvoie la liste des forums
	  * @return
	  */
	
	public String list_forum(){
		String s = null;
		try {
			s=AdminImpl.forum_admin.List_forum();
		} catch (RemoteException e) {
			
			e.printStackTrace();
		}
		return s;
	}
	
	/**
	 * cette m�thode revoie la liste de clients d'un forum donn�
	 * @param list
	 * @return
	 */
	
	public String list_client(String list){
		String s = null;
		try {
			s=AdminImpl.forum_admin.List_client(list);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
		}
	
		/**
		 * cette m�thode interdit � un client de se connecter � un forum donn�
		 * @param client_name
		 * @param forum_name
		 */
	
	public void ban_client(String client_name,String forum_name){
		
		try {
			AdminImpl.forum_admin.Ban_client(client_name,forum_name);
		} catch (RemoteException e) {
			
			e.printStackTrace();
		}
		
	}
	/**
	 * autoriser un client d�j� banni
	 * @param client_name
	 * @param forum_name
	 */
	
public void auth_client(String client_name,String forum_name){
		
		try {
			AdminImpl.forum_admin.Auth_client(client_name,forum_name);
		} catch (RemoteException e) {
			
			e.printStackTrace();
		}
		
	}

	/**
	 * verifier l'�tat de connexion d'un forum
	 * @param forum_name
	 * @return
	 * @throws InterruptedException
	 */

public String ping(String forum_name) throws InterruptedException{
	String s=null;
	try {
		s=AdminImpl.forum_admin.Ping(forum_name);
	} catch (RemoteException e) {
		
		e.printStackTrace();
	}
	return s;
}
	
	
	
	
	
	public void listen(String msg) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addNewClient(IntervenantDescriptor i) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delNewClient(IntervenantDescriptor i) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	/**
	 * ce Runnable repr�sente la veille de l'admin sur la fabrique d'objets forums
	 * il v�rifie p�riodiquement si la r�f�rence distante de la fabrique a chang�, si c'est le cas
	 * il r�cup�re la nouvelle
	 */
	
	Runnable  veille_forum= new Runnable(){

		@Override
		public void run() {
			
			while(true){
				
				 try {
					AdminImpl.forum_admin=  (ForumAdmin) registry.lookup("admin_forum");
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
