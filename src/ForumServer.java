// HelloServer.java
// Copyright and License 

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


/**
 * Cette classe est la classe principale constituant le programme serveur de forum. 
 * Cette classe représente le serveur du forum. Elle initialise l'orb 
 * et l'objet servant du forum (ForumImpl).
 */
 

public class ForumServer {

    
    public static void main(String args[]) {

        int status = 0; 

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        
        try {
            ForumImpl forum = new ForumImpl();
            final Registry reg = LocateRegistry.createRegistry(Forum.FORUM_SERVER_PORT);
            LocateRegistry.getRegistry(Forum.FORUM_SERVER_PORT).rebind(Forum.FORUM_SERVER_NAME,forum);
        } catch (Exception ex) {
            ex.printStackTrace();
            status = 1;
        }
        
        if (status == 0){
            System.out.println("Server is ready...");
        }else{
            System.exit(status);
        }

    }

}
