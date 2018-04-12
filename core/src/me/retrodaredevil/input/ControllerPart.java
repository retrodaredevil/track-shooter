package me.retrodaredevil.input;

/**
 * Represents part of a controller such as a button or joystick
 */
public abstract class ControllerPart {

	protected ControlConfig config;

	public void update(ControlConfig config){
		this.config = config;
	}
}
