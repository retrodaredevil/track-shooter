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
	public TwoAxisJoystickPart(InputPart x, InputPart y){
		super(autoJoystickTypeHelper(x, y));
		this.xAxis = x;
		this.yAxis = y;
		xAxis.setParent(this);
		yAxis.setParent(this);
	}
	private static JoystickType autoJoystickTypeHelper(InputPart x, InputPart y){
		if (x.getAxisType() != y.getAxisType()) {
			throw new IllegalArgumentException("Each passed argument must have the same AxisType.");
		}
		switch(x.getAxisType()){ // note this is also the same as y.getAxisType()
			case FULL_ANALOG:
				return JoystickType.NORMAL;
			case FULL_DIGITAL:
				return JoystickType.POV;
			default:
				throw new IllegalArgumentException("Unsupported AxisType: " + x.getAxisType().toString());
		}
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
