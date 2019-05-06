package me.retrodaredevil.game.trackshooter.account.achievement;

/**
 * This represents an Achievement
 */
public interface Achievement extends GamePart {
	/**
	 * NOTE: Even if this returns null, it could still be revealed because it may be revealed by default.
	 *
	 * @return A number in range [1..10000] or null to never reveal
	 */
	Integer getIncrementsForReveal();

	boolean isIncremental();

}
