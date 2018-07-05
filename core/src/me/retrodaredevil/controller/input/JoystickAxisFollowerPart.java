package me.retrodaredevil.controller.input;

public class JoystickAxisFollowerPart extends InputPart {
	private final JoystickPart joystick;
	private final boolean useY;

	/**
	 * When this is created, it sets its own parent to the passed JoystickPart meaning JoystickPart needs to update this
	 * @param joystick The joystick
	 * @param useY true for y axis, false for x axis
	 */
	public JoystickAxisFollowerPart(JoystickPart joystick, boolean useY){
		super(autoAxisTypeHelper(joystick));
		this.joystick = joystick;
		this.useY = useY;
		this.setParent(joystick);
	}
	private static AxisType autoAxisTypeHelper(JoystickPart joystick){
		JoystickPart.JoystickType type = joystick.getJoystickType();

		if(type.isRangeOver()){
			return AxisType.FULL_RANGE_OVER_NO_DELTA;
		}
		if(type.isAnalog()){
			return AxisType.FULL_ANALOG;
		}
		return AxisType.FULL_DIGITAL;
	}

	@Override
	public double getPosition() {
		return useY ? joystick.getY() : joystick.getX();
	}

	@Override
	public boolean isDeadzone() {
		return useY ? joystick.isYDeadzone() : joystick.isXDeadzone();
	}

	@Override
	protected void positionUpdate() {
	}
	@Override
	protected double calculatePosition() {
		throw new AssertionError("Should not be called since positionUpdate() is overridden.");
	}

	@Override
	public boolean isConnected() {
		return joystick.isConnected();
	}
}
