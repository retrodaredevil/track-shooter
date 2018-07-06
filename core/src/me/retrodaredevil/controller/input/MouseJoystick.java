package me.retrodaredevil.controller.input;

/**
 * NOTE: This should not be referenced with instanceof subject to change
 */
public abstract class MouseJoystick extends SimpleJoystickPart {

	public MouseJoystick() {
		super(new JoystickType(true, true, false, false));
	}

	// TODO add x and y position abstract methods

	@Override
	public boolean isXDeadzone() {
		return Math.abs(getX()) <= config.mouseDeadzone;
	}

	@Override
	public boolean isYDeadzone() {
		return Math.abs(getY()) <= config.mouseDeadzone;
	}

}
