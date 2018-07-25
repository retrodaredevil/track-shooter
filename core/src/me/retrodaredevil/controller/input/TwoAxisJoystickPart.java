package me.retrodaredevil.controller.input;

/**
 * A single joystick with two axis
 */
public class TwoAxisJoystickPart extends SimpleJoystickPart {
	private InputPart xAxis;
	private InputPart yAxis;


	/**
	 * @param x x axis where left is negative and positive is right
	 * @param y y axis where down is negative and positive is up
	 * @param shouldScale The shouldScale value for the JoystickType. Note this doesn't actually change the x or y, just the JoystickType
	 */
	public TwoAxisJoystickPart(InputPart x, InputPart y, boolean shouldScale){
		super(autoJoystickTypeHelper(x, y, shouldScale));
		this.xAxis = x;
		this.yAxis = y;
		xAxis.setParent(this);
		yAxis.setParent(this);
	}
	private static JoystickType autoJoystickTypeHelper(InputPart x, InputPart y, boolean shouldScale){
		if(!x.getAxisType().isFull() || !y.getAxisType().isFull()){
			throw new IllegalArgumentException("Each axis must have a full range.");
		}
		if(x.getAxisType().shouldUseDelta() != y.getAxisType().shouldUseDelta()){
			System.err.println("The axes in " + TwoAxisJoystickPart.class.getSimpleName() + " don't have the same shouldUseDelta() return values");
		}
		return new JoystickType(x.getAxisType().isAnalog() || y.getAxisType().isAnalog(),
				x.getAxisType().isRangeOver() || y.getAxisType().isRangeOver(),
				shouldScale, x.getAxisType().shouldUseDelta() || y.getAxisType().shouldUseDelta());
	}
	public static TwoAxisJoystickPart createFromFour(InputPart up, InputPart down, InputPart left, InputPart right){
		return new TwoAxisJoystickPart(new TwoWayInput(right, left), new TwoWayInput(up, down), true);
	}

	@Override
	public double getX(){
		return xAxis.getPosition();
	}
	@Override
	public double getY(){
		return yAxis.getPosition();
	}

	@Override
	public boolean isXDeadzone() {
		return xAxis.isDeadzone();
	}

	@Override
	public boolean isYDeadzone() {
		return yAxis.isDeadzone();
	}

	@Override
	public boolean isConnected() {
		return xAxis.isConnected() && yAxis.isConnected();
	}

	@Override
	public InputPart getXAxis(){
		return xAxis;
	}
	@Override
	public InputPart getYAxis(){
		return yAxis;
	}


}
