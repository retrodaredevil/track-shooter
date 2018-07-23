package me.retrodaredevil.game.input;

import me.retrodaredevil.controller.ControllerInput;
import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.types.RumbleCapableController;

/**
 * GameInput is a ControllerInput because it is allowed to introduce its own InputParts and ControllerExtras
 * that are handled by the ControllerInput class.
 */
public interface GameInput extends ControllerInput, RumbleCapableController {
	/** @return main joystick that controls movement */
	JoystickPart mainJoystick();
//	SimpleJoystickPart rotateJoystick();
	/** @return The rotate InputPart where a positive position turns clockwise and negative turns counter clockwise*/
	InputPart rotateAxis();
	InputPart fireButton();
	InputPart slow();
	InputPart activatePowerup();

	InputPart startButton();
}
