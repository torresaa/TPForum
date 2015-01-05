/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irc;

/**
 *
 * @author omar
 */

import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import admin.Forum_Gui;
import forum.Forum;
import forum.ForumImpl;
import Intervenant.Intervenant;
import Intervenant.IntervenantImpl;


/**
* Cette classe est la classe principale constituant le programme client.
* Elle est composé d'une fonction main qui a pour rôle d'instancier 
* l'interface graphique associé au client (IrcGui) ainsi qu'un objet 
* gérant les communications avec le forum (IntervenantImpl).
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

