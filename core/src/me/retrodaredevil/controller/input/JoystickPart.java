package me.retrodaredevil.controller.input;

import me.retrodaredevil.controller.ControllerPart;

/**
 * Represents a ControllerPart that has two axi (2D) representing a joystick.
 */
public interface JoystickPart extends ControllerPart, AnglePart{
	/**
	 *
	 * @return returns the x axis that is controlled and updated by this SimpleJoystickPart
	 */
	InputPart getXAxis();

	/**
	 * Just like getY(), the getPosition() should be exactly the same: positive is up and negative is down.
	 * @return returns the y axis that is controlled and updated by this SimpleJoystickPart
	 */
	InputPart getYAxis();

    /**
     * NOTE: Normally, this stays constant but it is possible for it to change.
     * @return The joystick type (contains information on what the x and y values might look like and whether or not they need to be scaled when diagonal)
     */
	JoystickType getJoystickType();

	/**
	 * NOTE: It is possible for this to return a value > 1 even if isInputSquare is correct, it could
	 * be 1.00000001. This is why it is recommended to use {@link #getCorrectMagnitude()} instead.
	 * @return The raw magnitude using the values from {@link #getX()} and {@link #getY()}
	 * */
	double getMagnitude();

	/**
	 * This will NEVER be > 1 unless getJoystickType().isRangeOver() is true
	 * @return returns getMagnitude() and scales it if
	 * 			{@link #getJoystickType()}.{@link JoystickType#isInputSquare() isInputSquare()} == true.
	 */
	double getCorrectMagnitude();

	/**
	 * When implementing: You should try to cache the value in update() for this so if this method is called 100 times
	 * per frame, it won't affect performance.
	 * <p>
	 * NOTE: Normally instead of this, you should calculate x and y using getAngle() and getCorrectMagnitude()
	 *
	 * @return The raw X value of the joystick in range [-1, 1] or an unknown range if
	 * 			{@link #getJoystickType()}.{@link JoystickType#isRangeOver() isRangeOver()} == true
	 */
	double getX();

	/**
	 * When implementing: You should try to cache the value in update() for this so if this method is called 100 times
	 * per frame, it won't affect performance.
	 *
	 * NOTE: When joystick is up, this is positive, when joystick is down, this is negative (Like a normal coordinate system)
	 * <p>
	 * NOTE: Normally instead of this, you should calculate x and y using getAngle() and getCorrectMagnitude()
	 *
	 * @return The raw Y value of the joystick in range [-1, 1] or an unknown range if
	 * 			{@link #getJoystickType()}.{@link JoystickType#isRangeOver() isRangeOver()} == true
	 */
	double getY();

	boolean isXDeadzone();

	boolean isYDeadzone();
}
