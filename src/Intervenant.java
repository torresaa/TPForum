import java.rmi.*;

public interface Intervenant extends Remote {
        int PORT = 1099;
        String CLIENT_NAME = "remote_client";
        
        int FORUM_SERVER_PORT = 1099;
        String FORUM_SERVER_IP = "localhost";
        String FORUM_SERVER_NAME = "server_forum"; 
        
	public void listen (String msg) throws RemoteException;

	public void addNewClient(IntervenantDescriptor i) throws RemoteException;

	public void delNewClient(IntervenantDescriptor i) throws RemoteException;
}