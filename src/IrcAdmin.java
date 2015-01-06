
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.lang.*;
import java.math.BigDecimal;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;




/**
* Cette classe est la classe principale constituant le programme admin.

*/

public class IrcAdmin {

    public static void main (String args[]) {

	try{
		Forum_Gui gui= new Forum_Gui();
		AdminImpl admin = new AdminImpl("rmi", "rmi");
		admin.setGUI(gui);
		gui.setHandler(admin);
		
		

		} catch (Exception e) {
	          System.out.println("ERROR : " + e) ;
		  e.printStackTrace(System.out);
		  }
	    
	    }
}

