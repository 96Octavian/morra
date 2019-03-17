import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Comm extends UnicastRemoteObject implements CommInterface   {

	private static final long serialVersionUID = 1L;
	private String name;
	private int ID = 0;

	public void setID(int newID) throws RemoteException {
		ID = newID;
	}

	public Integer getID() throws RemoteException {
		return ID;
	}

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
