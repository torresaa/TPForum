import java.rmi.*;

public interface Intervenant extends Remote {
        String PORT = "1099";
        String CLIENT_NAME = "remote_client";
	public void listen (String msg) throws RemoteException;

	public void addNewClient(Intervenant i) throws RemoteException;

	public void delNewClient(Intervenant i) throws RemoteException;
}