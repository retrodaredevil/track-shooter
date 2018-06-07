package me.retrodaredevil.game.trackshooter.effect;

import me.retrodaredevil.game.trackshooter.Updateable;

public interface Effect extends Updateable {
	/**
	 *
	 * @return true if this Effect is done and should be removed, false otherwise
	 */
	boolean isDone();

	/**
	 * NOTE: This is only for display and should not be used in place of isDone()<p>
	 * As time goes on, the return value goes from 0 to 1. (0% done, 50% done, 100% done etc)
	 *
	 * @return A number from 0 to 1 where 1 is done and 0 is not close to done OR 1 is out of ammo and 0 is full ammo etc.
	 */
	float percentDone();
}
