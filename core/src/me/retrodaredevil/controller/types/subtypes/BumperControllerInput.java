package me.retrodaredevil.controller.types.subtypes;

import me.retrodaredevil.controller.ControllerInput;
import me.retrodaredevil.controller.input.InputPart;

/**
 * Represents a controller with two bumpers
 */
public interface BumperControllerInput extends ControllerInput {
	InputPart getLeftBumper();
	InputPart getRightBumper();
}
