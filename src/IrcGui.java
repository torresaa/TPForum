import java.awt.*;
import java.awt.event.*;
import java.lang.*;
 

/**
 * Cette classe d�fini l'interface graphique du programme client.
 * L'interface est mise en place par le constructeur IrcGui. 
*/

public class IrcGui {
	
    /**
     * utilis� pour imprimer les messages de chat
     */
    public TextArea		text; 
    
    /**
     * utilis� pour saisir les messages de chat
     */
    public TextField	        data,dataconnect; 
    
    /**
     * Frame de la fenetre principale
     */
    public Frame 		frame;

    /**
     * ref directe vers le handler de communication (IntervenantImpl)
     */
    static IntervenantImpl intervenant; 
    
    /**
     * Indique que le client n'est pas connect� � un forum. Cette constante est utilis� 
     * pour g�rer le comportement des boutons de l'interface graphique.
     */
    private static final int NOTCONNECTED =0;
    
    /**
     * Indique que le client est connect� � un forum. Cette constante est utilis� 
     * pour g�rer le comportement des boutons de l'interface graphique.
     */
    private static final int CONNECTED =1;
    
    /**
     * Variable repr�sentant l'�tat de connexion d'un client. 
     */
    private int statut = NOTCONNECTED;
    
    /**
     * Constructeur de l'interface graphique Ircgui. Ce constructeur instancie 
     * les divers objets graphiques et associe au divers bouttons les classes 
     * de traitement correspondantes.
     * L'interface comporte 4 boutons :
     *		- connect : connexion � un forum (traitant associ� : connectListener)
     *		- write	  : Emission d'un message � un forum (traitant associ� : connectListener)
     *		- who	  : liste des intervnant connect� au forum (traitant associ� : WhoListener)
     *		- leave   : quitte un forum de discussion ((traitant associ� : LeaveListener))
     */
    public IrcGui() {
	
	// initGui
	frame=new Frame();
	frame.setLayout(new FlowLayout());
	
	text=new TextArea(10,60);
	text.setEditable(false);
	text.setForeground(Color.red);
	frame.add(text);
	
	data=new TextField(60);
	frame.add(data);
	
	Button write_button = new Button("write");
	write_button.addActionListener(new writeListener(this));
	frame.add(write_button);
	
	
	Button connect_button = new Button("connect");
	connect_button.addActionListener(new connectListener(this));
	frame.add(connect_button);
	
	Button who_button = new Button("who");
	who_button.addActionListener(new whoListener(this));
	frame.add(who_button);
	
	Button leave_button = new Button("leave");
	leave_button.addActionListener(new leaveListener(this));
	frame.add(leave_button);
	
	frame.setSize(470,300);
	text.setBackground(Color.black); 
	frame.show();	
    }
    
     /**
     * Fixe le traitant de communication (IntervenantImpl) utiliser par
     * le GUI pour communiquer avec le forum
     * @param intervenant une r�f�rence directe sur le traitant de communication
     */
    public void setHandler(IntervenantImpl intervenant){
    	this.intervenant = intervenant;
    }
    
    /**
     * Affiche un message de chat dans le GUI. 
     * Cette methode est utilis� par le traitant de communication lors de la
     * reception d'un message de chat en provenance du forum.Un message de chat
     * � le format suivant : "nom.prenom >> txt....".
     * @param msg le message de chat � afficher.  
     */
     
    public void Print(String msg) {
    	try {
    		this.text.append(msg+"\n");
    	} catch (Exception ex) {
			ex.printStackTrace();
			return;
	}	
    }

    /**
     * Classe traitant les action sur le bouton connect du GUI. 
     * 
     */
    class connectListener implements ActionListener {
	IrcGui irc;
	
    /**
     * Constructeur de ConnectListener
     * @param IrcGui une r�f�rence directe vers l'objet g�rant le GUI (IrcGui).  
     */
	public connectListener (IrcGui i) {
        	irc = i;
	}
    
     /**
     * Traite les clicks sur bouton connect. 
     * Execute la methode enter(java.lang.String forum_name) sur le traitant 
     * de communication (IntervenantImpl)
     * @param e l'evenement associ�
     */
	public void actionPerformed (ActionEvent e) {
            try {
                irc.intervenant.enter(irc.data.getText());
            } catch (Exception meth_e) {
                System.err.println("Enter Event Exception:");
                meth_e.printStackTrace(System.out);
                irc.Print(meth_e.getMessage());
            }
            irc.Print("Connection ready to forum '"+irc.data.getText()+"' ");
            irc.data.setText("");
	}
    }  
    
    /**
     * Classe traitant les action sur le bouton write du GUI. 
     * 
     */
    class writeListener implements ActionListener  {
	IrcGui irc;
	
     /**
     * Constructeur de WriteListener
     * @param IrcGui une r�f�rence directe vers l'objet g�rant le GUI (IrcGui).  
     */
	public writeListener (IrcGui i) {
        	irc = i;
	}
	
     /**
     * Traite les clicks sur bouton connect.
     * Execute la methode say(java.lang.String msg) ) sur le traitant 
     * de communication (IntervenantImpl)
     * @param e l'evenement associ�
     */
	public void actionPerformed (ActionEvent e) {
		// TO DO !!!
		  // emission d'une commande say au forum via le traitant de communication
		  // le msg est dans irc.data.getText()
	}
    }  
    
    /**
     * Classe traitant les action sur le bouton who du GUI. 
     * 
     */
    class whoListener implements ActionListener {
	IrcGui irc;
	
     /**
     * Constructeur de whoListener
     * @param IrcGui une r�f�rence directe vers l'objet g�rant le GUI (IrcGui).  
     */
	public whoListener (IrcGui i) {
        	irc = i;
	}
	
     /**
     * Traite les clicks sur bouton who.
     * Execute la methode String who() sur le traitant 
     * de communication (IntervenantImpl)
     * @param e l'evenement associ�
     */
	public void actionPerformed (ActionEvent e) {
		
		  // TO DO !!!
		  // emission d'une commande who au forum via le traitant de communication	
	}
    }

     /**
     * Classe traitant les action sur le bouton leave du GUI. 
     * 
     */
    class leaveListener implements ActionListener {
	IrcGui irc;
	
     /**
     * Constructeur de leaveListener
     * @param IrcGui une r�f�rence directe vers l'objet g�rant le GUI (IrcGui).  
     */
	public leaveListener (IrcGui i) {
        	irc = i;
	}
	
     /**
     * Traite les clicks sur bouton leave.
     * Execute la methode void leave()  sur le traitant 
     * de communication (IntervenantImpl)
     * @param e l'evenement associ�
     */
	public void actionPerformed (ActionEvent e) {
		// TO DO !!!
		// emission d'une commande leave au forum via le traitant de communication	

	}
    }
    
}


