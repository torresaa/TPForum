
import java.lang.*;
import java.rmi.*;
import java.rmi.server.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Cette classe d�fini le traitant de communication du programme client. Elle
 * est utilis�e par les classes
 * connectListener,writeListener,whoListener,leaveListener du GUI pour effectuer
 * les communications distante avec le forum.
 */
public class IntervenantImpl extends UnicastRemoteObject implements Intervenant {

    private static IrcGui gui;

    /**
     * R�f�rence distante vers un forum.
     */
    private static Forum forum = null; //ref vers forum   

    /**
     * Identification du client dans le forum. Cet identifiant est retourn� lors
     * de l'appel � la m�thode enter sur le forum distant.
     */
    private int id;

    /**
     * nom de l'intervenant
     */
    private String nom;
    /**
     * prenom de l'intervenant
     */
    private String prenom;
    
    /**
     * Intervenant descriptor
     * Cet methode a la reference vers this intervenant
     */
    private IntervenantDescriptor descriptor;
    
    /**
     * Ref vers les autres intervenants dans le forum
     */
    private HashMap intervenants; 
    
    /**
     * Counter for the local ids
     */
    private int id_key = 0; 

    /**
     * id de l'intervenant lors de son inscription à un forum constructeur de la
     * classe IntervenantImpl. Le nom et le prenom de l'intervenant sont pass�s
     * en parametre du programme client (irc.java)
     *
     * @param nom nom de l'intervenant
     * @param prenom prenom de l'intervenant
     */
    public IntervenantImpl(String nom, String prenom) throws RemoteException, Exception {
        super();
        this.nom = nom;
        this.prenom = prenom;
        this.intervenants = new HashMap();
        
//        //Ref vers localhost // TODO:Review, maybe the reference need o be created in the forum
//        InetAddress ip = InetAddress.getLocalHost();
//        try {
//            Intervenant local_ref = (Intervenant) Naming.lookup("//" + ip.getHostAddress() + ":"
//                    + PORT + "/" + CLIENT_NAME);
//            //Descriptor prope
//            this.descriptor = new IntervenantDescriptor(local_ref, this.prenom, this.nom); 
//        } catch (Exception e) {
//            System.err.println("Local reference creation exception:");
//            e.printStackTrace();
//        }     
    }

    /**
     * Fixe une reference directe vers le gui (IrcGui). Cette reference est
     * utilis�e par le traitant de communication pour imprimer des message de
     * chat dans le gui via la methode print definie dans IrcGui.
     *
     * @param gui le GUI
     */
    public void setGUI(IrcGui gui) {
        this.gui = gui;
    }

    /**
     * Execute la methode enter sur le forum. Cette methode est appel� par le
     * traitant writeListener d�fini dans IrcGui. Cette m�thode doit utiliser un
     * serveur de nom pour obtenir une r�f�rence distante vers le forum et
     * ex�cuter la m�thode enter dessus.
     *
     * @param forum_name nom du forum
     * @throws java.lang.Exception
     */
    public void enter(String forum_name) throws Exception {
        
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        
        //Ref vers le Forum
        try {
            IntervenantImpl.forum = (Forum) Naming.lookup("//" + FORUM_SERVER_IP
                    + ":" + FORUM_SERVER_PORT + "/" + forum_name);
        } catch (Exception e) {
            System.err.println("Connection to server exception: ");
            throw new Exception("Connection to forum " + forum_name + " denied");
        }
        
        //Intervenants initialization, forum registration
        //TODO: Test
        this.descriptor = new IntervenantDescriptor(this, prenom, nom);
        try{
            this.intervenants = forum.enter(this.descriptor.intervenant, prenom, nom);
        }catch( Exception e){
            e.printStackTrace(System.out);
        }
        
        //Id init
        Iterator it = intervenants.entrySet().iterator();
        id_key = 0;
        while (it.hasNext()){
            Map.Entry intervenantEntry = (Map.Entry)it.next();
            
            int key = (int)intervenantEntry.getKey();
            if (key > id_key){id_key = key;}
            
            IntervenantDescriptor client = 
                    (IntervenantDescriptor) intervenantEntry.getValue();
            if (this.descriptor.equals(client)){
                this.id = (int)intervenantEntry.getKey();
                System.out.println("Intervenant ID: "+ this.id);
            }
            it.remove();
        }
        
        //Intervenants list refresh, tous les autres sauf moi
        this.intervenants.remove(this.id);
    }

