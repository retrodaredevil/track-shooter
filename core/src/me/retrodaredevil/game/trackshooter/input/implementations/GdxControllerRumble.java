package me.retrodaredevil.game.trackshooter.input.implementations;

import com.badlogic.gdx.controllers.Controller;

import me.retrodaredevil.controller.SimpleControllerPart;
import me.retrodaredevil.controller.output.ControllerRumble;
import me.retrodaredevil.controller.output.DisconnectedRumble;

/**
 * Currently, rumble is not supported by libgdx but in the future they may be so
 * even though this class doesn't work right now, you can use it as a place holder for a gyro
 * for controllers.
 */
public class GdxControllerRumble extends DisconnectedRumble {

//	private final Controller controller;

	public GdxControllerRumble(Controller controller){
//		this.controller = controller;
	}

}
