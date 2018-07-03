package me.retrodaredevil.game.trackshooter.item;

import me.retrodaredevil.game.trackshooter.Updateable;

public interface Item extends Updateable {
	/** @return true if this item should be removed from the player's items */
	boolean isUsed();
}
