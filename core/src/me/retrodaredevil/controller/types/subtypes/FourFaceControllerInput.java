package me.retrodaredevil.controller.types.subtypes;

import me.retrodaredevil.controller.input.InputPart;

/**
 * Represents a controller with four face buttons
 * <p>
 * It is recommended that you use the getFace*() methods instead of the get*Button() methods
 * because getFace methods should have the same physical location across different controllers.
 */
public interface FourFaceControllerInput extends SelectingControllerInput {
	/**
	 * Square button on Playstation Controllers
	 * @return The X button on the controller. This may not always be in the same physical location
	 */
	InputPart getXButton();
	/**
	 * Triangle button on Playstation Controllers
	 * @return The Y button on the controller. This may not always be in the same physical location
	 */
	InputPart getYButton();

	/** @return The face button at the top out of the four face buttons. Should always have the physical location as the upper face button */
	InputPart getFaceUp();
	/** @return The face button on the right out of the four face buttons. Should always have the physical location as the right face button */
	InputPart getFaceRight();
	/** @return The face button at the bottom out of the four face buttons. Should always have the physical location as the lower face button */
	InputPart getFaceDown();
	/** @return The face button on the left out of the four face buttons. Should always have the physical location as the left face button */
	InputPart getFaceLeft();
}
