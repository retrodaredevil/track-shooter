package me.retrodaredevil.game.trackshooter.input.implementations;

public interface ScreenArea {
	/**
	 * The x and y parameters are proportional to the screen. (0, 0) is the bottom left and (1, 1)
	 * is the upper right. Some android phones actually detect touches outside this range so the
	 * x and y values may not be in range [0..1]
	 * @param x The x value
	 * @param y The y value
	 * @return true if the area that this represents contains the point
	 */
	boolean containsPoint(float x, float y);
}
