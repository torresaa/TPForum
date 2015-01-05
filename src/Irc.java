import java.lang.*;
import java.rmi.registry.LocateRegistry;
import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;



/**
* Cette classe est la classe principale constituant le programme client.
* Elle est compos� d'une fonction main qui � pour r�le d'instancier 
* l'interface graphique associ�e au client (IrcGui) ainsi qu'un objet 
* g�rant les communications avec le forum (IntervenantImpl).
* 
*/

public class Irc {

    public static void main(String args[]) {			
	try{
		System.setProperty("java.security.policy","file:./security.policy");
		IrcGui gui = new IrcGui() ;  
		IntervenantImpl intervenant = new IntervenantImpl("a","b");    
		intervenant.setGUI(gui);      // fixe la r�f�rence directe veres le IrcGui       
		gui.setHandler(intervenant);  // fixe la reference directe vers le client
                //gui.start(); 
	} catch (Exception e) {
          System.out.println("ERROR Irc : " + e) ;
	  e.printStackTrace(System.out);
	  }
    }
}

