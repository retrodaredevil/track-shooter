package me.retrodaredevil.game.input;

import me.retrodaredevil.controller.ControllerInput;
import me.retrodaredevil.controller.InputPart;
import me.retrodaredevil.controller.JoystickPart;

public abstract class GameInput extends ControllerInput {
	public abstract JoystickPart mainJoystick();
	public abstract JoystickPart rotateJoystick();
	public abstract InputPart fireButton();
	public abstract InputPart slow();
	public abstract InputPart activatePowerup();
}
