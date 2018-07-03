package me.retrodaredevil.controller;

/**
 * NOTE: For all deadzones, they will return true if the position is less than OR equal to the deadzone
 */
public class ControlConfig {
	/** Should be used for all isDown() calls */
	public double buttonDownDeadzone = .5;
	/** Should be used for things like D-pads and POVs */
	public double analogDigitalValueDeadzone = buttonDownDeadzone;
	/** Should be used for ANALOG AxisTypes */
	public double analogDeadzone = .001;
	/** Should be used for FULL_ANALOG AxisTypes */
	public double fullAnalogDeadzone = .004;
	/** Not used often if at all. Used for checking if in deadzone and should not be used for anything analog */
	public double digitalDeadzone = .001;
	public double fullDigitalDeadzone = digitalDeadzone;

	public double mouseDeadzone = 0;

	/** true if the angle and magnitude of a joystick should be calculated in lateUpdate(), otherwise it will only be
	 * cached once one of those methods are called. */
	public boolean cacheAngleAndMagnitudeInUpdate = false;

	// TODO public boolean scaleFromDeadzone; ex: is deadzone is .001, .001 is 0 and everything is LERPed accordingly
}
