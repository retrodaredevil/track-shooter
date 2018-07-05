package me.retrodaredevil.controller;

/**
 * Represents part of a controller such as a button or joystick
 */
public abstract class ControllerPart {

	private boolean completelyRemoved = false;

	protected ControlConfig config;
	private ControllerPart parent = null;


	public void completelyRemove(){
		completelyRemoved = true;
	}
	public boolean isCompletelyRemoved(){
		return completelyRemoved;
	}
	/**
	 * If null, that means that the only thing updating this should be a ControllerInput
	 * @return The parent ControllerPart that calls this ControllPart's update or null if there is none
	 */
	public ControllerPart getParent(){
		return parent;
	}

	/**
	 * Normally, you shouldn't call this with a ControllerInput as parent because ControllerInput already handles this
	 * automatically
	 * @param parent The parent to set
	 */
	public void setParent(ControllerPart parent){
		this.parent = parent;
	}


	/**
	 *
	 * @param parts The parts to change each element's parent to this
	 * @param changeParent true if you want to allow the parent of parts that already have a parent to be changed
	 * @param canAlreadyHaveParents Should be true if you expect one or more elements to already have a parent. If false,
	 *                                 it will throw an AssertionError if a part already has a parent. If false,
	 *                                 changeParent's value will do nothing.
	 * @throws AssertionError if canAlreadyHaveParents == false and one of the parts in the
	 *                        parts Iterable has a parent that isn't null, this will be thrown
	 * @throws IllegalArgumentException only thrown when changeParent == true and canAlreadyHaveParents == false
	 */
	public void setParentsToThis(Iterable<? extends ControllerPart> parts,
	                             boolean changeParent, boolean canAlreadyHaveParents){
		if(changeParent && !canAlreadyHaveParents){
			throw new IllegalArgumentException("If changeParent == true, canAlreadyHaveParents cannot be false");
		}
		for(ControllerPart part : parts){
			boolean hasParent = part.getParent() != null;
			if(!canAlreadyHaveParents && hasParent){
				throw new AssertionError("A part already has a parent");
			}
			if(changeParent || !hasParent) {
				part.setParent(this);
			}
		}
	}

	/**
	 * After this method is called, whether it is for a superclass or subclass, calls to getXXXX should
	 * be the most current and accurate value.
	 * <br/>
	 * If a superclass has defined abstract methods such as getPosition() or getX(), when the superclass's update is called,
	 * calling those methods must be up to date. This is one of the reasons that you might need to call
	 * super after performing important steps that affect the return value of the methods that the call to super's update might use.
	 */
	public void update(ControlConfig config){
		checkCompletelyRemoved();
		this.config = config;
	}
	protected void checkCompletelyRemoved(){
		if(isCompletelyRemoved()){
			throw new IllegalStateException("Cannot update because we are completely removed. this: " + this);
		}
		if(parent != null && parent.isCompletelyRemoved()){
			throw new IllegalStateException("Cannot update because our parent is completely removed. parent: " + parent);
		}
	}

	/**
	 * Called right after update()
	 * If you want to utilize something like getPosition() or getX() or something calculated in update, it is
	 * recommended you override this. Also remember to call super
	 * <br/><br/>
	 * NOTE: This is called right after update and should not be used to get the values of other ControllerParts because
	 * they may not have been called yet this frame.
	 */
	public void lateUpdate(){
	}


	/**
	 * If this part somehow represents a button or axis (like InputPart), then this should return true if the button
	 * exists on the controller, false otherwise.
	 *
	 * @return true if this ControllerPart will give accurate values and if it is connected.
	 */
	public abstract boolean isConnected();
}
