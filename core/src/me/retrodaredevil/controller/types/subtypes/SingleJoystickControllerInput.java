package me.retrodaredevil.controller.types.subtypes;

import me.retrodaredevil.controller.ControllerInput;
import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;

/**
 * Represents a controller that is a single joystick with a trigger
 */
public interface SingleJoystickControllerInput extends ControllerInput {
	JoystickPart getMainJoystick();
	InputPart getTrigger();
}
