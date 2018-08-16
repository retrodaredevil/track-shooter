package me.retrodaredevil.controller.types;

import me.retrodaredevil.controller.types.subtypes.BumperControllerInput;
import me.retrodaredevil.controller.types.subtypes.FourFaceControllerInput;

/**
 * Represents a SNES controller
 */
public interface SNESControllerInput extends NESControllerInput, FourFaceControllerInput, BumperControllerInput {
}
