package me.retrodaredevil.controller;

/**
 * Represents part of a controller such as a button or joystick
 */
public abstract class ControllerPart {

	protected ControlConfig config;
	private ControllerPart parent = null;

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
	 *                                 it will throw an IllegalStateException if a part already has a parent. If false,
	 *                                 changeParent's value will do nothing.
	 */
	public void setParentsToThis(Iterable<? extends ControllerPart> parts, boolean changeParent, boolean canAlreadyHaveParents){
		for(ControllerPart part : parts){
			boolean hasParent = part.getParent() != null;
			if(!canAlreadyHaveParents && hasParent){
				throw new IllegalStateException("A part already has a parent");
			}
			if(changeParent || !hasParent) {
				part.setParent(this);
			}
		}
	}

	public void update(ControlConfig config){
		this.config = config;
	}

	/**
	 * Called right after update()
	 * If you want to utilize something like getPosition() or getX() or something calculated in update, it is
	 * recommended you override this. Also remember to call super
	 */
	public void lateUpdate(){
	}


	/**
	 * If this part somehow represents a button or axis (like InputPart), then this should return true if the button
	 * exists on the controller, false otherwise.
	 *
	 * @param manager The ControllerManager that handles controllers
	 * @return true if this ControllerPart will give accurate values and if it is connected.
	 */
	public abstract boolean isConnected(ControllerManager manager);
}
