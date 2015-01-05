
import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;







import javax.swing.text.PasswordView;



  

/**
 * Cette classe dï¿?fini l'interface graphique de l'admin.
 * L'interface est mise en place par le constructeur Forum_Gui. 
*/

public class Forum_Gui {
	
	private static AdminImpl admin; 
    /**
     * utilisï¿? pour imprimer les messages de chat
     */
    public TextArea		text; 
    
    /**
     * utilisï¿? pour saisir les messages de chat
     */
    public  TextField data,forum_name1,forum_name2,client_name1,client_name2;
    public Label lab1,lab2,lab3,lab4;

	public TextField dataconnect; 
    
    /**
     * Frame de la fenetre principale
     */
    public Frame 		frame,frame_ban_client,frame_auth_client;

    /**
     * ref directe vers le handler de communication (IntervenantImpl)
     */
   
   
    
    @SuppressWarnings("deprecation")
	public Forum_Gui() {
	
	frame=new Frame();
	frame.setLayout(new FlowLayout());
	frame_ban_client=new Frame();
	frame_ban_client.setVisible(false);
	frame_ban_client.setLayout(new FlowLayout());
	frame_auth_client=new Frame();
	frame_auth_client.setVisible(false);
	frame_auth_client.setLayout(new FlowLayout());
	
	
	text=new TextArea(10,60);
	text.setEditable(false);
	text.setForeground(Color.red);
	frame.add(text);
	
	data=new TextField(60);
	frame.add(data);
	forum_name1=new TextField(10);
	forum_name2=new TextField(10);
	lab1=new Label("forum_name");
	lab2=new Label("client_name");
	lab3=new Label("forum_name");
	lab4=new Label("client_name");
	frame_ban_client.add(lab1);
	frame_ban_client.add(forum_name1);
	frame_ban_client.add(lab2);
	client_name1=new TextField(10);
	client_name2=new TextField(10);
	frame_ban_client.add(client_name1);
	frame_auth_client.add(lab3);
	frame_auth_client.add(forum_name2);
	frame_auth_client.add(lab4);
	frame_auth_client.add(client_name2);
	
	
	Button submit_ban = new Button("submit");
	submit_ban.addActionListener(new SubBanListener(this));
	frame_ban_client.add(submit_ban);
	
	Button submit_auth = new Button("submit");
	submit_auth.addActionListener(new SubAuthListener(this));
	frame_auth_client.add(submit_auth);
	
	
	Button write_button = new Button("Create");
	write_button.addActionListener(new CreateListener(this));
	frame.add(write_button);
	
	Button destroy_button = new Button("Destroy");
	destroy_button.addActionListener(new DestroyListener(this));
	frame.add(destroy_button);
	
	Button pingForum_button = new Button("Ping");	
	pingForum_button.addActionListener(new PingForumListener(this));	
	frame.add(pingForum_button);
	
	Button listForum_button = new Button("List of Forums");
	listForum_button.addActionListener(new ListForumListener(this));
	frame.add(listForum_button);
	
	Button listClient_button = new Button("List of Clients");
	listClient_button.addActionListener(new ListClientListener(this));
	frame.add(listClient_button);
	
	Button banClient_button = new Button("Ban Client");
	banClient_button.addActionListener(new BanClientListener(this));
	frame.add(banClient_button);
	
	Button authClient_button = new Button("Authenticate Client");
	authClient_button.addActionListener(new AuthenClientListener(this));
	frame.add(authClient_button);
	
	Button connect_button = new Button("connect");
	connect_button.addActionListener(new ConnectListenner(this));
	frame.add(connect_button);

	
	frame.setSize(470,300);
	frame_ban_client.setSize(470,300);
	frame_auth_client.setSize(470,300);
	text.setBackground(Color.black); 
	frame.show();	
    }
    
     /**
     * Fixe le traitant de communication (AdminImpl) utilisé par
     * le GUI pour communiquer avec la fabrique d'objets forums
     * 
     */
    public void setHandler(AdminImpl admin){
    	Forum_Gui.admin = admin;
    }
    
  /**
   * affiche un message sur l'interface graphique
   * @param msg
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
     * Classe traitant les action sur le bouton destroy du GUI. 
     * 
     */
    class DestroyListener implements ActionListener {
	Forum_Gui gui;
	
    /**
     * Constructeur de DestroyListener
     * @param Forum_Gui une rï¿?fï¿?rence directe vers l'objet gï¿?rant le GUI (Forum_Gui).  
     */
	public DestroyListener (Forum_Gui i) {
        	gui = i;
	}
    
     
	public void actionPerformed (ActionEvent e) {
		// TO DO !!! :Destroy Forum
	
		
		admin.destroy(gui.data.getText());
		
	}
    }  
    
