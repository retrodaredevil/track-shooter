package me.retrodaredevil.input;

/**
 * Represents part of a controller such as a button or joystick
 */
public abstract class ControllerPart {

	protected ControlConfig config;
	private ControllerPart parent = null;

	/**
	 * @return The parent ControllerPart that calls this ControllPart's update or null if there is none
	 */
	ControllerPart getParent(){
		return parent;
	}

	/**
	 * Normally, you shouldn't call this with a ControllerInput as parent because ControllerInput already handles this
	 * automatically
	 * @param parent The parent to set
	 */
	void setParent(ControllerPart parent){
		this.parent = parent;
	}

	public void update(ControlConfig config){
		this.config = config;
	}

	/**
	 * Called right after update()
	 */
	public void lateUpdate(){
	}

	/**
	 * If this part somehow represents a button or axis (like SingleInput), then this should return true if the button
	 * exists on the controller, false otherwise.
	 *
	 * @param manager The ControllerManager that handles controllers
	 * @return true if this ControllerPart will give accurate values and if it is connected.
	 */
	public abstract boolean isConnected(ControllerManager manager);
}
