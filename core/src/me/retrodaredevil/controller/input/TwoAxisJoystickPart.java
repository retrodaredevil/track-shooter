package me.retrodaredevil.controller.input;

import java.util.Arrays;

/**
 * A single joystick with two axis
 */
public class TwoAxisJoystickPart extends SimpleJoystickPart {
	private InputPart xAxis;
	private InputPart yAxis;


	/**
	 * This will change x and y's parents to this only if needed. Note if they already have parents,
	 * then those parents must update before this does or there will be incorrect results or errors thrown.
	 * <p>
	 * It is not recommended that x or y have parents when this constructor is called.
	 * @param x x axis where left is negative and positive is right
	 * @param y y axis where down is negative and positive is up
	 * @param shouldScale Is the input from each x and y a "square".
	 *                    Almost always false with autoCorrectNeedsScaleMagnitudes is true. If set to true, autoCorrectNeedsScaleMagnitudes will have no effect
	 * @param autoCorrectNeedsScaleMagnitudes Should this change to shouldScale only if needed.
	 *                                        Should only be set to false if you are sure shouldScale will and should always be false.
	 *                                        If this is true and the x or y axis support range over, this will be set to false and have no effect
	 */
	public TwoAxisJoystickPart(InputPart x, InputPart y, boolean shouldScale, boolean autoCorrectNeedsScaleMagnitudes){
		super(autoJoystickTypeHelper(x, y, shouldScale), true,
				autoCorrectNeedsScaleMagnitudes && !(x.getAxisType().isRangeOver() || y.getAxisType().isRangeOver()));
		this.xAxis = x;
		this.yAxis = y;
//		xAxis.setParent(this);
//		yAxis.setParent(this);
		this.addChildren(Arrays.asList(xAxis, yAxis), true, true);
	}

	/**
	 * The recommended way of constructing a TwoAxisJoystickPart.
	 * <p>
	 * Calls {@link TwoAxisJoystickPart#TwoAxisJoystickPart(InputPart, InputPart, boolean, boolean)}
	 * with shouldScale=false and autoCorrectNeedsScaleMagnitude=true meaning that at first
	 * this expects the magnitude to not go over 1 but if it goes over too much it will switch to
	 * shouldScale=true
	 * @param x x axis where left is negative and positive is right
	 * @param y y axis where down is negative and positive is up
	 */
	public TwoAxisJoystickPart(InputPart x, InputPart y){
		this(x, y, false, true);
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
		return new TwoAxisJoystickPart(new TwoWayInput(right, left), new TwoWayInput(up, down));
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
