package me.retrodaredevil.game.trackshooter.input;

import me.retrodaredevil.controller.ControllerPart;
import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.input.References;
import me.retrodaredevil.controller.input.implementations.*;
import me.retrodaredevil.controller.options.*;
import me.retrodaredevil.controller.output.ControllerRumble;
import me.retrodaredevil.controller.output.DisconnectedRumble;
import me.retrodaredevil.controller.types.ExtremeFlightJoystickControllerInput;
import me.retrodaredevil.controller.types.LogitechAttack3JoystickControllerInput;
import me.retrodaredevil.controller.types.RumbleCapableController;
import me.retrodaredevil.controller.types.StandardControllerInput;

import java.util.Collection;

public class ControllerGameInput extends SimpleUsableGameInput {
	private final ControllerPart reliesOn;

	private final JoystickPart mainJoystick;
	private final JoystickPart selectorJoystick;

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

	private final InputPart rumbleOnSingleShot;

	private final JoystickPart rotationPointInput;

	{
		rotationPointInput = new TwoAxisJoystickPart(
				new DummyInputPart(0, true),
				new DummyInputPart(0, true)
		);
	}

	/**
	 * If the passed controller is an instanceof {@link ConfigurableControllerPart}, its ControlOptions
	 * will be added and used
	 * @param controller The controller to use. This will also be added as a child to this object. When passed, it CANNOT have a parent
	 */
	public ControllerGameInput(final StandardControllerInput controller, ConfigurableObject extraOptions){
		partUpdater.addPartAssertNotPresent(controller);
		reliesOn = controller;

		controlOptions.add(extraOptions);

		mainJoystick = new MultiplexerJoystickPart(
				References.create(controller::getLeftJoy),
				References.create(controller::getDPad)
		);
		selectorJoystick = new MultiplexerJoystickPart(
				References.create(controller::getLeftJoy),
				References.create(controller::getRightJoy),
				References.create(controller::getDPad)
		);
		ControlOption rotateAxisSensitivity = createRotationalAxisSensitivity();
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
			rumble = DisconnectedRumble.getInstance(); // we don't have to update a disconnected rumble
		}

		partUpdater.addPartsAssertNonePresent(
				mainJoystick, rotateAxis, fireButton, slow, activatePowerup,
				startButton, backButton, enterButton, selectorJoystick);

		controlOptions.add(rotateAxisSensitivity);
		if(controller instanceof ConfigurableObject){
			controlOptions.add((ConfigurableObject) controller);
		}
		rumbleOnSingleShot = GameInputs.createRumbleOnSingleShotInputPart(partUpdater, controlOptions, rumble);
	}
	public ControllerGameInput(final LogitechAttack3JoystickControllerInput controller){
		partUpdater.addPartAssertNotPresent(controller);
		reliesOn = controller;

		mainJoystick = References.create(controller::getMainJoystick);
		selectorJoystick = References.create(controller::getMainJoystick);
		ControlOption rotateAxisSensitivity = createRotationalAxisSensitivity();
		rotateAxis = new TwoWayInput( // x
				References.create(controller::getThumbUpper), // +
				References.create(controller::getThumbLeft)   // -
		);
		fireButton = References.create(controller::getTrigger);
		slow = References.create(controller::getCenterLeft);
		activatePowerup = new HighestPositionInputPart(
				References.create(controller::getLeftUpper),
				References.create(controller::getLeftLower)
		);
		startButton = References.create(controller::getRightUpper);
		pauseButton = startButton;
		backButton = References.create(controller::getRightLower);
		enterButton = References.create(controller::getCenterRight);
		if(controller instanceof RumbleCapableController){
			rumble = ((RumbleCapableController) controller).getRumble();
		} else {
			rumble = DisconnectedRumble.getInstance();
		}
		partUpdater.addPartsAssertNonePresent(mainJoystick, rotateAxis, fireButton, slow, activatePowerup, startButton, backButton, enterButton, selectorJoystick);
		controlOptions.add(rotateAxisSensitivity);
		if(controller instanceof ConfigurableObject){
			controlOptions.add((ConfigurableObject) controller);
		}
		rumbleOnSingleShot = GameInputs.createRumbleOnSingleShotInputPart(partUpdater, controlOptions, rumble);
	}
	public ControllerGameInput(final ExtremeFlightJoystickControllerInput controller){
		partUpdater.addPartAssertNotPresent(controller);
		reliesOn = controller;

		mainJoystick = References.create(controller::getMainJoystick);
		selectorJoystick = new MultiplexerJoystickPart(
				References.create(controller::getMainJoystick),
				References.create(controller::getDPad)
		);
		ControlOption rotateAxisSensitivity = createRotationalAxisSensitivity();
//		rotateAxis = References.create(() -> controller.getDPad().getXAxis());
        rotateAxis = References.create(controller::getTwist);
		fireButton = References.create(controller::getTrigger);
		slow = References.create(controller::getGridLowerLeft);
		activatePowerup = References.create(controller::getThumbButton);
		startButton = References.create(controller::getGridUpperLeft);
		pauseButton = startButton;
		backButton = References.create(controller::getGridUpperRight);
		enterButton = References.create(controller::getThumbLeftLower);
		if(controller instanceof RumbleCapableController){
			rumble = ((RumbleCapableController) controller).getRumble();
		} else {
			rumble = DisconnectedRumble.getInstance();
		}
		partUpdater.addPartsAssertNonePresent(mainJoystick, rotateAxis, fireButton, slow, activatePowerup, startButton, backButton, enterButton, selectorJoystick);
		controlOptions.add(rotateAxisSensitivity);
		if(controller instanceof ConfigurableObject){
			controlOptions.add((ConfigurableObject) controller);
		}

		rumbleOnSingleShot = GameInputs.createRumbleOnSingleShotInputPart(partUpdater, controlOptions, rumble);
	}
	private static ControlOption createRotationalAxisSensitivity(){
		return new ControlOption("Rotation Sensitivity", "Adjust the sensitivity when rotating",
				"controls.rotation.controller.sensitivity", OptionValues.createAnalogRangedOptionValue(.4, 2.5, 1));
	}

	@Override
	public String getRadioOptionName() {
		return "Controller Control Option";
	}

	@Override
	public Collection<? extends ControlOption> getControlOptions() {
		return controlOptions.getControlOptions();
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
	public JoystickPart getRotationPointInput() {
		return rotationPointInput;
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
        return selectorJoystick;
	}

	@Override
	public InputPart getEnterButton() {
		return enterButton;
	}

	@Override
	public InputPart getRumbleOnSingleShot() {
		return rumbleOnSingleShot;
	}

	@Override
	public boolean isConnected() {
		return partUpdater.isAnyPartsConnected() && (reliesOn == null || reliesOn.isConnected());
	}

	@Override
	public ControllerRumble getRumble() {
		return rumble;
	}
}
