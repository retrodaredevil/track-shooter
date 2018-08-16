package me.retrodaredevil.controller.types.subtypes;

import me.retrodaredevil.controller.ControllerInput;
import me.retrodaredevil.controller.input.JoystickPart;

public interface DPadControllerInput extends ControllerInput {
	JoystickPart getDPad();
}
