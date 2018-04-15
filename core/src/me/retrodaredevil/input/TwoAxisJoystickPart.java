package me.retrodaredevil.input;

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

	@Override
	public void update(ControlConfig config) {
		super.update(config);
		xAxis.update(config);
		yAxis.update(config);
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
	public boolean isConnected(ControllerManager manager) {
		return xAxis.isConnected(manager) && yAxis.isConnected(manager);
	}

	public InputPart getXAxis(){
		return xAxis;
	}
	public InputPart getYAxis(){
		return yAxis;
	}


}
