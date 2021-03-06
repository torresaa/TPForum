
import java.rmi.Remote;
import java.util.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Cette classe repr�sente le serveur principal qui tourne le serveur des forums
 * ou la "fabrique de forums" Le serveur principal supporte la gestion des
 * pannes � deux niveaux : niveau des objets forums et niveau du serveur de
 * forums.
 */
public class ForumServer {

    private HashMap forums = new HashMap();

    public static void main(String args[]) {

        String name = "admin_forum";

//        if (System.getSecurityManager() == null) {
//            System.setSecurityManager(new SecurityManager());
//        }

        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            ForumAdminImpl forumAdminImpl = new ForumAdminImpl("principal");
           //final Registry registry = LocateRegistry.createRegistry(1099);
            //LocateRegistry.getRegistry(1099).rebind(name, forumAdminImpl);
            Remote stub =(Remote) (ForumAdmin) UnicastRemoteObject.exportObject((Remote) forumAdminImpl, 1098);
			registry.rebind(name, stub);
            forumAdminImpl.veille_forum(true);              //activation
            forumAdminImpl.setVeille_serveur_bool(true);  //de la gestion de pannes
            System.out.println(name + " bound");
            System.out.println(" salut hha");

        } catch (Exception ex) {
            System.err.println("ForumServer exception:");
            ex.printStackTrace();

        }
        
        while(true){
        }

    }

    public HashMap getForums() {
        return forums;
    }

    public void setForums(HashMap forums) {
        this.forums = forums;
    }

}
