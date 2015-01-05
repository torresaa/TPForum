package Intervenant;

import java.util.*;
import java.io.*;
import java.rmi.*;
import java.rmi.server.*;
/**
 * classe représentant une decription du client 
 */
public  class IntervenantDescriptor implements Serializable {
	/**
	* le prenom de l'intervenant
	*/
	public String prenom;
	/**
	* le nom de l'intervenant
	*/
	public String nom;
	/**
	 * @return the prenom
	 */
	public String getPrenom() {
		return prenom;
	}

	/**
	 * @param prenom the prenom to set
	 */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* l'objet reprï¿½sentant l'intervenant
	*/
	public Intervenant intervenant;
	/**
	 * @return the intervenant
	 */
	public Intervenant getIntervenant() {
		return intervenant;
	}

	/**
	 * @param intervenant the intervenant to set
	 */
	public void setIntervenant(Intervenant intervenant) {
		this.intervenant = intervenant;
	}

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
 	* test l'ï¿½galitï¿½ entre deux intervenants. deux intervenant sont ï¿½gaux 
 	* si leur nom et prenom sont identiques.
 	*/
	public boolean equals(java.lang.Object anObject){
		if (anObject!=null &&  ((IntervenantDescriptor)anObject).prenom.equals(this.prenom) 
					&& ((IntervenantDescriptor)anObject).nom.equals(this.nom) ) {
			return true;
		}
		return false;
	}
 
}



    