package me.retrodaredevil.controller.types.subtypes;

import me.retrodaredevil.controller.ControllerInput;
import me.retrodaredevil.controller.input.InputPart;

/**
 * Represents a controller used for selecting items using A and B
 */
public interface SelectingControllerInput extends ControllerInput {
	/**
	 * X Button on playstation controllers
	 * @return The A button on the controller. This may not always be in the same physical location
	 */
	InputPart getAButton();

	/**
	 * Circle button on playstation controllers
	 * @return The B button on the controller. This may not always be in the same physical location
	 */
	InputPart getBButton();
}
