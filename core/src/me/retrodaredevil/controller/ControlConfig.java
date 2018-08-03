package me.retrodaredevil.controller;

/**
 * NOTE: For all deadzones, they will return true if the position is less than OR equal to the deadzone
 */
public class ControlConfig {
	/** Should be used for all isDown() calls */
	public double buttonDownDeadzone = .5;
	/** Should be used for things like D-pads and POVs and all digitalPosition calls */
	public double analogDigitalValueDeadzone = buttonDownDeadzone;
	/** Should be used for ANALOG AxisTypes */
	public double analogDeadzone = .005;
	/** Should be used for FULL_ANALOG AxisTypes */
	public double fullAnalogDeadzone = .005;
	/** Usually not relevant (since it is usually only comparing -1, 0 or 1).
	 * Used for checking if in deadzone and should not be used for anything analog */
	public double digitalDeadzone = .005;
	/** Usually not relevant */
	public double fullDigitalDeadzone = digitalDeadzone;

	public double rangeOverDeadzone = 0;

	/** true if the angle and magnitude of a joystick should be calculated in lateUpdate(), otherwise it will only be
	 * cached once one of those methods are called. */
	public boolean cacheAngleAndMagnitudeInUpdate = false;
	/** Since some controllers allow you to see how much a trigger is being pressed and if it's down,
	 * setting this to true will use the controller's interpretation of when an analog button (like a trigger)
	 * is pressed */
	public boolean useAbstractedIsDownIfPossible = true;

	public double switchToShouldScaleThreshold = 1.01;
}
