import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.function.Consumer;
import java.rmi.server.RemoteServer;

public class Player {

	private String name;
	private final String ID;
	private int wins;
	private int games;
	private MyInterface<String> printerz;

	public Player(CommInterface client, String ID) throws RemoteException{
		name = client.getName();
		this.ID = ID;
		printerz = client::printout;
		wins = 0;
		games = 0;
	}

	public void printer(String msg) throws RemoteException{
		printerz.accept(msg);
	}

	public String getName() {
		return name;
	}

	public String getID() {
		return ID;
	}

	public void oneMoreGame() {
		games++;
	}

	public void oneMoreWin() {
		wins++;
	}

}