package me.retrodaredevil.controller.types.subtypes;

import me.retrodaredevil.controller.ControllerInput;
import me.retrodaredevil.controller.input.JoystickPart;

/**
 * Represents a controller with a main d-pad
 */
public interface DPadControllerInput extends ControllerInput {
	JoystickPart getDPad();
}
