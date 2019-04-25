package me.retrodaredevil.game.trackshooter.achievement;

/**
 * An achievement that is achieved when an event has reached a certain number of increments
 */
public interface EventAchievement extends Achievement {
	GameEvent getGameEvent();
	/**
	 * NOTE: Even if this returns null, it could still be revealed because it may be revealed by default.
	 *
	 * @return A number in range [1..10000] or null to never reveal
	 */
	Integer getIncrementsForReveal();

	default Boolean isRevealedByDefault(){
		return null;
	}

	/**
	 * @return The amount of increments that are needed to achieve this. May be in range [1..10000]
	 */
	int getIncrementsForAchieve();

	/**
	 * The android equivalent of this would be: Is this incremental or not?
	 * @return true if progress should be shown while viewing this achievement, false otherwise.
	 */
	boolean isProgressShown();
}
