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
//	SimpleJoystickPart rotateJoystick();
	/** @return The rotate InputPart where a positive position turns clockwise and negative turns counter clockwise*/
	InputPart getRotateAxis();
	JoystickPart getRotationPointInput();
	InputPart getFireButton();
	InputPart getSlowButton(); // maybe remove this entirely and refactor it into main joystick
	InputPart getActivatePowerup();

	InputPart getStartButton();
	InputPart getPauseButton();
	InputPart getBackButton();

	JoystickPart getSelectorJoystick();
	InputPart getEnterButton();

	InputPart getRumbleOnSingleShot();
}
