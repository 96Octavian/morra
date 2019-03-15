import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CommInterface extends Remote {
    public String getName() throws RemoteException;
    public void printout(String msg) throws RemoteException;
}