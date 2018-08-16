package me.retrodaredevil.controller.types;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.types.subtypes.DPadControllerInput;
import me.retrodaredevil.controller.types.subtypes.SingleJoystickControllerInput;

public interface FlightJoystickControllerInput extends SingleJoystickControllerInput, DPadControllerInput {
	InputPart getSlider();

	InputPart getThumbButton();
}
