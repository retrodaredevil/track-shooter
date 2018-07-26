package me.retrodaredevil.game.trackshooter.util;

import com.badlogic.gdx.graphics.Color;

public final class Constants {
	/** Should all stages' act methods be called before draw is called? */
	public static final boolean SHOULD_ACT = true;
	public static final float BULLET_SPEED = 22;
	public static final float SHOT_GUN_BULLET_SPEED = 15;

	public static final float ROTATIONAL_VELOCITY_SET_GOTO_DEADBAND = 10;

	private static final float COLOR_SHADE = 16.0f / 255.0f;
	public static final Color BACKGROUND_COLOR = new Color(COLOR_SHADE, COLOR_SHADE, COLOR_SHADE, 1);
}
