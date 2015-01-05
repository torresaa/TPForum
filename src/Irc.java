import java.lang.*;
import java.rmi.registry.LocateRegistry;


/**
* Cette classe est la classe principale constituant le programme client.
* Elle est compos� d'une fonction main qui � pour r�le d'instancier 
* l'interface graphique associ�e au client (IrcGui) ainsi qu'un objet 
* g�rant les communications avec le forum (IntervenantImpl).
* 
*/

public class Irc {

    public static void main(String args[]) {
        if (args.length != 2) {
            System.err.println("usage: java Irc <NOM> <prenom>");
            System.exit(-1);
        }
        int status = 0;

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            IrcGui gui = new IrcGui();
            IntervenantImpl intervenant = new IntervenantImpl(args[0], args[1]);
            //final Registry reg = LocateRegistry.createRegistry(Intervenant.PORT);
            LocateRegistry.getRegistry().rebind(Intervenant.CLIENT_NAME, intervenant);
            intervenant.setGUI(gui);
            gui.setHandler(intervenant);
        } catch (Exception e) {
            System.out.println("ERROR : " + e);
            e.printStackTrace(System.out);
            status = 1;
        }
        if (status == 0) {
            System.out.println("Client init ready...");
        }else{
            System.err.println("Initialization problem...");
            System.exit(status);
        }
    }
}

