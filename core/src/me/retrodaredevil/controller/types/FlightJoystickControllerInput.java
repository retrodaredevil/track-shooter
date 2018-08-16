package me.retrodaredevil.controller.types;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.types.subtypes.DPadControllerInput;
import me.retrodaredevil.controller.types.subtypes.SingleJoystickControllerInput;
import me.retrodaredevil.controller.types.subtypes.SliderControllerInput;

/**
 * Represents a Flight Joystick that is guaranteed to have a joystick, twist axis, trigger (usually digital), thumb button, slider,
 * and 4 buttons around the POV.
 */
public interface FlightJoystickControllerInput extends SingleJoystickControllerInput, DPadControllerInput, SliderControllerInput {
	InputPart getTwist();

	InputPart getThumbButton();

	InputPart getThumbLeftUpper();
	InputPart getThumbLeftLower();
	InputPart getThumbRightUpper();
	InputPart getThumbRightLower();
}
