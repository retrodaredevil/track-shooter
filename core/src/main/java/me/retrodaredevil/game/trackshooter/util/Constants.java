package me.retrodaredevil.game.trackshooter.util;

public final class Constants {
	/** In world units */
	public static final float BULLET_SPEED = 22;
	/** In world units */
	public static final float SHOT_GUN_BULLET_SPEED = 15;

	/** In degrees */
	public static final float ROTATIONAL_VELOCITY_SET_GOTO_DEADBAND = 10;
	/** In world units*/
	public static final float TRAVEL_VELOCITY_SET_GOTO_DEADBAND = .5f;

	public static final float VECTOR_VELOCITY_SET_GOTO_DEADBAND = .1f;

	/** The normal size for buttons on menus with buttons */
	public static final Size START_SCREEN_BUTTON_SIZE = Size.createSize(380, 68);
	public static final Size PAUSE_BUTTON_SIZE = Size.createSize(220, 60);

	/** The maximum velocity the player can travel at on the track*/
	public static final float PLAYER_VELOCITY = 5f;

	public static final float PLAYER_FREE_VELOCITY = 5f;

}
