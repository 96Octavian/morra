import java.rmi.RemoteException;
import java.rmi.Remote;

public interface MatchInterface extends Remote {

	public void play(CommInterface client, String mossaSfidante) throws RemoteException;
	public boolean subscribe(CommInterface client) throws RemoteException;

}