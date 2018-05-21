package me.retrodaredevil.game.input;

import me.retrodaredevil.input.ControllerInput;
import me.retrodaredevil.input.InputPart;
import me.retrodaredevil.input.JoystickPart;

public abstract class GameInput extends ControllerInput {
	public abstract JoystickPart mainJoystick();
	public abstract JoystickPart rotateJoystick();
	public abstract InputPart fireButton();
	public abstract InputPart slow();
}
