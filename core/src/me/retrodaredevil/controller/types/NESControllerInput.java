package me.retrodaredevil.controller.types;

import me.retrodaredevil.controller.types.subtypes.DPadControllerInput;
import me.retrodaredevil.controller.types.subtypes.SelectingControllerInput;
import me.retrodaredevil.controller.types.subtypes.StartSelectControllerInput;

/**
 * Represents a simple NES controller
 */
public interface NESControllerInput extends SelectingControllerInput, DPadControllerInput, StartSelectControllerInput {
}
