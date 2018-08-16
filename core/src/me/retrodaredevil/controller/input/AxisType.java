package me.retrodaredevil.controller.input;

/**
 * An immutable class that represents information used with an InputPart.
 * <p>
 * There are some general constant AxisTypes such as FULL_ANALOG and these should never be compared with ==
 * <p>
 * When using an AxisType, you can rely on isFull(), isRangeOver() and isShouldUseDelta() to be accurate
 * but analog should be accurate but is usually not guaranteed
 */
public final class AxisType{
	/** Used when range of getValue() is -1 to 1. Normally stays at 0 when still. */
	public static final AxisType FULL_ANALOG = new AxisType(true, true);
	/** Used when getValue() is -1, 0, or 1. Normally stays at 0 when still. */
	public static final AxisType FULL_DIGITAL = new AxisType(true, false);
	/** Used when range of getValue() is 0 to 1 */
	public static final AxisType ANALOG = new AxisType(false, true);
	/** Used when getValue() is 0 or 1 */
	public static final AxisType DIGITAL = new AxisType(false, false);

	private final boolean full;
	private final boolean analog;
	private final boolean rangeOver;
	private final boolean shouldUseDelta;

	/**
	 * Calls {@link #AxisType(boolean, boolean, boolean, boolean)} with rangeOver=false, isShouldUseDelta=true
	 * @param full Can the values be negative. If true range: [-1, 1] if false range: [0, 1]
	 * @param analog Can the values be in between. Ex: .5
	 */
	public AxisType(boolean full, boolean analog){
		this(full, analog, false, true);
	}

	/**
	 * @param full Can the values be negative
	 * @param analog Can the values be in between. Ex: .5
	 * @param rangeOver Can the abs of the value be > 1
	 * @param shouldUseDelta Should delta time be applied. For position input, should be true, for change in position (like a mouse), it should be false
	 */
	public AxisType(boolean full, boolean analog, boolean rangeOver, boolean shouldUseDelta){
		this.full = full;
		this.analog = analog;
		this.rangeOver = rangeOver;
		this.shouldUseDelta = shouldUseDelta;
	}

	/** @return true if the values can be negative. range [-1, 1] */
	public boolean isFull(){
		return full;
	}

	/** @return true if the values can be analog (in between) */
	public boolean isAnalog(){
		return analog;
	}

	/**
	 * A good example of the range being over is a mouse where {@link #isShouldUseDelta()} == true as well
	 * <p>
	 * Another example is a controller using tilt controls: The range can be over 1 if the phone is tilted
	 * too far and the angle will be taken into account but you can normalize the magnitude if > 1.
	 * @return true if the range of what this AxisType represents can be > 1
	 */
	public boolean isRangeOver(){
		return rangeOver;
	}

	/**
	 * If true, this usually means that whatever this AxisType represents returns a POSITION.
	 * <br/>
	 * If false this usually means that whatever this AxisType represents returns a CHANGE IN POSITION
	 * @return true if delta should be applied.
	 */
	public boolean isShouldUseDelta(){
		return shouldUseDelta;
	}

	public boolean equals(boolean full, boolean analog, boolean rangeOver, boolean shouldUseDelta){
		return this.full == full && this.analog == analog && this.rangeOver == rangeOver && this.shouldUseDelta == shouldUseDelta;
	}
	@Override
	public boolean equals(Object o) {
		if(o instanceof AxisType){
			AxisType type = (AxisType) o;
			return type.equals(full, analog, rangeOver, shouldUseDelta);
		}
		return false;
	}

	@Override
	public int hashCode() {
		int r = 17;
		r = 37 * r + (full ? 1 : 0);
		r = 37 * r + (analog ? 1 : 0);
		r = 37 * r + (rangeOver ? 1 : 0);
		r = 37 * r + (shouldUseDelta ? 1 : 0);
		return r;
	}

	@Override
	public String toString() {
		return String.format("AxisType{full:%s,analog:%s,rangeOver:%s,isShouldUseDelta:%s}", full, analog, rangeOver, shouldUseDelta);
	}

	public static AxisType getAxisType(InputPart... parts){
		if(parts.length == 0){
			throw new IllegalArgumentException("The passed parts cannot have a length of 0.");
		}
		boolean anyFull = false;
		boolean anyAnalog = false;
		boolean anyRangeOver = false;
		Boolean shouldUseDelta = null;
		for(InputPart part : parts){
			anyFull = anyFull || part.getAxisType().isFull();
			anyAnalog = anyAnalog || part.getAxisType().isAnalog();
			anyRangeOver = anyRangeOver || part.getAxisType().isRangeOver();
			if(shouldUseDelta == null){
				shouldUseDelta = part.getAxisType().isShouldUseDelta();
			} else {
				if(shouldUseDelta != part.getAxisType().isShouldUseDelta()){
					throw new IllegalArgumentException("Each passed InputPart in parts should have the same value for isShouldUseDelta()");
				}
			}
		}
//			return getAxisType(anyFull, anyAnalog, anyRangeOver, isShouldUseDelta);
//			assert isShouldUseDelta != null; always true
		return new AxisType(anyFull, anyAnalog, anyRangeOver, shouldUseDelta);
	}
}


