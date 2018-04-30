package me.retrodaredevil.input;

/**
 * A single input such as a button, joystick axis, pov axis, etc
 */
public abstract class InputPart extends ControllerPart{
	private AxisType type;

	private Double position = null;
	private Double previousPosition = null;

	public InputPart(AxisType type){
		this.type = type;
	}

	public AxisType getAxisType(){
		return type;
	}

	/**
	 * Overridden by subclasses
	 * <p>
	 * When implementing:
	 * The returned value should comply with whatever getAxisType() returns
	 * @return The raw(est) position of the input
	 */
	protected abstract double calculatePosition();

	/**
	 * Can be used for all AxisTypes.
	 * @return
	 */
	public boolean isDown(){
		return getPosition() > this.config.buttonDownDeadzone;
	}

	public boolean isPressed(){
		return isDown() && (previousPosition == null || previousPosition <= this.config.buttonDownDeadzone);
	}
	public boolean isReleased(){
		return !isDown() && (previousPosition != null && previousPosition > this.config.buttonDownDeadzone);
	}

	/**
	 * @return true if the current getValue() is in a deadzone or close enough to 0
	 */
	public boolean isDeadzone(){
		double deadzone;
		switch(getAxisType()){
			case FULL_ANALOG:
				deadzone = config.fullAnalogDeadzone;
				break;
			case ANALOG:
				deadzone = config.analogDeadzone;
				break;
			case DIGITAL:
				deadzone = config.digitalDeadzone;
				break;
			case FULL_DIGITAL:
				deadzone = config.fullDigitalDeadzone;
				break;
			default: // usually digital
				throw new UnsupportedOperationException("We didn't account for AxisType: " + getAxisType());
		}
		return Math.abs(position) < deadzone;
	}

	/**
	 * @return returns getValue() unless getAxisType() returns FULL_ANALOG or ANALOG, it will round it using the
	 * ControlConfig#analogDigitalValueDeadzone;
	 */
	public int getDigitalPosition(){
		double value = getPosition();

		if(value > config.analogDigitalValueDeadzone){
			return 1;
		} else if(value < -config.analogDigitalValueDeadzone){
			return -1;
		}

		return 0;
	}


	/**
	 * Depending on what getAxisType() returns, the returned value may have a smaller range.
	 * @return The value of this axis/button. This will always be within -1 and 1 (inclusive)
	 */
	public double getPosition(){
		if(this.position == null){
			throw new NullPointerException("this.value was not initialized. update() must not have been called.");
		}
		return this.position;
	}

	@Override
	public void update(ControlConfig config) {
		super.update(config);
		if(this.position != null){
			this.previousPosition = this.position;
		}
		this.position = calculatePosition();
	}

	public enum AxisType{
		/** Used when range of getValue() is -1 to 1. Normally stays at 0 when still. */
		FULL_ANALOG,
		/** Used when getValue() is -1, 0, or 1. Normally stays at 0 when still. */
		FULL_DIGITAL,
		/** Used when range of getValue() is 0 to 1 */
		ANALOG,
		/** Used when getValue() is 0 or 1 */
		DIGITAL
	}

}
