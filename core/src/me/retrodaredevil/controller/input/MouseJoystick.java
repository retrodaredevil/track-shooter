package me.retrodaredevil.controller.input;

import me.retrodaredevil.controller.input.JoystickPart;

public abstract class MouseJoystick extends JoystickPart {

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
