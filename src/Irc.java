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
* Elle est composï¿½ d'une fonction main qui ï¿½ pour rï¿½le d'instancier 
* l'interface graphique associï¿½e au client (IrcGui) ainsi qu'un objet 
* gï¿½rant les communications avec le forum (IntervenantImpl).
* 
*/

public class Irc {

    public static void main(String args[]) {			
	try{
		System.setProperty("java.security.policy","file:./security.policy");
		IrcGui gui = new IrcGui() ;  
		IntervenantImpl intervenant = new IntervenantImpl("a","b");    
		intervenant.setGUI(gui);      // fixe la référence directe veres le IrcGui       
		gui.setHandler(intervenant);  // fixe la reference directe vers le client
                //gui.start(); 
	} catch (Exception e) {
          System.out.println("ERROR Irc : " + e) ;
	  e.printStackTrace(System.out);
	  }
    }
}

