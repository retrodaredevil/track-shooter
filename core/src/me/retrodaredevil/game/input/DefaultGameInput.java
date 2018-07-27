package me.retrodaredevil.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import me.retrodaredevil.controller.ControllerPart;
import me.retrodaredevil.controller.SimpleControllerPart;
import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.input.SimpleJoystickPart;
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

	private ControllerPart reliesOn = null;

	public DefaultGameInput(StandardControllerInput controller){
		mainJoystick = controller.leftJoy();
//		mainJoystick = controller.dPad();
		rotateAxis = controller.rightJoy().getXAxis();
		fireButton = new HighestPositionInputPart(controller.rightBumper(), controller.leftBumper(), controller.rightTrigger(), controller.leftTrigger());
//		fireButton = controller.leftBumper();
		slow = controller.leftStick();
		activatePowerup = controller.faceLeft();
		startButton = controller.start();
		if(controller instanceof RumbleCapableController) {
			rumble = ((RumbleCapableController) controller).getRumble();
		} else {
			rumble = null;
		}

		addChildren(Collections.singletonList(fireButton), false, false);

		reliesOn = controller;
	}
	public DefaultGameInput(){
		if(Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope)){
			mainJoystick = new GdxTiltJoystick(15);
		} else {
			mainJoystick = FourKeyJoystick.newWASDJoystick();
		}
//		rotateJoystick = FourKeyJoystick.newArrowKeyJoystick();
//		final JoystickPart rotateJoystick;
		if (Gdx.input.isPeripheralAvailable(Input.Peripheral.MultitouchScreen)) {
//			rotateJoystick = new GdxDragJoystick(, 5.0f);
			rotateAxis = new GdxMouseAxis(true, -5.0f, new Rectangle(.5f, 0, .5f, 1));
			fireButton = new GdxScreenTouchButton(new Rectangle(0, 0, .5f, 1));
			startButton = new GdxScreenTouchButton(new Rectangle(0, 0, 1, 1));
		} else {
//			rotateJoystick = new GdxMouseJoystick();
			rotateAxis = new GdxMouseAxis(false, 1.0f);
			fireButton = new HighestPositionInputPart(new KeyInputPart(Input.Keys.SPACE), new KeyInputPart(Input.Buttons.LEFT, true));
			startButton = new KeyInputPart(Input.Keys.ENTER);
		}
		slow = new KeyInputPart(Input.Keys.SHIFT_LEFT);
		activatePowerup = new KeyInputPart(Input.Keys.F);
//		if(Gdx.input.isPeripheralAvailable(Input.Peripheral.HardwareKeyboard)) {
//		} else {
//		}
		if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Vibrator)) {
			rumble = new GdxRumble();
		} else {
			rumble = null;
		}

		addChildren(Arrays.asList(mainJoystick, rotateAxis, fireButton, slow, activatePowerup, startButton),
				false, false);
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
