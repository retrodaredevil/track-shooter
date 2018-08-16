package me.retrodaredevil.controller.types;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.types.subtypes.BumperTriggerControllerInput;
import me.retrodaredevil.controller.types.subtypes.ClickDualJoystickControllerInput;
import me.retrodaredevil.controller.types.subtypes.DPadControllerInput;
import me.retrodaredevil.controller.types.subtypes.FourFaceControllerInput;

/**
 * This class can be used to store and access the standard layout on most modern controllers. Note that when
 * implementing, you can return null if the controller doesn't support something.
 * <p>
 * None of the values returned defined in this interface should be null, however some may not be
 * "connected"
 */
public interface StandardControllerInput extends FourFaceControllerInput, ClickDualJoystickControllerInput, DPadControllerInput, BumperTriggerControllerInput {

	InputPart getStart();
	InputPart getSelect();

}
