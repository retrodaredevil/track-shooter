package me.retrodaredevil.controller.input;

import me.retrodaredevil.controller.ControlConfig;
import me.retrodaredevil.controller.ControllerPart;
import me.retrodaredevil.controller.NotConnectedException;

/**
 * A single controller such as a button, joystick axis, pov axis, etc
 */
public abstract class InputPart extends ControllerPart {
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
	 * @return The raw(est) position of the controller
	 */
	protected abstract double calculatePosition();

	/**
	 * Can be used for all AxisTypes.
	 * @return
	 */
	public boolean isDown() {
		return getPosition() > this.config.buttonDownDeadzone;
	}

	public boolean isPressed() {
		return isDown() && (previousPosition == null || previousPosition <= this.config.buttonDownDeadzone);
	}
	public boolean isReleased() {
		return !isDown() && (previousPosition != null && previousPosition > this.config.buttonDownDeadzone);
	}

	/**
	 * @return true if the current getValue() is in a deadzone or close enough to 0
	 */
	public boolean isDeadzone() {
		return Math.abs(position) <= getDeadzone(getAxisType(), config);
	}
	public static double getDeadzone(AxisType type, ControlConfig config){
		switch(type){
			case FULL_ANALOG:
				return config.fullAnalogDeadzone;
			case ANALOG:
				return config.analogDeadzone;
			case DIGITAL:
				return config.digitalDeadzone;
			case FULL_DIGITAL:
				return config.fullDigitalDeadzone;
			default:
				throw new UnsupportedOperationException("We didn't account for AxisType: " + type);
		}
	}

	/**
	 * @return returns getValue() unless getAxisType().isAnalog(), it will round it using the
	 * ControlConfig#analogDigitalValueDeadzone;
	 */
	public int getDigitalPosition() {
		double value = getPosition();
		if(!getAxisType().isFull()){
			if(value > config.buttonDownDeadzone){
				return 1;
			}
		} else {
			if (value > config.analogDigitalValueDeadzone) {
				return 1;
			} else if (value < -config.analogDigitalValueDeadzone) {
				return -1;
			}
		}

		return 0;
	}


	/**
	 * Depending on what getAxisType() returns, the returned value may have a smaller range.
	 * @return The value of this axis/button. This will always be within -1 and 1 (inclusive)
	 */
	public double getPosition() {
		if(this.position == null){
			throw new NullPointerException("this.position was not initialized. update() must not have been called.");
		}
		return this.position;
	}

	@Override
	public void update(ControlConfig config) {
		super.update(config);
		positionUpdate();
	}

	/**
	 * Calls in update(). This is meant to be overridden if the default behaviour of calling calculatePosition() is not
	 * needed and you would like to handle isPressed() by yourself
	 */
	protected void positionUpdate(){
		if(this.position != null){
			this.previousPosition = this.position;
		}
		this.position = calculatePosition();
	}

	public enum AxisType{
		/** Used when range of getValue() is -1 to 1. Normally stays at 0 when still. */
		FULL_ANALOG(true, true),
		/** Used when getValue() is -1, 0, or 1. Normally stays at 0 when still. */
		FULL_DIGITAL(true, false),
		/** Used when range of getValue() is 0 to 1 */
		ANALOG(false, true),
		/** Used when getValue() is 0 or 1 */
		DIGITAL(false, false),
		/** Rarely if ever used. Can be used for a mouse that goes one way*/
		RANGE_OVER_NO_DELTA(false, true, true, false),
		/** Usually used with something like a mouse*/
		FULL_RANGE_OVER_NO_DELTA(true, true, true, false);

		private final boolean full;
		private final boolean analog;
		private final boolean rangeOver;
		private final boolean shouldUseDelta;
		AxisType(boolean full, boolean analog){
			this(full, analog, false, true);
		}

		/**
		 *
		 * @param full Can the values be negative
		 * @param analog Can the values be in between. Ex: .5
		 * @param rangeOver Can the abs of the value be > 1
		 * @param shouldUseDelta Should delta time be applied
		 */
		AxisType(boolean full, boolean analog, boolean rangeOver, boolean shouldUseDelta){
			this.full = full;
			this.analog = analog;
			this.rangeOver = rangeOver;
			this.shouldUseDelta = shouldUseDelta;
		}

		public boolean isFull(){
			return full;
		}
		public boolean isAnalog(){
			return analog;
		}
		public boolean isRangeOver(){
			return rangeOver;
		}
		public boolean shouldUseDelta(){
			return shouldUseDelta;
		}
		public boolean equals(boolean full, boolean analog, boolean rangeOver, boolean shouldUseDelta){
			return this.full == full && this.analog == analog && this.rangeOver == rangeOver && this.shouldUseDelta == shouldUseDelta;
		}
		public boolean equalsIgnoreAnalog(boolean full, boolean rangeOver, boolean shouldUseDelta){
			return this.full == full && this.rangeOver == rangeOver && this.shouldUseDelta == shouldUseDelta;
		}
		public static AxisType getAxisType(boolean full, boolean analog, boolean rangeOver, boolean shouldUseDelta){
			for(AxisType type : values()){
				if(type.equals(full, analog, rangeOver, shouldUseDelta)){
					return type;
				}
			}
			for(AxisType type : values()){
				if(type.equalsIgnoreAnalog(full, rangeOver, shouldUseDelta)){
					return type;
				}
			}
			return null;
		}
		public static AxisType getAxisType(InputPart... parts){
			boolean anyFull = false;
			boolean anyAnalog = false;
			boolean anyRangeOver = false;
			Boolean shouldUseDelta = null;
			for(InputPart part : parts){
				anyFull = anyFull || part.getAxisType().isFull();
				anyAnalog = anyAnalog || part.getAxisType().isAnalog();
				anyRangeOver = anyRangeOver || part.getAxisType().isRangeOver();
				if(shouldUseDelta == null){
					shouldUseDelta = part.getAxisType().shouldUseDelta();
				} else {
					if(shouldUseDelta != part.getAxisType().shouldUseDelta()){
						throw new IllegalArgumentException("Each passed InputPart in parts should have the same value for shouldUseDelta()");
					}
				}
			}
			return getAxisType(anyFull, anyAnalog, anyRangeOver, shouldUseDelta);
		}
	}

}
