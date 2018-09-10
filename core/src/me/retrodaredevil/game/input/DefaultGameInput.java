package me.retrodaredevil.game.input;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import me.retrodaredevil.controller.ControllerPart;
import me.retrodaredevil.controller.SimpleControllerPart;
import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionValue;
import me.retrodaredevil.controller.options.OptionValues;
import me.retrodaredevil.controller.output.ControllerRumble;
import me.retrodaredevil.controller.types.RumbleCapableController;
import me.retrodaredevil.controller.types.StandardControllerInput;
import me.retrodaredevil.controller.input.HighestPositionInputPart;

public class DefaultGameInput extends SimpleControllerPart implements UsableGameInput {
	private final JoystickPart mainJoystick;
//	private final JoystickPart rotateJoystick;
	private final InputPart rotateAxis;
	private final InputPart fireButton;
	private final InputPart slow;
	private final InputPart activatePowerup;
	private final InputPart startButton;
	private final InputPart pauseButton;
	private final InputPart backButton;

	private final ControllerRumble rumble;

	private final Collection<? extends ControlOption> controlOptions;

	private final ControllerPart reliesOn;

	/**
	 *
	 * @param controller The controller to use. This will also be added as a child to this object
	 */
	public DefaultGameInput(StandardControllerInput controller){
		reliesOn = controller; // TODO do we really need reliesOn?

		mainJoystick = controller.getLeftJoy();
//		getMainJoystick = controller.getDPad();
		rotateAxis = controller.getRightJoy().getXAxis();
		fireButton = new HighestPositionInputPart(controller.getRightBumper(), controller.getLeftBumper(), controller.getRightTrigger(), controller.getLeftTrigger());
//		getFireButton = controller.getLeftBumper();
		slow = controller.getLeftStick();
		activatePowerup = controller.getFaceLeft();
		startButton = controller.getStart();
		pauseButton = controller.getStart();
		backButton = controller.getBButton();
		if(controller instanceof RumbleCapableController) {
			rumble = ((RumbleCapableController) controller).getRumble();
		} else {
			rumble = null;
		}

		addChildren(Arrays.asList(fireButton, controller), false, false);

		controlOptions = Collections.emptyList();
	}
	public DefaultGameInput(){
		reliesOn = null;
		final List<ControlOption> options = new ArrayList<>();
		controlOptions = options;

		if(Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope)){
			GdxTiltJoystick joy = new GdxTiltJoystick();
			options.addAll(joy.getControlOptions());
			mainJoystick = joy;
		} else {
			mainJoystick = FourKeyJoystick.newWASDJoystick();
		}
		final OptionValue mouseMultiplier = OptionValues.createAnalogRangedOptionValue(.5, 2, 1);
		final OptionValue mouseInvert = OptionValues.createBooleanOptionValue(false);
		options.add(new ControlOption("Rotation Sensitivity", "How sensitive should rotation be",
				"controls.all.mouse", mouseMultiplier));
		options.add(new ControlOption("Invert Rotation", "Should the rotation be inverted",
				"controls.all.mouse", mouseInvert));
		if (Gdx.input.isPeripheralAvailable(Input.Peripheral.MultitouchScreen)) {
			rotateAxis = new GdxMouseAxis(true, -5.0f, mouseMultiplier, mouseInvert, new Rectangle(.5f, 0, .5f, 1));
			fireButton = new GdxScreenTouchButton(new Rectangle(0, 0, .5f, 1));
		} else {
			rotateAxis = new GdxMouseAxis(false, 1.0f, mouseMultiplier, mouseInvert);
			fireButton = new HighestPositionInputPart(new KeyInputPart(Input.Keys.SPACE), new KeyInputPart(Input.Buttons.LEFT, true));
		}
		startButton = new KeyInputPart(Input.Keys.ENTER);
		slow = new KeyInputPart(Input.Keys.SHIFT_LEFT);
		if (Gdx.input.isPeripheralAvailable(Input.Peripheral.HardwareKeyboard) || !Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
			activatePowerup = new KeyInputPart(Input.Keys.F);
		} else {
			OptionValue shakeThresholdValue = OptionValues.createDigitalRangedOptionValue(3, 16, 8);
			GdxShakeButton button = new GdxShakeButton(shakeThresholdValue);
			options.add(new ControlOption("Powerup Activate Shake Sensitivity",
					"How much you have to shake the device to activate the powerup in m/s^2",
					"controls.all.shake", shakeThresholdValue));
			activatePowerup = button;
		}
		if(Gdx.app.getType() == Application.ApplicationType.Android){
			Gdx.input.setCatchBackKey(true);
			pauseButton = new KeyInputPart(Input.Keys.BACK);
			backButton = pauseButton;
		} else {
			pauseButton = new HighestPositionInputPart(new KeyInputPart(Input.Keys.ESCAPE),
					new KeyInputPart(Input.Keys.ENTER));
			backButton = new KeyInputPart(Input.Keys.ESCAPE);
		}
		if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Vibrator)) {
			GdxRumble gdxRumble = new GdxRumble();
			options.addAll(gdxRumble.getControlOptions());
			rumble = gdxRumble;
		} else {
			rumble = null;
		}

		addChildren(Arrays.asList(mainJoystick, rotateAxis, fireButton, slow, activatePowerup, startButton),
				false, false);
		if(pauseButton != backButton){
			addChildren(Arrays.asList(pauseButton, backButton), false, false);
		} else {
			addChildren(Collections.singletonList(pauseButton), false, false);
		}
		if(rumble != null){
			addChildren(Collections.singletonList(rumble), false, false);
		}
	}

	@Override
	public String getRadioOptionName() {
		return "Temporary Radio Option Name";
	}

	@Override
	public Collection<? extends ControlOption> getControlOptions() {
		return controlOptions;
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
	public boolean isConnected() {
		return areAnyChildrenConnected() && (reliesOn == null || reliesOn.isConnected());
	}

	@Override
	public ControllerRumble getRumble() {
		return rumble;
	}
}
