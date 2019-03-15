import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MorraServer {

	private static Map<String, Player> players;

	private static void broadcast(String message) {
		for (String playerID : players.keySet()) {
			try {
				players.get(playerID).printer(message);
			} catch (Exception e) {
				//TODO: handle exception
			}
		}
	}

    public static void main (String[] argv) {
        try {

			Scanner s = new Scanner(System.in);

			players =  new HashMap<String, Player>();

			// Create the object to be exposed
            Match server = new Match(players);

            // Bind the object
            Naming.rebind("rmi://localhost/morra", server);

            System.out.println("[System] The ring is ready!");

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
