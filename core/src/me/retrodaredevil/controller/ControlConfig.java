package me.retrodaredevil.controller;

/**
 * NOTE: For all deadzones, they will return true if the position is less than OR equal to the deadzone
 */
public interface ControlConfig {
	/** Should be used for all isDown() calls */
	double getButtonDownDeadzone();
	/** Should be used for things like D-pads and POVs and all digitalPosition calls */
	double getAnalogDigitalValueDeadzone();
	/** Should be used for AxisTypes with full=false and analog=true */
	double getAnalogDeadzone();
	/** Should be used for AxisTypes with full=true and analog=true*/
	double getFullAnalogDeadzone();
	/** Usually not relevant (since it is usually only comparing -1, 0 or 1).
	 * Used for checking if in deadzone and should not be used for anything analog. Really, if you set this to 0,
	 * there should be no difference but it is recommended to use a normal deadzone in case of round off errors
	 * or there being analog input for an unknown reason.*/
	double getDigitalDeadzone();
	/** Just as relevant as {@link #getDigitalDeadzone()} used for full digital*/
	double getFullDigitalDeadzone();
	/** Deadzone used for range over axis types*/
	double getRangeOverDeadzone();

	/** true if the angle and magnitude of a joystick should be calculated in lateUpdate(), otherwise it will only be
	 * cached once one of those methods are called. */
	boolean isCacheAngleAndMagnitudeInUpdate();
	/** Since some controllers allow you to see how much a trigger is being pressed and if it's down,
	 * setting this to true will use the controller's interpretation of when an analog button (like a trigger)
	 * is pressed */
	boolean isUseAbstractedIsDownIfPossible();

	/** Some implementations of JoystickParts allow you to determine if it should check to see if the magnitude
	 * of the joystick is over 1 and change the AxisType to scale it if the magnitude is > this threshold.*/
	double getSwitchToSquareInputThreshold();
}
