package Intervenant;
import java.rmi.*;

public interface Intervenant extends Remote {
	public void listen (String msg) throws RemoteException;

	public void addNewClient(IntervenantDescriptor interdes) throws RemoteException;

	public void delNewClient(IntervenantDescriptor i) throws RemoteException;
}