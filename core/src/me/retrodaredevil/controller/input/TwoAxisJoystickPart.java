package me.retrodaredevil.controller.input;

import me.retrodaredevil.controller.ControlConfig;

/**
 * A single joystick with two axis
 */
public class TwoAxisJoystickPart extends JoystickPart {
	private InputPart xAxis;
	private InputPart yAxis;

//	public TwoAxisJoystickPart(InputPart x, InputPart y, JoystickType type){
//		super(type);
//		this.xAxis = x;
//		this.yAxis = y;
//	}

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
	public TwoAxisJoystickPart(InputPart x, InputPart y){
		this(x, y, true);
	}
	private static JoystickType autoJoystickTypeHelper(InputPart x, InputPart y, boolean shouldScale){
//		if (!x.getAxisType().equals(y.getAxisType())) {
//			throw new IllegalArgumentException("Each passed argument must have the same AxisType.");
//		}
		if(!x.getAxisType().isFull() || !y.getAxisType().isFull()){
			throw new IllegalArgumentException("Each axis must have a full range.");
		}
		return new JoystickType(x.getAxisType().isAnalog() || y.getAxisType().isAnalog(),
				x.getAxisType().isRangeOver() || y.getAxisType().isRangeOver(),
				shouldScale, x.getAxisType().shouldUseDelta() || y.getAxisType().shouldUseDelta());
	}
	public static TwoAxisJoystickPart createFromFour(InputPart up, InputPart down, InputPart left, InputPart right){
		return new TwoAxisJoystickPart(new TwoWayInput(right, left), new TwoWayInput(up, down));
	}

	@Override
	public void update(ControlConfig config) {
		xAxis.update(config);
		yAxis.update(config);
		super.update(config);
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
