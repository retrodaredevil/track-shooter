package me.retrodaredevil.game.input;

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
import me.retrodaredevil.controller.output.ControllerRumble;
import me.retrodaredevil.controller.types.RumbleCapableController;
import me.retrodaredevil.controller.types.StandardControllerInput;
import me.retrodaredevil.controller.input.HighestPositionInputPart;

public class DefaultGameInput extends SimpleControllerPart implements GameInput {
	private final JoystickPart mainJoystick;
//	private final JoystickPart rotateJoystick;
	private final InputPart rotateAxis;
	private final InputPart fireButton;
	private final InputPart slow;
	private final InputPart activatePowerup;
	private final InputPart startButton;

	private final ControllerRumble rumble;

	private final Collection<ControlOption> controlOptions;

	private final ControllerPart reliesOn;

	/**
	 *
	 * @param controller The controller to use. This will also be added as a child to this object
	 */
	public DefaultGameInput(StandardControllerInput controller){
		reliesOn = controller; // TODO do we really need reliesOn?

		mainJoystick = controller.getLeftJoy();
//		mainJoystick = controller.getDPad();
		rotateAxis = controller.getRightJoy().getXAxis();
		fireButton = new HighestPositionInputPart(controller.getRightBumper(), controller.getLeftBumper(), controller.getRightTrigger(), controller.getLeftTrigger());
//		fireButton = controller.getLeftBumper();
		slow = controller.getLeftStick();
		activatePowerup = controller.getFaceLeft();
		startButton = controller.getStart();
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

		if(Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope)){
			GdxTiltJoystick joy = new GdxTiltJoystick();
			options.addAll(joy.getControlOptions());
			mainJoystick = joy;
		} else {
			mainJoystick = FourKeyJoystick.newWASDJoystick();
		}
		if (Gdx.input.isPeripheralAvailable(Input.Peripheral.MultitouchScreen)) {
			rotateAxis = new GdxMouseAxis(true, -5.0f, new Rectangle(.5f, 0, .5f, 1));
			fireButton = new GdxScreenTouchButton(new Rectangle(0, 0, .5f, 1));
//			startButton = new GdxScreenTouchButton(new Rectangle(0, 0, 1, 1));
		} else {
			rotateAxis = new GdxMouseAxis(false, 1.0f);
			fireButton = new HighestPositionInputPart(new KeyInputPart(Input.Keys.SPACE), new KeyInputPart(Input.Buttons.LEFT, true));
		}
		startButton = new KeyInputPart(Input.Keys.ENTER);
		slow = new KeyInputPart(Input.Keys.SHIFT_LEFT);
		if (Gdx.input.isPeripheralAvailable(Input.Peripheral.HardwareKeyboard) || !Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {
			activatePowerup = new KeyInputPart(Input.Keys.F);
		} else {
			GdxShakeButton button = new GdxShakeButton();
			options.add(new ControlOption("Powerup Activate Shake Sensitivity",
					"How much you have to shake the device to activate the powerup in m/s^2",
					"controls.all.shake", button));
			activatePowerup = button;
		}
		if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Vibrator)) {
			GdxRumble gdxRumble = new GdxRumble();
			options.addAll(gdxRumble.getControlOptions());
			rumble = gdxRumble;
		} else {
			rumble = null;
		}
		controlOptions = options;

		addChildren(Arrays.asList(mainJoystick, rotateAxis, fireButton, slow, activatePowerup, startButton),
				false, false);
		if(rumble != null){
			addChildren(Collections.singletonList(rumble), false, false);
		}
	}

	@Override
	public Collection<? extends ControlOption> getControlOptions() {
		return controlOptions;
	}

	@Override
	public JoystickPart mainJoystick(){
		return mainJoystick;
	}

	@Override
	public InputPart rotateAxis() {
		return rotateAxis;
	}

	@Override
	public InputPart fireButton() {
		return fireButton;
	}

	@Override
	public InputPart slow() {
		return slow;
	}

	@Override
	public InputPart activatePowerup() {
		return activatePowerup;
	}

	@Override
	public InputPart startButton() {
		return startButton;
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
