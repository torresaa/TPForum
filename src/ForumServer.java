import java.util.*;
import java.io.*;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
 



/**
 * Cette classe représente le serveur principal qui tourne le serveur des forums ou  la "fabrique de forums" 
 * Le serveur principal supporte la gestion des pannes à deux niveaux :  niveau des objets forums et niveau du
 * serveur de forums.
 */


public class ForumServer {

	private HashMap forums = new HashMap();
		
	public static void main(String args[]) {

		
		
		String name = "admin_forum";
		
		try {
			
			Registry registry = LocateRegistry.getRegistry(1099);
			ForumAdminImpl forumAdminImpl = new ForumAdminImpl("principal");
			forumAdminImpl.veille_forum(true);               //activation
			forumAdminImpl.setVeille_serveur_bool(true);    //de la gestion de pannes
			Remote stub =(Remote) (ForumAdmin) UnicastRemoteObject.exportObject((Remote) forumAdminImpl, 1098);
			registry.rebind(name, stub);
			System.out.println(name+ " bound");
                        System.out.println( " salut hha");
			
		 }
		    catch (Exception ex) {
			System.err.println("ForumServer exception:");
			ex.printStackTrace();
			
		}

	}

	public HashMap getForums() {
		return forums;
	}


	public void setForums(HashMap forums) {
		this.forums = forums;
	}

}