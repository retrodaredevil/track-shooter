package me.retrodaredevil.game.trackshooter.multiplayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DisconnectedMultiplayer implements Multiplayer {

	private final Collection<Player> players;

	public DisconnectedMultiplayer(int numberOfPlayers){
		players = new ArrayList<>(numberOfPlayers);
		for(int i = 0; i < numberOfPlayers; i++){
			players.add(new DisconnectedPlayer());
		}
	}
	@Override
	public boolean isConnected() {
		return false;
	}

	@Override
	public void leave() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isHost() {
		return true;
	}

	@Override
	public void sendToHost(Packet packet) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void sendToEveryone(Packet packet) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void sendToPlayer(Packet packet, Player player) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void sendToPlayers(Packet packet, List<Player> players) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<? extends Player> getPlayers() {
		return players;
	}

	@Override
	public Collection<? extends Player> getHandledPlayers() {
		return players;
	}

	@Override
	public Collection<? extends Player> getNonHandledPlayers() {
		return Collections.emptyList();
	}

	private static class DisconnectedPlayer implements Player {

		@Override
		public boolean isConnected() {
			return true;
		}

		@Override
		public boolean isHandledByUs() {
			return true;
		}

		@Override
		public boolean isPlayerHost() {
			return true;
		}
	}
}
