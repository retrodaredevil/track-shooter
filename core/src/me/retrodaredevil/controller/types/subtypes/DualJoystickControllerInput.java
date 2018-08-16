package me.retrodaredevil.controller.types.subtypes;

import me.retrodaredevil.controller.ControllerInput;
import me.retrodaredevil.controller.input.JoystickPart;

/**
 * Represents a controller with two joysticks
 */
public interface DualJoystickControllerInput extends ControllerInput {
	JoystickPart getLeftJoy();
	JoystickPart getRightJoy();
}
