package me.retrodaredevil.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import me.retrodaredevil.controller.ControllerExtras;
import me.retrodaredevil.controller.ControllerInput;
import me.retrodaredevil.controller.ControllerPart;
import me.retrodaredevil.controller.StandardControllerInput;
import me.retrodaredevil.controller.input.HighestPositionInputPart;
import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;

public class DefaultGameInput extends GameInput {
	private final JoystickPart mainJoystick;
//	private final JoystickPart rotateJoystick;
	private final InputPart rotateAxis;
	private final InputPart fireButton;
	private final InputPart slow;
	private final InputPart activatePowerup;

	private final ControllerExtras extras;

	private final Collection<ControllerPart> parts;

	private ControllerInput reliesOn = null;

	public DefaultGameInput(StandardControllerInput controller){
		mainJoystick = controller.leftJoy();
		rotateAxis = controller.rightJoy().getXAxis();
		fireButton = new HighestPositionInputPart(controller.rightBumper(), controller.leftBumper());
//		fireButton = controller.leftBumper();
		slow = controller.leftStick();
		activatePowerup = controller.faceLeft();

		extras = createExtras();

		parts = Collections.emptyList(); // parts are already handled by controller

		reliesOn = controller;
	}
	public DefaultGameInput(){
		if(Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope)){
			mainJoystick = new GdxTiltJoystick(20);
		} else {
			mainJoystick = FourKeyJoystick.newWASDJoystick();
		}
//		rotateJoystick = FourKeyJoystick.newArrowKeyJoystick();
		JoystickPart rotateJoystick = new GdxMouseJoystick();
		rotateAxis = rotateJoystick.getXAxis();
		fireButton = new HighestPositionInputPart(new KeyInputPart(Input.Keys.SPACE), new KeyInputPart(Input.Buttons.LEFT, true));
		slow = new KeyInputPart(Input.Keys.SHIFT_LEFT);
		activatePowerup = new KeyInputPart(Input.Keys.F);
//		if(Gdx.input.isPeripheralAvailable(Input.Peripheral.HardwareKeyboard)) {
//		} else {
//		}
		extras = createExtras();

		parts = Arrays.asList(mainJoystick, rotateJoystick, fireButton, slow, activatePowerup);
		setParentsToThis(parts, false, false);
	}
	private static ControllerExtras createExtras(){
		ControllerExtras r = new ControllerExtras();
		if(Gdx.input.isPeripheralAvailable(Input.Peripheral.Vibrator)) {
			r.setRumble(new GdxRumble());
		}
		return r;
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
	public ControllerExtras getExtras() {
		return extras;
	}

	@Override
	public Collection<ControllerPart> getPartsToUpdate() {
		return parts;
	}

	@Override
	public boolean isConnected() {
		for(ControllerPart part : parts){
			if(!part.isConnected()){
				return false;
			}
		}
		return reliesOn == null || reliesOn.isConnected();
	}
}
