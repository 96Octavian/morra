import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Comm extends UnicastRemoteObject implements CommInterface   {

	private static final long serialVersionUID = 1L;
	
	private String name;

    public Comm(String n) throws RemoteException {
        this.name=n;
    }

	public String getName() throws RemoteException {
        return this.name;
    }

    public void printout(String msg) throws RemoteException {
        System.out.println(msg);
    }

}
