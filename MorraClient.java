import java.rmi.Naming;
import java.util.Scanner;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MorraClient {

	private static void logger(String msg) {
		System.out.println("[Client] " + msg);
	}

    public static void main (String[] argv) {
        try {

            //set up
            Scanner s=new Scanner(System.in);
            System.out.println("Enter Your name: ");
            String name=s.nextLine().trim();

            //create the instance of the client
			CommInterface client = new Comm(name);
			logger("Created Comm");

            //get the server remote object
			// MatchInterface server = (MatchInterface) Naming.lookup("rmi://riserva/morra");
			//Registry registry = LocateRegistry.getRegistry("riserva");
			//logger("Located registry");
			//MatchInterface server = (MatchInterface) registry.lookup("morra");
			//look_up = (RMIInterface) Naming.lookup("//localhost/MyServer");
			MatchInterface match = (MatchInterface) Naming.lookup("//127.0.0.1/server");
			logger("Got the remote object");

			while (!match.subscribe(client)) {
				System.out.println("Unable to subscribe. Retry?");
				String resp = s.nextLine().trim();
				if(!resp.equals("y") || !resp.equals("Y")) {
					return;
				}
			}

            //keep sending msgs
            while(true){
                String msg=s.nextLine().trim();
                match.play(client, msg);
            }

        }catch (Exception e) {
            System.out.println("[System] Server failed: " + e);
        }
    }

}
