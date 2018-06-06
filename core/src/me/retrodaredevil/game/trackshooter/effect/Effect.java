package me.retrodaredevil.game.trackshooter.effect;

import me.retrodaredevil.game.trackshooter.Updateable;

public interface Effect extends Updateable {
	/**
	 *
	 * @return true if this Effect is done and should be removed, false otherwise
	 */
	boolean isDone();

	/**
	 * NOTE: This is only for display and should not be used in place of isDone()
	 *
	 * @return A number from 0 to 1 where 0 is done and 1 is not close to done OR 0 is out of ammo and 1 is full ammo etc.
	 */
	float percentDone();
}
