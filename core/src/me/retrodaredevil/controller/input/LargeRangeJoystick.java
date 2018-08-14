package me.retrodaredevil.controller.input;

/**
 * Represents a joystick where its JoystickType's rangeOver is true and isInputSquare is false
 * <p>
 * NOTE: This should not be referenced with instanceof
 */
public abstract class LargeRangeJoystick extends SimpleJoystickPart {

	protected LargeRangeJoystick() {
		super(new JoystickType(true, true, false, false), false, false);
	}

	@Override
	public boolean isXDeadzone() {
		return Math.abs(getX()) <= config.getRangeOverDeadzone();
	}

	@Override
	public boolean isYDeadzone() {
		return Math.abs(getY()) <= config.getRangeOverDeadzone();
	}

}
