
import java.util.*;
import java.io.*;
import java.rmi.*;
import java.rmi.server.*;
/**
 * classe repr�sentant l'objet servant du forum 
 */
final public class ForumImpl extends UnicastRemoteObject implements Forum {

    public ForumImpl() throws RemoteException {
     super();
    }

    /**
      la structure de m�moristion des intervenants
    */
    protected HashMap intervenants = new HashMap();
    /**
    * l'identifiant unique d'intervenant
    */	
    protected Integer id = new Integer(0);

    
  /**
 * Enregistre un intervanant dans la structure de m�moristion des intervenants. Cette m�thode est
 * appel�e par le traitant de communication du programme client (IntervenantImpl) 
 * @param intervenant une reference distante vers l'intervenant
 * @param nom nom de l'intervenant
 * @param prenom prenom de l'intervenant
 * @return un identifiant interne representant l'intervenant 
 * dans la structure de m�moristion des intervenants
 */
  public synchronized int enter (Intervenant intervenant, String prenom, String nom)throws RemoteException{	
        System.out.println("enter");
        IntervenantDescriptor nouveauMembre= new IntervenantDescriptor(intervenant,nom, prenom);
        this.intervenants.put(this.id, nouveauMembre);
        this.id = this.id + 1;
        return id - 1;
        
  }
  
   /**
 * De-enregistre un intervanant dans la structure de m�moristion des intervenants. Cette m�thode est
 * appel�e par le traitant de communication du programme client (IntervenantImpl) 
 * @param id identification de l'intervenant retourne lors de l'appel � la methode enter.
 */
  public synchronized void leave(int id) throws RemoteException {
        IntervenantDescriptor AncienIntervenant = (IntervenantDescriptor) this.intervenants.remove(id);
        System.out.println("Suppression de l'intervenant:" + AncienIntervenant);

    }

   
    public synchronized void broadcastMessage(String msg, String nom) throws RemoteException {
        Map<Integer , IntervenantImpl> map = this.intervenants;
        for(Integer mapKey : map.keySet()){
             System.out.println("mapSize"+map.size());
             map.get(mapKey).retrieveMessages(msg, nom);
        }
    }
    public void msgForum() throws RemoteException{
        System.out.println("je suis le forum");
    } 
  
  
  
}

