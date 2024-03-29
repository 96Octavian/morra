import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MorraServer {

	public static Integer ID = 0;

	private static Map<Integer, Player> players;

	private static void broadcast(String message) {
		for (Integer playerID : players.keySet()) {
			try {
				players.get(playerID).printer(message);
			} catch (Exception e) {
				//TODO: handle exception
			}
		}
	}

	public static int getAvailableID() {
		ID++;
		return ID;
	}

    public static void main (String[] argv) {
        try {

			Scanner s = new Scanner(System.in);

			players =  new HashMap<Integer, Player>();
			Utilities.logger("Created list");

			// Create the object to be exposed
			Match server = new Match(players);
			Utilities.logger("Created match");
			//MatchInterface stub = (MatchInterface) UnicastRemoteObject.exportObject(server, 0);
			//Naming.rebind("//localhost/MyServer", new ServerOperation());
			Naming.rebind("//127.0.0.1/server", server);

            Utilities.logger("Binding done. The ring is ready!");

            //keep waiting new mesages from shell
            while(true){

				// Wait for a message and broadcast it to every player
                String msg=s.nextLine().trim();

				broadcast(msg);
            }

        }catch (Exception e) {
            System.out.println("[System] Server failed: " + e);
        }
    }

}