    /**
     * Execute la methode say sur le forum. Cette methode est appel� par le
     * traitant writeListener d�fini dans IrcGui. Cette m�thode doit utilise une
     * r�f�rence distante vers le forum et ex�cuter la m�thode say dessus.
     *
     * @param msg message � envoyer aux intervenants enregistrer dans le forum.
     */
    public void say(String msg) throws Exception {
        if (this.id < 0){
            throw new Exception("You are not registred in a forum");
        }else{
            System.out.println("Writing new message...");
            try{
                forum.say(msg);
            }catch(Exception e){
                System.out.println("No forum connection");
                throw new Exception("No forum connection");
                //System.out.println("Sending message direct to users");
                //TODO: Send message to every user.
            } 
        }
    }

    /**
     * Cette methode est appel� par le forum pour imprimer un nouveau message de
     * chat a l'intervenant. Cette impression est d�l�gu�e � la m�thode print
     * d�finie dans IrcGui.
     *
     * @param msg nouveau message � imprimer dans le gui.
     */
    public void listen(String msg) throws RemoteException {
        System.out.println("Listen new msg...");
        if (this.id < 0) {
            System.out.println("...rejected message, no active forum");
        } else {
            IntervenantImpl.gui.Print(msg);
        }
    }

    public void addNewClient(IntervenantDescriptor i) throws RemoteException {
        this.intervenants.put(id_key++, i);
        System.out.println("Adding new client with id: "+ id_key);
    }

    public void delNewClient(IntervenantDescriptor i) throws RemoteException {
        Iterator it = intervenants.entrySet().iterator();
        int del_id = 0;
        while (it.hasNext()){
            Map.Entry intervenantEntry = (Map.Entry)it.next();
            IntervenantDescriptor client = 
                    (IntervenantDescriptor) intervenantEntry.getValue();
            if (i.equals(client)){
                del_id = (int)intervenantEntry.getKey();
                break;
            }
            it.remove();
        }
        Object remove = intervenants.remove(del_id);
        System.out.println("Delete client with id: "+ id_key + " " + i.toString());
    }

    /**
     * Execute la methode leave sur le forum. Cette methode est appel� par le
     * traitant leaveListener d�fini dans IrcGui. Cette m�thode doit utilise une
     * r�f�rence distante vers le forum et ex�cuter la m�thode leave dessus.
     */
    public void leave() throws Exception {
        try{
            this.forum.leave(this.id);
        }catch (Exception e){
            System.err.println("No conecction to Forum.");
            this.gui.Print("No connection to forum.");
            //TODO:Delete command to every client
            //TODO:Maybe return Exception
        }
        this.id = -1;  // Sera bien de dire -1 car dans les forums on a ids toujour positif
        this.intervenants.clear(); // No reference to other ones in the forum 
        System.out.println("Exit of forum..");
    }

    /**
     * Execute la methode who sur le forum. Cette methode est appel� par le
     * traitant whoListener d�fini dans IrcGui. Cette m�thode doit utilise une
     * r�f�rence distante vers le forum et ex�cuter la methode who dessus.
     */
    public String who() throws Exception {
        try{
            return IntervenantImpl.forum.who();
        }catch(Exception e){
            System.err.println("No conecction to Forum.");
            throw new Exception("No connection to Forum");            
        }
    }
}
