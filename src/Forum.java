import java.rmi.*;
import java.util.HashMap;
import java.util.LinkedList;




public interface Forum extends Remote {
	/**
 * Enregistre un intervanant dans la structure de m?moristion des intervenants. Cette m?thode est
 * appel?e par le traitant de communication du programme client (IntervenantImpl) 
 * @param intervenant une reference distante vers l'intervenant
 * @param nom nom de l'intervenant
 * @param prenom prenom de l'intervenant
 * @return un identifiant interne representant l'intervenant 
 * dans la structure de m?moristion des intervenants
 */
  public  HashMap<IntervenantDescriptor, Integer> enter (Intervenant intervenant, String prenom, String nom)throws RemoteException;
  
   /**
 * De-enregistre un intervanant dans la structure de m?moristion des intervenants. Cette m?thode est
 * appel?e par le traitant de communication du programme client (IntervenantImpl) 
 * @param id identification de l'intervenant retourne lors de l'appel ? la methode enter.
 */
  public  void leave(IntervenantDescriptor interdes) throws RemoteException;

  public String who() throws RemoteException;


  public void say(String s)throws RemoteException;
 
  public String check_forum()throws RemoteException;
  public void update(HashMap map) throws RemoteException;

public HashMap getBanned(String client_name)   throws RemoteException;
public void   auth(String name)  throws RemoteException;
}