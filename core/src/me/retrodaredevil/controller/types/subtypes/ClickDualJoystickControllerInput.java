package me.retrodaredevil.controller.types.subtypes;

import me.retrodaredevil.controller.input.InputPart;

/**
 * Represents a controller with two joysticks where each are "clickable"
 */
public interface ClickDualJoystickControllerInput extends DualJoystickControllerInput {
	InputPart getLeftStick();
	InputPart getRightStick();
}
