import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;

public class Match extends UnicastRemoteObject implements MatchInterface {

	private static final long serialVersionUID = 1L;
	private Map<Integer, Player> players;
	private Map<String, Integer> moves = new HashMap<String, Integer>();
	private ArrayList<Object> champion = new ArrayList<Object>();

	public Match(Map<Integer, Player> players) throws RemoteException {
		this.players = players;
		moves.put("Sasso", 0);
		moves.put("Carta", 1);
		moves.put("Forbice", 2);
	}

	public boolean subscribe(CommInterface client) throws RemoteException {

		Utilities.logger("Received sub request");

		Integer id = client.getID();
		Utilities.logger("Got the ID " + id + " from " + client.getName());
		if(id == 0) {
			id = MorraServer.getAvailableID();
			client.setID(id);
			Utilities.logger("User now logged with ID " + id);
		}
		if (players.containsKey(id)) {
			Utilities.logger("Already subscribed");
			client.printout("Already subscribed");
			return true;
		}

		players.put(id, new Player(client, id));
		Utilities.logger("Client joined");

		/*for (String pl : players.keySet()) {
			Utilities.logger(players.get(pl).toString());
		}*/

		client.printout("You joined the tournament");
		for (Integer playerID : players.keySet()) {
			Utilities.logger("Sending ID to " + playerID);
			if(playerID != id) {
				players.get(playerID).printer(client.getName() + " joined the tournament");
			}
		}

		return true;
	}

	private int beats(String mossaCampione, String mossaSfidante) {
		// 1: vince
		// 0: stessa mossa
		// 2: perde

		int r = moves.get(mossaCampione) - moves.get(mossaSfidante);
		if (r<0) r = r + 3;
		return r % 3;
	}

	private void broadcast(String message) {
		for (Integer playerID : players.keySet()) {
			try {
				players.get(playerID).printer(message);
			} catch (Exception e) {
				//TODO: handle exception
			}
		}

	}

	public void play(Integer ID, String mossaSfidante) throws RemoteException {

		// Check if player subscribed first
		if(!players.containsKey(ID)){
			players.get(ID).printer("You are not subscribed");
			return;
		}
		// Check if there's a champion already
		if(champion.size() == 0) {
			champion.add(players.get(ID));
			champion.add(mossaSfidante);
			players.get(ID).printer("Waiting for an opponent...");
			return;
		}
		// Check if he is already the champion
		if(((Player) champion.get(0)).getID() == ID) {
			if(champion.size() == 1) {
				champion.add(mossaSfidante);
				players.get(ID).printer("Waiting for an opponent...");
				return;
			}
			players.get(ID).printer("You already made your choice. Waiting for an opponent...");
			return;
		}

		String msg = players.get(ID).getName() + "challenged " + ((Player) champion.get(0)).getName() + "!";
		broadcast(msg);
		((Player) champion.get(0)).oneMoreGame();
		players.get(ID).oneMoreGame();

		int result = beats((String) champion.get(1), mossaSfidante);
		if(result == 1) {
			broadcast("Our champion wins");
			((Player) champion.get(0)).oneMoreWin();
			champion.remove(1);
		}
		else if(result == 0) {
			broadcast("It's a tie");
		}
		else if(result == 2) {
			broadcast("The challenger wins!");
			players.get(ID).oneMoreWin();
			champion.clear();
			champion.add(players.get(ID));
		}
	}

}