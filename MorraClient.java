import java.rmi.Naming;
import java.util.Scanner;

public class MorraClient {

    public static void main (String[] argv) {
        try {

            //set up
            Scanner s=new Scanner(System.in);
            System.out.println("Enter Your name: ");
            String name=s.nextLine().trim();

            //create the instance of the client
			CommInterface client = new Comm(name);

            //get the server remote object
            MatchInterface server = (MatchInterface) Naming.lookup("rmi://localhost/morra");

			while (!server.subscribe(client)) {
				System.out.println("Unable to subscribe. Retry?");
				String resp = s.nextLine().trim();
				if(!resp.equals("y") || !resp.equals("Y")) {
					return;
				}
			}

            //keep sending msgs
            while(true){
                String msg=s.nextLine().trim();
                server.play(client, msg);
            }

        }catch (Exception e) {
            System.out.println("[System] Server failed: " + e);
        }
    }

}
