package me.retrodaredevil.game.trackshooter.input;

import java.util.Collection;

import me.retrodaredevil.controller.ControllerPart;
import me.retrodaredevil.controller.SimpleControllerPart;
import me.retrodaredevil.controller.input.HighestPositionInputPart;
import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.input.References;
import me.retrodaredevil.controller.input.SensitiveInputPart;
import me.retrodaredevil.controller.options.ConfigurableControllerPart;
import me.retrodaredevil.controller.options.ConfigurableObject;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionTracker;
import me.retrodaredevil.controller.options.OptionValues;
import me.retrodaredevil.controller.output.ControllerRumble;
import me.retrodaredevil.controller.output.DisconnectedRumble;
import me.retrodaredevil.controller.types.RumbleCapableController;
import me.retrodaredevil.controller.types.StandardControllerInput;

public class ControllerGameInput extends SimpleUsableGameInput {
	private final ControllerPart reliesOn;

	private final JoystickPart mainJoystick;
//	private final JoystickPart rotateJoystick;
	private final InputPart rotateAxis;
	private final InputPart fireButton;
	private final InputPart slow;
	private final InputPart activatePowerup;
	private final InputPart startButton;
	private final InputPart pauseButton;
	private final InputPart backButton;
	private final InputPart enterButton;

	private final ControllerRumble rumble;

	private final OptionTracker controlOptions = new OptionTracker();

	/**
	 * If the passed controller is an instanceof {@link ConfigurableControllerPart}, its ControlOptions
	 * will be added and used
	 * @param controller The controller to use. This will also be added as a child to this object. When passed, it CANNOT have a parent
	 */
	public ControllerGameInput(final StandardControllerInput controller){
		addChildren(false, false, controller);
		reliesOn = controller;

		mainJoystick = References.create(controller::getLeftJoy);
//		getMainJoystick = controller.getDPad();
		ControlOption rotateAxisSensitivity = new ControlOption("Rotation Sensitivity", "Adjust the sensitivity when rotating",
				"controls.all", OptionValues.createAnalogRangedOptionValue(.4, 2.5, 1));
		rotateAxis = new SensitiveInputPart(
				References.create(() -> controller.getRightJoy().getXAxis()),
				rotateAxisSensitivity.getOptionValue(),null);
		fireButton = new HighestPositionInputPart(
				References.create(controller::getRightBumper),
				References.create(controller::getLeftBumper),
				References.create(controller::getRightTrigger),
				References.create(controller::getLeftTrigger)
		);
		slow = References.create(controller::getLeftStick);
		activatePowerup = References.create(controller::getFaceLeft);
		startButton = References.create(controller::getStart);
		pauseButton = startButton; // pause button same as start. Add as children if this changes in the future
		backButton = References.create(controller::getBButton);
		enterButton = References.create(controller::getAButton);
		if(controller instanceof RumbleCapableController) {
			rumble = ((RumbleCapableController) controller).getRumble();
		} else {
			rumble = new DisconnectedRumble();
		}

		addChildren(false, false,
				mainJoystick, rotateAxis, fireButton, slow, activatePowerup,
				startButton, backButton);

		controlOptions.add(rotateAxisSensitivity);
		if(controller instanceof ConfigurableObject){
			controlOptions.add((ConfigurableObject) controller);
		}
	}

	@Override
	public String getRadioOptionName() {
		return "Controller Control Option";
	}

	@Override
	public Collection<? extends ControlOption> getControlOptions() {
		return controlOptions.getOptions();
	}

	@Override
	public JoystickPart getMainJoystick(){
		return mainJoystick;
	}

	@Override
	public InputPart getRotateAxis() {
		return rotateAxis;
	}

	@Override
	public InputPart getFireButton() {
		return fireButton;
	}

	@Override
	public InputPart getSlowButton() {
		return slow;
	}

	@Override
	public InputPart getActivatePowerup() {
		return activatePowerup;
	}

	@Override
	public InputPart getStartButton() {
		return startButton;
	}

	@Override
	public InputPart getPauseButton() {
		return pauseButton;
	}

	@Override
	public InputPart getBackButton() {
		return backButton;
	}

	@Override
	public JoystickPart getSelectorJoystick() {
		return mainJoystick;
	}

	@Override
	public InputPart getEnterButton() {
		return enterButton;
	}

	@Override
	public boolean isConnected() {
		return areAnyChildrenConnected() && (reliesOn == null || reliesOn.isConnected());
	}

	@Override
	public ControllerRumble getRumble() {
		return rumble;
	}
}
