
import java.io.*;
/**
 * classe repr�sentant l'objet servant du forum 
 */
public  class IntervenantDescriptor implements Serializable{
	/**
	* le prenom de l'intervenant
	*/
	public String prenom;
	/**
	* le nom de l'intervenant
	*/
	public String nom;
	/**
	* l'objet repr�sentant l'intervenant
	*/
	public Intervenant intervenant;
	/**
	* constructeur d'un descripteur d'intervenant
	* <dt><b> Requires: </b><code>
	* <dd>  argsValides : intervenant!=null && prenom!=null && nom!=null
	* </code>
	*/
	public IntervenantDescriptor(Intervenant intervenant, String prenom, String nom)  {
		/*if(intervenant==null || prenom==null || nom==null) {
			throw new PreconditionException(this.getClass(),"IntervenantDescriptor");
		}*/
		this.intervenant=intervenant;
		this.prenom=prenom;
		this.nom=nom;
	}
	
	/**
 	* test l'�galit� entre deux intervenants. deux intervenant sont �gaux 
 	* si leur nom et prenom sont identiques.
 	*/
	public boolean equals(java.lang.Object anObject){
		if (anObject!=null &&  ((IntervenantDescriptor)anObject).prenom.equals(this.prenom) 
					&& ((IntervenantDescriptor)anObject).nom.equals(this.nom) ) {
			return true;
		}
		return false;
	}
        
        @Override
        public String toString(){
            return this.nom + " " + this.prenom; 
        }
 
}



    