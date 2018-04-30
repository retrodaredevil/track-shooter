package me.retrodaredevil.input;

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

	// TODO public boolean scaleFromDeadzone; ex: is deadzone is .001, .001 is 0 and everything is LERPed accordingly
}
