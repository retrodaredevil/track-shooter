package me.retrodaredevil.game.trackshooter.level;

/**
 * By using this instead of a boolean value called canEnd, you are able to tell if danger is still
 * in the level. For instance, when there are no CANNOT_END requests in a level, and there are
 * CAN_END_SOONs, that usually means that there is no danger left so we stop the player from firing
 * bullets. When there are only CAN_END requests left, we then know that we can go on to the next
 * level.
 */
public enum LevelEndState {
	/** Represents a level that can end*/
	CAN_END(1),
	/** Represents a level that can't end, but is expected to end soon*/
	CAN_END_SOON(2),
	/** Represents a level that cannot end and it not expected to end in a certain amount of time*/
	CANNOT_END(3);

	public final int value;

	LevelEndState(int value){
		this.value = value;
	}
}
