package me.retrodaredevil.controller.input;

/**
 * Represents information for a JoystickPart
 */
public final class JoystickType{
	//		NORMAL(true, false, true, true),
//		POV(false, false, true, true),
//		MOUSE(true, true, false, false);
	private final boolean analog;
	private final boolean rangeOver;
	private final boolean shouldScale;
	private final boolean shouldUseDelta;

	/**
	 *
	 * @param analog Does either of the joystick's axes give analog input
	 * @param rangeOver Do either of the joystick's axes's abs of getPosition go over 1
	 * @param shouldScale normally true when the magnitude of the joystick can go over 1 because the axes
	 *                    need to be scaled
	 * @param shouldUseDelta normally true unless the input device is something like a mouse or if the input
	 *                       device can be moved freely
	 */
	public JoystickType(boolean analog, boolean rangeOver, boolean shouldScale, boolean shouldUseDelta){
		this.analog = analog;
		this.rangeOver = rangeOver;
		this.shouldScale = shouldScale;
		this.shouldUseDelta = shouldUseDelta;
	}

	public boolean isAnalog(){
		return analog;
	}

	/** @return returns true if either the abs of the x or the y can go over 1 */
	public boolean isRangeOver(){
		return rangeOver;
	}

	/**
	 * NOTE: For things like MOUSE, this will return false since it doesn't make sense to scale it.
	 * <br/>
	 * If shouldUseDelta() is false, then it is usually safe to assume that this is false as well.
	 * @return true if the magnitude of x and y can go over 1.
	 */
	public boolean shouldScale(){
		return shouldScale;
	}

	/**
	 *
	 * @return true if you should apply delta time to the value
	 */
	public boolean shouldUseDelta(){
		return shouldUseDelta;
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof JoystickType){
			JoystickType type = (JoystickType) o;
			return type.analog == this.analog && type.rangeOver == this.rangeOver
					&& type.shouldScale == this.shouldScale && type.shouldUseDelta == this.shouldUseDelta;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int r = 17;
		r = 37 * r + (analog ? 1 : 0);
		r = 37 * r + (rangeOver ? 1 : 0);
		r = 37 * r + (shouldScale ? 1 : 0);
		r = 37 * r + (shouldUseDelta ? 1 : 0);
		return r;
	}
}

