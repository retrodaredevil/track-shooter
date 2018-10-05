package me.retrodaredevil.game.input;

import java.util.Collection;

import me.retrodaredevil.controller.ControllerPart;
import me.retrodaredevil.controller.SimpleControllerInput;
import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionTracker;
import me.retrodaredevil.controller.output.ControllerRumble;

public class DefaultUsableGameInput extends SimpleUsableGameInput {

	private final String controlName;

	private final JoystickPart mainJoystick;
	//	private final JoystickPart rotateJoystick;
	private final InputPart rotateAxis;
	private final InputPart fireButton;
	private final InputPart slow;
	private final InputPart activatePowerup;
	private final InputPart startButton;
	private final InputPart pauseButton;
	private final InputPart backButton;

	private final JoystickPart selectorJoystick;
	private final InputPart enterButton;

	private final ControllerRumble rumble;

//	private final Collection<? extends ControlOption> controlOptions;
	private final OptionTracker controlOptions;

	private final Collection<ControllerPart> reliesOnCollection;

	public DefaultUsableGameInput(String controlName, JoystickPart mainJoystick, InputPart rotateAxis,
								  InputPart fireButton, InputPart slow, InputPart activatePowerup, InputPart startButton,
								  InputPart pauseButton, InputPart backButton, JoystickPart selectorJoystick, InputPart enterButton,
								  ControllerRumble rumble, OptionTracker controlOptions,
								  Collection<ControllerPart> reliesOnCollection){
		this.controlName = controlName;
		this.mainJoystick = mainJoystick;
		this.rotateAxis = rotateAxis;
		this.fireButton = fireButton;
		this.slow = slow;
		this.activatePowerup = activatePowerup;
		this.startButton = startButton;
		this.pauseButton = pauseButton;
		this.backButton = backButton;

		this.selectorJoystick = selectorJoystick;
		this.enterButton = enterButton;

		this.rumble = rumble;
		this.controlOptions = controlOptions;
		this.reliesOnCollection = reliesOnCollection;

//		List<ControllerPart> partsList = Arrays.asList(
//				mainJoystick, rotateAxis, fireButton, slow, activatePowerup,
//				startButton, pauseButton, backButton, rumble
//		);
//		addChildren(partsList, true, true);
	}


	@Override
	public String getRadioOptionName() {
		return controlName;
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
		return selectorJoystick;
	}

	@Override
	public InputPart getEnterButton() {
		return enterButton;
	}

	@Override
	public ControllerRumble getRumble() {
		return rumble;
	}

	@Override
	public boolean isConnected() {
		for(ControllerPart part : reliesOnCollection){
			if(!part.isConnected()){
				return false;
			}
		}

		return areAnyChildrenConnected();
	}

}
