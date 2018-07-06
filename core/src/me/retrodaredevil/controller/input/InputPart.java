package me.retrodaredevil.controller.input;

import me.retrodaredevil.controller.ControllerPart;

public interface InputPart extends ControllerPart {
	/**
	 *
	 * @return returns an AxisType object with info on what getPosition() is allowed to return
	 */
	AxisType getAxisType();

	/**
	 *
	 * @return true if the value returned by getPosition() is close enough to 0 to be ignored.
	 */
	boolean isDeadzone();

	/**
	 * Depending on what getAxisType() returns, the returned value may have a smaller range.
	 * @return The value of this axis/button. This will always be within -1 and 1 (inclusive)
	 */
	double getPosition();


	/**
	 * Can be used for all AxisTypes.
	 * @return true if this input part is currently down
	 */
	boolean isDown();

	/**
	 *
	 * @return returns true the first frame the key is pressed down.
	 */
	boolean isPressed();

	/**
	 *
	 * @return true the first frame the button wasn't down
	 */
	boolean isReleased();


	/**
	 * NOTE: This will only ever return 1, 0, and -1
	 * @return returns getPosition() unless getAxisType().isAnalog() or getAxisType().isRangeOver(), it will round it using the
	 *          ControlConfig#analogDigitalValueDeadzone;
	 */
	int getDigitalPosition();
}
