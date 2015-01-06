
import java.lang.*;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface ForumAdmin extends Remote {

    public void Create(String name) throws RemoteException, NotBoundException;

    public void Destroy(String forum_name) throws RemoteException;

    public String List_forum() throws RemoteException;

    public String List_client(String forum_name) throws RemoteException;

    public void Ban_client(String client_name, String forum_name) throws RemoteException;

    public void Auth_client(String client_name, String forum_name) throws RemoteException;

    public String Ping(String forum_name) throws RemoteException, InterruptedException;

    public String check_serv() throws RemoteException;

    public void updateForum(String name, HashMap map) throws RemoteException, NotBoundException;

    void updateForum1(String forum_name, Forum forum) throws RemoteException;

}
