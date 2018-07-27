package me.retrodaredevil.controller.input;

/**
 * Represents information used with an InputPart.
 * <p>
 * There are some general constant AxisTypes such as FULL_ANALOG and these should never be compared with ==
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

	public AxisType(boolean full, boolean analog){
		this(full, analog, false, true);
	}

	/**
	 * @param full Can the values be negative
	 * @param analog Can the values be in between. Ex: .5
	 * @param rangeOver Can the abs of the value be > 1
	 * @param shouldUseDelta Should delta time be applied. (Not used with a mouse, or another device that gives "moved with much since last call" feedback)
	 */
	public AxisType(boolean full, boolean analog, boolean rangeOver, boolean shouldUseDelta){
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
		return String.format("AxisType{full:%s,analog:%s,rangeOver:%s,shouldUseDelta:%s}", full, analog, rangeOver, shouldUseDelta);
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
				shouldUseDelta = part.getAxisType().shouldUseDelta();
			} else {
				if(shouldUseDelta != part.getAxisType().shouldUseDelta()){
					throw new IllegalArgumentException("Each passed InputPart in parts should have the same value for shouldUseDelta()");
				}
			}
		}
//			return getAxisType(anyFull, anyAnalog, anyRangeOver, shouldUseDelta);
//			assert shouldUseDelta != null; always true
		return new AxisType(anyFull, anyAnalog, anyRangeOver, shouldUseDelta);
	}
}


