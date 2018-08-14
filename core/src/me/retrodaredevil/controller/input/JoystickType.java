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
	private final boolean isInputSquare;
	private final boolean shouldUseDelta;

	/**
	 *
	 * @param analog Does either of the joystick's axes give analog input
	 * @param rangeOver Do either of the joystick's axes's abs of getPosition go over 1
	 * @param isInputSquare normally true when the magnitude of the joystick can go over 1 because the axes
	 *                    need to be scaled. (Used if the joystick can produce a square output ex: x can be 1 when y is 1)
	 * @param shouldUseDelta normally true unless the input device is something like a mouse or if the input
	 *                       device can be moved freely
	 */
	public JoystickType(boolean analog, boolean rangeOver, boolean isInputSquare, boolean shouldUseDelta){
		this.analog = analog;
		this.rangeOver = rangeOver;
		this.isInputSquare = isInputSquare;
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
	 * If isShouldUseDelta() is false, then it is usually safe to assume that this is false as well.
     * <br/><br/>
     * Some joystick's magnitude can go above 1 when going into corners (it can get as high as sqrt(2)).
     * If this is true, then you should scale the joystick accordingly. Sometimes this is called the shape of the input.
     * (It can be a circle or a square)
	 * @return true if the magnitude of x and y can go over 1.
	 */
	public boolean isInputSquare(){
		return isInputSquare;
	}

	/**
	 * If true, this usually means that whatever this JoystickType represents returns a POSITION.
	 * <br/>
	 * If false this usually means that whatever this JoystickType represents returns a CHANGE IN POSITION
	 * @return true if you should apply delta time to the value
	 */
	public boolean isShouldUseDelta(){
		return shouldUseDelta;
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof JoystickType){
			JoystickType type = (JoystickType) o;
			return type.analog == this.analog && type.rangeOver == this.rangeOver
					&& type.isInputSquare == this.isInputSquare && type.shouldUseDelta == this.shouldUseDelta;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int r = 17;
		r = 37 * r + (analog ? 1 : 0);
		r = 37 * r + (rangeOver ? 1 : 0);
		r = 37 * r + (isInputSquare ? 1 : 0);
		r = 37 * r + (shouldUseDelta ? 1 : 0);
		return r;
	}
}

