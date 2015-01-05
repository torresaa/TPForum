package forum;

import java.util.*;
import java.util.Map.Entry;
import java.io.*;
import java.math.BigDecimal;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;

import AdministrationForum.ForumAdmin;
import Intervenant.IntervenantImpl;
import Intervenant.Intervenant;
import Intervenant.IntervenantDescriptor;

/**
 * classe représentant l'objet servant de forum
 */


public class ForumImpl implements Forum,Serializable {

	public static String Message;
	public  String Name;
	public ForumImpl() throws RemoteException {
		super();
		this.id +=1;
		
	}
	public ForumImpl( String name) throws RemoteException {
		super();
		this.id +=1;
		this.Name=name;
		
	}
	/**
	 * la structure de mémoristion des intervenants
	 */
	protected HashMap<IntervenantDescriptor,Integer> intervenants = new HashMap();
	/**
	 * la structure de mémoristion des intervenants bannis
	 */
	protected HashMap<String,Integer> intervenants_banni= new HashMap();
	/**
	 * l'identifiant unique d'intervenant
	 */
	protected Integer id = new Integer(0);
	protected Integer id_banni = new Integer(0);
	
	
	/**
	 * méthode permettan le ban d'un client
	 * @ param name 
	 * le nom du client à 
	 * @ return
	 * la structure de mémorisation des clients bannis mise à jour
	 */
	public HashMap getBanned(String name)
	{   			
	if(!this.intervenants_banni.containsKey(name))

	{
		this.intervenants_banni.put(name,this.id_banni);
		
	 this.id_banni=this.id_banni+1;}
	
	return this.intervenants_banni;
	}
	
	public void   auth(String name){
		
		
		
				this.intervenants_banni.remove(name);
				
			
		}
		
		
	/**
	 * Enregistre un intervanant dans la structure de mï¿½moristion des
	 * intervenants. Cette méthode est appliquée par le traitant de communication
	 * du programme client (IntervenantImpl)
	 * 
	 * @param intervenant
	 *            une reference distante vers l'intervenant
	 * @param nom
	 *            nom de l'intervenant
	 * @param prenom
	 *            prenom de l'intervenant
	 * @return un identifiant interne representant l'intervenant dans la
	 *         structure de mémorisation des intervenants
	 */
	public synchronized HashMap enter(Intervenant intervenant, String prenom,
			String nom) throws RemoteException {

		
		IntervenantDescriptor interdes = new IntervenantDescriptor(intervenant,
				prenom, nom);
		String name = nom+"."+prenom;
		
		if(intervenants_banni.containsKey(name)){
			try{
				
			interdes.getIntervenant().listen("vous  êtes banni");
			System.out.println("banni");
			return null;
			}
			catch(Exception e){
				
			}
		
		}
		
		if (this.intervenants.isEmpty()) {
			this.intervenants.put(interdes,this.id );
			this.id += 1;
			interdes.getIntervenant().listen("vous vous êtes connecté au forum");
			 
		} else {
			boolean exist = false;
			Set set = this.intervenants.keySet();
			Iterator i = set.iterator();
			while (i.hasNext()) {
			if (i.next().equals(interdes)) {
					exist = true;
				}
			}

			if (exist == false) {
				Set set1 = this.intervenants.keySet();
				Iterator j = set.iterator();
				
				while (j.hasNext()) {
					IntervenantDescriptor intervenant1 = (IntervenantDescriptor)  j.next();
					intervenant1.getIntervenant().addNewClient(interdes);
				}
				
				this.intervenants.put(interdes,this.id);
				this.id += 1;
				interdes.getIntervenant().listen("vous vous êtes connécté au forum");
				
			}
			
		}
		Registry registry = LocateRegistry.getRegistry();
		ForumAdmin forum_admin;
		
		try {
			forum_admin = (ForumAdmin) registry.lookup("admin_forum");
			forum_admin.updateForum(Name,this.intervenants);  // mise à jour du serveur, nouvelle liste de clients
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		return this.intervenants;
		
		
	}

	/**
	 * De-enregistre un intervanant dans la structure de mémoristion des
	 * intervenants. Cette méthode est appelée par le traitant de communication
	 * du programme client (IntervenantImpl)
	 * 
	 * @param interdes 
	 *           
	 *           
	 */
	public synchronized void leave(IntervenantDescriptor interdes) throws RemoteException {
		
		 IntervenantDescriptor interv_supp = null;
		 Set set = this.intervenants.keySet();
			// Get an iterator
			Iterator i = set.iterator();
			
			while (i.hasNext()) {
				
				IntervenantDescriptor intervenant = (IntervenantDescriptor)  i.next();
				
				if((intervenant.equals(interdes))){
					interv_supp=intervenant;
				}
						
			}
	this.intervenants.remove(interv_supp);
	Set set1 = this.intervenants.keySet();
	
	Iterator j = set.iterator();
	
	while (j.hasNext()) {
		IntervenantDescriptor intervenant1 = (IntervenantDescriptor)  j.next();
		intervenant1.getIntervenant().delNewClient(interv_supp);
	}
	Registry registry = LocateRegistry.getRegistry();
	ForumAdmin forum_admin;
	try {
		forum_admin = (ForumAdmin) registry.lookup("admin_forum");
		forum_admin.updateForum(Name,this.intervenants);   // mise à jour du serveur, nouvelle liste de clients
	} catch (NotBoundException e) {
		
		e.printStackTrace();
	}
		
	}
/**
 * méthode qui renvoie la liste des clients connectés au forum
 */
	public synchronized String who() {
		String s = "";
		int j=1;
		Set set = this.intervenants.keySet();
		Iterator i = set.iterator();
		while (i.hasNext()) {
			IntervenantDescriptor me = (IntervenantDescriptor) i.next();
			s = s + j+ ": "
					+  me.getNom() + " "
					+  me.getPrenom() + " ";
			j=j+1;

		}

		return s;
	}

	
	public void say(String s) throws RemoteException {
		Set set = this.intervenants.keySet();
		Iterator i = set.iterator();
		while (i.hasNext()) {
			
			IntervenantDescriptor intervenant = (IntervenantDescriptor)  i.next();
					

			String name = intervenant.getNom() + "." + intervenant.getPrenom();
			;
			Registry registry = LocateRegistry.getRegistry();
			Intervenant inter;
			try {
				inter = (Intervenant) registry.lookup(name);
				inter.listen(s);
			} catch (NotBoundException e) {
				
				e.printStackTrace();
			}
			
			

		}
	}
	public String getForumNom()
	{
	return this.Name;	
	}
    public String check_forum() throws RemoteException {
	return "actif";
	}
    /**
     * méthode qui met à jour le serveur de forum pour garder un liste cohérente des clients connectés
     */
	public void update(HashMap map) throws RemoteException {
		// TODO Auto-generated method stub
		this.intervenants=map;
	}
}
