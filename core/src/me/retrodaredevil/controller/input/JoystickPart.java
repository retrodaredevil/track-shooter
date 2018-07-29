package me.retrodaredevil.controller.input;

import me.retrodaredevil.controller.ControllerPart;

public interface JoystickPart extends ControllerPart {
	/**
	 *
	 * @return returns the x axis that is controlled and updated by this SimpleJoystickPart
	 */
	InputPart getXAxis();

	/**
	 * Just like getY(), the getPosition() should be exactly the same so positive is up and negative is down.
	 * @return returns the y axis that is controlled and updated by this SimpleJoystickPart
	 */
	InputPart getYAxis();

    /**
     * NOTE: Normally, this stays constant but it is possible for it to change.
     * @return The joystick type (contains information on what the x and y values might look like and whether or not they need to be scaled when diagonal)
     */
	JoystickType getJoystickType();

	/** @return angle of the joystick in degrees. If joystick completely centered, usually returns 0. */
	double getAngle();

	/** @return The raw magnitude using the values from getX() and getY() */
	double getMagnitude();

	/**
	 * This will never be > 1 unless getJoystickType().isRangeOver() is true
	 * @return returns getMagnitude() and scales it if getJoystickType().shouldScale().
	 */
	double getCorrectMagnitude();

	/**
	 * When implementing: You should try to cache the value in update() for this so if this method is called 100 times
	 * per frame, it won't affect performance.
	 *
	 * @return The X value of the joystick -1 to 1. Or greater if getJoystickType() == MOUSE
	 */
	double getX();

	/**
	 * When implementing: You should try to cache the value in update() for this so if this method is called 100 times
	 * per frame, it won't affect performance.
	 *
	 * NOTE: When joystick is up, this is positive, when joystick is down, this is negative
	 *
	 * @return The Y value of the joystick -1 to 1 Or greater if getJoystickType() == MOUSE
	 */
	double getY();


	boolean isDeadzone();

	boolean isXDeadzone();

	boolean isYDeadzone();
}
