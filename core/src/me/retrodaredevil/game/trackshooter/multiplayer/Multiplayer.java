package me.retrodaredevil.game.trackshooter.multiplayer;

import java.util.Collection;
import java.util.List;

/**
 * Represents a single multiplayer game.
 */
public interface Multiplayer {

	boolean isConnected();
	void leave();

	/** @return true if we are the host, false otherwise */
	boolean isHost();

	void sendToHost(Packet packet);
	void sendToEveryone(Packet packet);
	void sendToPlayer(Packet packet, Player player);
	void sendToPlayers(Packet packet, List<Player> players);

	/** @return A collection of players including us*/
	Collection<? extends Player> getPlayers();

	interface Player {
	}
}
