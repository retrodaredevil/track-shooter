package me.retrodaredevil.game.input;

import me.retrodaredevil.controller.ControllerInput;
import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;

/**
 * GameInput is a ControllerInput because it is allowed to introduce its own InputParts and ControllerExtras
 * that are handled by the ControllerInput class.
 */
public abstract class GameInput extends ControllerInput {
	public abstract JoystickPart mainJoystick();
	public abstract JoystickPart rotateJoystick();
	public abstract InputPart fireButton();
	public abstract InputPart slow();
	public abstract InputPart activatePowerup();
}
