package me.retrodaredevil.game.trackshooter.multiplayer;

import java.io.Serializable;

public interface Packet extends Serializable {
	boolean isReliable();
}
