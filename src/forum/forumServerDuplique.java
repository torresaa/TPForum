package forum;

import java.util.*;
import java.io.*;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;

import admin.Forum_Gui;
import AdministrationForum.ForumAdmin;
import AdministrationForum.ForumAdminImpl;
import Intervenant.IntervenantImpl;


/**
 * Cette classe représente le serveur de forums dupliqué,
 * lorsque le serveur principal tombe en panne, celui là prend le relais
 * tout en ayant une copie des forums et de leurs clients.
 */


public class forumServerDuplique {

	private HashMap forums = new HashMap();
	
	
	public static void main(String args[]) {

				
		String name = "admin_forum";
		
		try {
				
			ForumAdminImpl forumAdminImpl = new ForumAdminImpl("dupliqué");  // enregistrement au registry sous le nom dupliqué
			forumAdminImpl.setVeille_serveur_bool(false);
			Registry registry = LocateRegistry.getRegistry();
			Remote stub =(Remote) (ForumAdmin) UnicastRemoteObject.exportObject((Remote) forumAdminImpl, 3000);
			registry.bind("serveur_duplique", stub);
			forumAdminImpl.start_veille();
		while(!forumAdminImpl.isPanne()){
			System.out.println("pas de panne");
			Thread.currentThread().sleep(1000);
		}
		// aprés detetction de la panne
		//enregistrement du serveur sous le nom admin_forum
		//il remplace donc le serveur principal
		registry= LocateRegistry.getRegistry(1099);
		registry.rebind("admin_forum", stub);
		System.out.println("bound");
		forumAdminImpl.stop_veille();
	    forumAdminImpl.rebindAll();        
			
			
			
			
			
		
			
		} catch (Exception ex) {
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
