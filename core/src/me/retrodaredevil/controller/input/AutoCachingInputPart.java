package me.retrodaredevil.controller.input;

import me.retrodaredevil.controller.ControllerPartNotUpdatedException;

/**
 * A single controller such as a button, joystick axis, pov axis, etc
 */
public abstract class AutoCachingInputPart extends SimpleInputPart{

	private final boolean calculatePositionAfterChildren;

	// unless overridden or changed, these two variables only changed in positionUpdate() (for caching)
	private Double position = null;
	private Double previousPosition = null;

	/**
	 *
	 * @param type
	 * @param calculatePositionAfterChildren if you rely on children to calculate position, set to true, otherwise set to false
	 */
	public AutoCachingInputPart(AxisType type, boolean calculatePositionAfterChildren){
		super(type);
		this.calculatePositionAfterChildren = calculatePositionAfterChildren;
	}
	public AutoCachingInputPart(AxisType type){
		this(type, false);
	}
	@Override
	public boolean isDown() {
		return Math.abs(getPosition()) > this.config.buttonDownDeadzone;
	}

	@Override
	public boolean isPressed() {
		return isDown() && (previousPosition == null || previousPosition <= this.config.buttonDownDeadzone);
	}
	@Override
	public boolean isReleased() {
		return !isDown() && (previousPosition != null && previousPosition > this.config.buttonDownDeadzone);
	}


	@Override
	public double getPosition() {
		if(this.position == null){
			throw new ControllerPartNotUpdatedException("this.position was not initialized. " +
					"update() must not have been called. this: " + this + " this.getParent(): " + this.getParent());
		}
		return this.position;
	}


	@Override
	protected void onSecondUpdate() {
		super.onSecondUpdate();
		if(!calculatePositionAfterChildren) {
			positionUpdate();
		}
	}

	@Override
	protected void onAfterChildrenUpdate() {
		super.onAfterChildrenUpdate();
		if(calculatePositionAfterChildren){
			positionUpdate();
		}
	}

	/**
	 * Calls in update(). This is meant to be overridden if the default behaviour of calling calculatePosition() is not
	 * needed and you would like to handle isPressed() by yourself
	 */
	protected void positionUpdate(){
		if(this.position != null){
			this.previousPosition = this.position;
		}
		try {
			this.position = calculatePosition();
//			System.out.println("Calculated position for: " + this + " is: " + this.position + " children.size: " + this.getChildren().size());
		} catch(NullPointerException ex){
			System.err.println("There was a NPE when calculatePosition() was called. " +
					"If you base this off of your children, make sure you set calculatePositionAfterChildren to true in the constructor.");
			throw ex;
		}
	}

	/**
	 * Overridden by subclasses
	 * <p>
	 * When implementing:
	 * The returned value should comply with whatever getAxisType() returns
	 * @return The raw(est) position of the controller
	 */
	protected abstract double calculatePosition();

}
