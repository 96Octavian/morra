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
	private Map<String, Player> players;
	private Map<String, Integer> moves = new HashMap<String, Integer>();
	private ArrayList<Object> currentPlayer = new ArrayList<Object>();

	public Match(Map<String, Player> players) throws RemoteException {
		this.players = players;
		moves.put("Sasso", 0);
		moves.put("Carta", 1);
		moves.put("Forbice", 2);
	}

	public boolean subscribe(CommInterface client) throws RemoteException {

		logger("Received sub request");

		String id;
		try {
			id = RemoteServer.getClientHost();
		} catch (ServerNotActiveException e) {
			return false;
		}
		logger("Got the ID " + id);
		logger("From " + client.getName());
		if (players.containsKey(id)) {
			logger("Already subscribed");
			client.printout("Already subscribed");
			return false;
		}

		players.put(id, new Player(client, id));
		logger("Client joined");
		for (String pl : players.keySet()) {
			logger(players.get(pl).toString());
		}
		client.printout("You joined the tournament");
		for (String playerID : players.keySet()) {
			logger("Sending ID to " + playerID);
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

		int r = moves.get("Forbice") - moves.get("Sasso");
		if (r<0) r = r + 3;
		return r % 3;
	}

	private void logger(String msg) {
		System.out.println("[Match] " + msg);
	}

	private void broadcast(String message) {
		for (String playerID : players.keySet()) {
			try {
				players.get(playerID).printer(message);
			} catch (Exception e) {
				//TODO: handle exception
			}
		}

	}

	public void play(CommInterface client, String mossaSfidante) throws RemoteException {

		String ID;
		try {
			ID = RemoteServer.getClientHost();
		} catch (Exception e) {
			client.printout("Error");
			return;
		}
		
		// Check if player subscribed first
		if(!players.containsKey(ID)){
			client.printout("You are not subscribed");
			return;
		}
		// Check if there's a champion already
		if(currentPlayer.size() == 0) {
			currentPlayer.add(players.get(ID));
			currentPlayer.add(mossaSfidante);
			players.get(ID).printer("Waiting for an opponent...");
			return;
		}
		// Check if he is already the champion
		if(((Player) currentPlayer.get(0)).getID() == ID) {
			if(currentPlayer.size() == 1) {
				currentPlayer.add(mossaSfidante);
			}
			players.get(ID).printer("You already made your choice. Waiting for an opponent...");
			return;
		}

		String msg = players.get(ID).getName() + "challenged " + ((Player) currentPlayer.get(0)).getName() + "!";
		broadcast(msg);
		((Player) currentPlayer.get(0)).oneMoreGame();
		players.get(ID).oneMoreGame();

		int result = beats((String) currentPlayer.get(1), mossaSfidante);
		if(result == 1) {
			broadcast("Our champion wins");
			((Player) currentPlayer.get(0)).oneMoreWin();
		}
		else if(result == 0) {
			broadcast("It's a tie");
		}
		else if(result == 2) {
			broadcast("The challenger wins!");
			players.get(ID).oneMoreWin();
			currentPlayer.add(players.get(ID));
			currentPlayer.remove(1);
		}
	}

}