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

	double getAngle();

	double getMagnitude();

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
