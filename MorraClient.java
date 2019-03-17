import java.rmi.Naming;
import java.util.Scanner;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MorraClient {

	public static void main (String[] argv) {
        try {

            //set up
            Scanner s=new Scanner(System.in);
            System.out.println("Enter Your name: ");
            String name=s.nextLine().trim();

            //create the instance of the client
			CommInterface client = new Comm(name);
			Utilities.logger("Created Comm");

            //get the server remote object
			MatchInterface match = (MatchInterface) Naming.lookup("//riserva/server");
			Utilities.logger("Got the remote object");

			while (!match.subscribe(client)) {
				System.out.println("Unable to subscribe. Retry?");
				String resp = s.nextLine().trim();
				if(!resp.equals("y") || !resp.equals("Y")) {
					return;
				}
			}

            //keep sending msgs
            while (true){
                String msg=s.nextLine().trim();
                match.play(client.getID(), msg);
            }

        } catch (Exception e) {
            System.out.println("[System] Server failed: " + e);
        }
    }

}
