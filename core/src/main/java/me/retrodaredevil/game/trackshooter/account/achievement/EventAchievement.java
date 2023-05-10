package me.retrodaredevil.game.trackshooter.account.achievement;

/**
 * An achievement that is achieved when an event has reached a certain number of increments
 *
 * NOTE: In the android implementation, this will not be an incremental achievement if {@link #getIncrementsForReveal()} == 1.
 * However, it can still be handled the same way for the most part.
 */
public interface EventAchievement extends Achievement{
	GameEvent getGameEvent();

}
