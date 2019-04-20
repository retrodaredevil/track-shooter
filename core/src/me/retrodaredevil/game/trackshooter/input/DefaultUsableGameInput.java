package me.retrodaredevil.game.trackshooter.input;

import java.util.Collection;

import me.retrodaredevil.controller.ControllerPart;
import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionTracker;
import me.retrodaredevil.controller.output.ControllerRumble;

public class DefaultUsableGameInput extends SimpleUsableGameInput {

	private final String controlName;

	private final JoystickPart mainJoystick;
	private final InputPart rotateAxis;
	private final JoystickPart rotationPointInput;
	private final InputPart fireButton;
	private final InputPart slow;
	private final InputPart activatePowerup;
	private final InputPart startButton;
	private final InputPart pauseButton;
	private final InputPart backButton;

	private final JoystickPart selectorJoystick;
	private final InputPart enterButton;


	private final InputPart rumbleOnSingleShot;

	private final ControllerRumble rumble;

	private final OptionTracker controlOptions;

	private final Collection<ControllerPart> reliesOnCollection;

	/**
	 * Note that none of the passed parameters are added as children. The caller must add the necessary {@link ControllerPart}s
	 * as children in order for this to function properly
	 */
	public DefaultUsableGameInput(String controlName, JoystickPart mainJoystick,
								  InputPart rotateAxis, JoystickPart rotationPointInput,
								  InputPart fireButton, InputPart slow, InputPart activatePowerup,
								  InputPart startButton, InputPart pauseButton, InputPart backButton,
								  JoystickPart selectorJoystick, InputPart enterButton, ControllerRumble rumble,
								  OptionTracker controlOptions, Collection<ControllerPart> reliesOnCollection){
		this.controlName = controlName;
		this.mainJoystick = mainJoystick;
		this.rotateAxis = rotateAxis;
		this.rotationPointInput = rotationPointInput;
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

		rumbleOnSingleShot = GameInputs.createRumbleOnSingleShotInputPart(this, controlOptions, rumble);
	}


	@Override
	public String getRadioOptionName() {
		return controlName;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "{" + controlName + "," + hashCode() + "}";
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
