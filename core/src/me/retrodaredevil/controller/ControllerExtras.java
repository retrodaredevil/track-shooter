package me.retrodaredevil.controller;

import me.retrodaredevil.controller.output.ControllerRumble;

/**
 * A class for storing extra things related to a controller
 */
public final class ControllerExtras {
//	private final Collection<JoystickPart> joysticks = new ArrayList<>();
	private ControllerRumble rumble = null;
//	private ControllerAccelerometer accelerometer = null;
//	private ControllerTilt tilt = null;
//	private ControllerCompass compass = null;


	/**
	 *
	 */
	public ControllerExtras(){
	}

	/**
	 * NOTE: Even if this doesn't return null, you should still check to make sure the rumble is connected
	 * with isConnected()
	 * @return The ControllerRumble or null if there is no rumble.
	 */
	public ControllerRumble getRumble(){
		return rumble;
	}
	public void setRumble(ControllerRumble rumble){
		this.rumble = rumble;
	}

}
