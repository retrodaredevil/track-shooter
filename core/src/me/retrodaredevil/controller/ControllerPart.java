package me.retrodaredevil.controller;

import java.util.Collection;

/**
 * Represents part of a controller such as a button or joystick
 */
public interface ControllerPart {
	/**
	 * If null: That means that this is being updated by something like a ControllerManager or that this
	 * ControllerPart is not being updated at all.
	 * @return The parent ControllerPart that calls this ControllerPart's update or null if there is none
	 */
	ControllerPart getParent();

	/**
	 * Normally, you shouldn't call this with a ControllerInput as parent because ControllerInput already handles this
	 * automatically
	 * @param parent The parent to set
	 * @throws IllegalArgumentException thrown if parent == this
	 */
	void setParent(ControllerPart parent);

	/**
	 * NOTE: The returned value should not be mutated
	 * <br/>
	 * NOTE: You can always assume that parent.getChildren().contains(part) == (part.getParent() == parent)
	 * @return A Collection representing all the children
	 */
	Collection<ControllerPart> getChildren();

	/**
	 * When this method is called it is possible that part.getParent() == this but this.getChildren().contains(part) == false
	 * so this method should check to make sure getChildren() will contain part and should also call part.setParent(this)
	 * @param part The part to make a child of this
	 */
	void addChild(ControllerPart part);

	/**
	 * Calling this method should set part's parent to null only if part.getParent() == this. Otherwise it's
	 * parent should not be changed.
	 * <br/>
	 * Calling this method will also (obviously) remove part as a child so part will not be contained in getChildren()
	 * @param part The part to remove a child
	 * @return true if the part was removed
	 */
	boolean removeChild(ControllerPart part);


	/**
	 * After this method is called, whether it is for a superclass or subclass, calls to getXXXX should
	 * be the most current and accurate value.
	 * <br/>
	 * If a superclass has defined abstract methods such as getPosition() or getX(), when the superclass's update is called,
	 * calling those methods must be up to date. This is one of the reasons that you might need to call
	 * super after performing important steps that affect the return value of the methods that the call to super's update might use.
	 */
	void update(ControlConfig config);



	/**
	 * If this part somehow represents a button or axis (like AutoCachingInputPart), then this should return true if the button
	 * exists on the controller, false otherwise.
	 *
	 * @return true if this ControllerPart will give accurate values and if it is connected.
	 */
	boolean isConnected();
}
