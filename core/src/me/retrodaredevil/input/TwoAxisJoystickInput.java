package me.retrodaredevil.input;

/**
 * A single joystick with two axis
 */
public class TwoAxisJoystickInput extends JoystickInput{
	private SingleInput xAxis;
	private SingleInput yAxis;

//	public TwoAxisJoystickInput(SingleInput x, SingleInput y, JoystickType type){
//		super(type);
//		this.xAxis = x;
//		this.yAxis = y;
//	}
	public TwoAxisJoystickInput(SingleInput x, SingleInput y){
		super(autoJoystickTypeHelper(x, y));
		this.xAxis = x;
		this.yAxis = y;
		xAxis.setParent(this);
		yAxis.setParent(this);
	}
	private static JoystickType autoJoystickTypeHelper(SingleInput x, SingleInput y){
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

	public SingleInput getXAxis(){
		return xAxis;
	}
	public SingleInput getYAxis(){
		return yAxis;
	}


}
