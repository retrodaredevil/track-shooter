package me.retrodaredevil.controller.types.subtypes;

import me.retrodaredevil.controller.input.InputPart;

/**
 * Represents a controller with two bumpers and two triggers. The triggers may be analog or digital
 */
public interface BumperTriggerControllerInput extends BumperControllerInput {
	InputPart getLeftTrigger();
	InputPart getRightTrigger();
}
