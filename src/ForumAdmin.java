
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.lang.*;
import java.math.BigDecimal;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;




	



import java.rmi.*;
import java.util.HashMap;



public interface ForumAdmin extends Remote  {

	
	public void Create(String name)throws RemoteException, NotBoundException;

    public void Destroy(String forum_name)throws RemoteException;

    public String List_forum()throws RemoteException;

    public String List_client(String forum_name)throws RemoteException;
    
    public void Ban_client(String client_name,String forum_name)throws RemoteException; 

    public void Auth_client(String client_name,String forum_name)throws RemoteException;

    public String Ping(String forum_name)throws RemoteException, InterruptedException;

	public String check_serv()throws RemoteException;

	public void updateForum(String name,HashMap map)throws RemoteException, NotBoundException;

	

	void updateForum1(String forum_name, Forum forum) throws RemoteException;
	
}
