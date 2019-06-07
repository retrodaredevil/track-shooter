package me.retrodaredevil.game.trackshooter.input;

import me.retrodaredevil.controller.ControllerInput;
import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.options.ConfigurableControllerPart;
import me.retrodaredevil.controller.types.RumbleCapableController;

/**
 * GameInput is a ControllerInput because it is allowed to introduce its own InputParts and ControllerExtras
 * that are handled by the ControllerInput class.
 */
public interface GameInput extends ControllerInput, RumbleCapableController, ConfigurableControllerPart {
	/** @return main joystick that controls movement */
	JoystickPart getMainJoystick();
	/** @return The rotate InputPart where a positive position turns clockwise and negative turns counter clockwise*/
	InputPart getRotateAxis();
	/** @return a joystick that represents the point where the player is pressing on screen. If connected, should be used over {@link #getRotateAxis()}*/
	JoystickPart getRotationPointInput();
	InputPart getFireButton();
	InputPart getSlowButton(); // TODO maybe remove this entirely and refactor it into main joystick
	InputPart getActivatePowerup();

	InputPart getStartButton();
	InputPart getPauseButton();
	InputPart getBackButton();

	JoystickPart getSelectorJoystick();

	/** @return A button to select the current selection */
	InputPart getEnterButton();

	/** @return A simple {@link InputPart} that if {@link InputPart#isDown()}, then rumble should be activated on single and triple shots. */
	InputPart getRumbleOnSingleShot();
}
