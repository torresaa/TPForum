/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author omar
 */
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
/**
 * cette classe permet de lancer le RMI registry dans un processus ind�pendant, 
 * comme �a on est s�r que le registry reste activ�
 */
public class Registry_creation {

	public static void main(String args[]) throws RemoteException {
		Registry registry = LocateRegistry.createRegistry(1099);
		while(true){
			
		}
	}
	
}