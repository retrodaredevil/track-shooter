package me.retrodaredevil.controller;

public abstract class MouseJoystick extends JoystickPart {

	public MouseJoystick() {
		super(JoystickType.MOUSE);
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
