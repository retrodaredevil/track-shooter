package me.retrodaredevil.controller.input;

/**
 * Caches the position of the x or y axis of a joystick. The parent of this class should be
 * the joystick we are using so the joystick must update us after it calculates its own position.
 */
public class JoystickAxisFollowerPart extends AutoCachingInputPart {
	private final JoystickPart joystick;
	private final boolean useY;

	/**
	 * When this is created, it sets its own parent to the passed SimpleJoystickPart meaning the passed Joystick needs to update this (should happen automatically)
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
		JoystickType type = joystick.getJoystickType();

		return new AxisType(true, type.isAnalog(), type.isRangeOver(), type.isShouldUseDelta());
	}


	@Override
	public boolean isDeadzone() {
		return useY ? joystick.isYDeadzone() : joystick.isXDeadzone();
	}

	@Override
	protected double calculatePosition() {
		return useY ? joystick.getY() : joystick.getX();
	}

	@Override
	public boolean isConnected() {
		return joystick.isConnected();
	}
}