    /**
     * Classe traitant les action sur le bouton create du GUI. 
     * 
     */
    class CreateListener implements ActionListener  {
	Forum_Gui gui;
	
    
	public CreateListener (Forum_Gui i) {
        	gui = i;
	}
	
    
	public void actionPerformed (ActionEvent e) {
		
		
		String name_forum= gui.data.getText();
		
			try {
				admin.create(name_forum);
			} catch (NotBoundException e1) {
			
				e1.printStackTrace();
			}
	}
    }  
    
    /**
     * Classe traitant les action sur le bouton list_forums du GUI. 
     * 
     */
    class ListForumListener implements ActionListener {
	Forum_Gui gui;

	public ListForumListener (Forum_Gui i) {
        	gui = i;
	}

	public void actionPerformed (ActionEvent e) {
		
		gui.Print(admin.list_forum());
	}
    } 
    
    /**
     * Classe traitant les action sur le bouton list_clients du GUI. 
     * 
     */
    class ListClientListener implements ActionListener {
	Forum_Gui gui;
	public ListClientListener (Forum_Gui i) {
        	gui = i;
	}
	public void actionPerformed (ActionEvent e) {
	
      gui.Print(admin.list_client(gui.data.getText()));
		
	}
    }  
    
    /**
     * Classe traitant les action sur le bouton ping du GUI. 
     * 
     */
    class PingForumListener implements ActionListener {
	Forum_Gui gui;
	
   
	public PingForumListener (Forum_Gui i) {
        	gui = i;
	}
    
     
	public void actionPerformed (ActionEvent e) {
		
		String name_forum= gui.data.getText();
		try {
			gui.Print(admin.ping(name_forum));
		} catch (InterruptedException e1) {
			
			e1.printStackTrace();
		}
	}
    }  
    /**
     * Classe traitant les action sur le bouton banclient du GUI. 
     * 
     */
    class BanClientListener implements ActionListener {
	Forum_Gui gui;
	
	public BanClientListener (Forum_Gui i) {
        	gui = i;
	}
    
     
	public void actionPerformed (ActionEvent e) {
		
		
		
		frame_ban_client.setVisible(true);   // affiche la fenetre traitant le ban du client
		
		
	}
    }  
    /**
     * Classe traitant les action sur le bouton auth_client du GUI. 
     * 
     */
    class AuthenClientListener implements ActionListener {
	Forum_Gui gui;
	
    
	public AuthenClientListener (Forum_Gui i) {
        	gui = i;
	}
    
     
	public void actionPerformed (ActionEvent e) {
		frame_auth_client.setVisible(true);        // affiche la fenetre permettant de traiter l'autorisation du client
		
		
	}
    }  
    /**
     * Classe traitant les action sur le bouton connect du GUI. 
     * 
     */
    
    class ConnectListenner implements ActionListener{
    	
    	Forum_Gui gui;
    	public ConnectListenner (Forum_Gui i) {
        	gui = i;
	}
    	public void actionPerformed (ActionEvent e) {
       try {
		admin.connect();
	} catch (Exception e1) {
		
		e1.printStackTrace();
	}
    	}}
    /**
     * Classe traitant les action sur le bouton submit de la fentre traitant le ban du client 
     * 
     */
       class SubBanListener implements ActionListener{
       	
       	Forum_Gui gui;
       	public SubBanListener (Forum_Gui i) {
           	gui = i;
   	}
       	public void actionPerformed (ActionEvent e) {
          try {
   		
        	  admin.ban_client(gui.client_name1.getText(), gui.forum_name1.getText());
   		
        	  frame_ban_client.setVisible(false); // masquer la fenetre
   	} catch (Exception e1) {
   		// TODO Auto-generated catch block
   		e1.printStackTrace();
   	}
    		
    		
    	}
    	
       	       	
    }
       /**
        * Classe traitant les action sur le bouton submit de la fentre traitant l'autorisation du client 
        * 
        */
       class SubAuthListener implements ActionListener{
          	
          	Forum_Gui gui;
          	public SubAuthListener (Forum_Gui i) {
              	gui = i;
      	}
          	public void actionPerformed (ActionEvent e) {
             try {
      		admin.auth_client(gui.client_name2.getText(), gui.forum_name2.getText());
      		frame_auth_client.setVisible(false);
      	} catch (Exception e1) {
      		
      		e1.printStackTrace();
      	}
       		
       		
       	}}
       	  
}


