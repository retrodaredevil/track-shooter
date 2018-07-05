package me.retrodaredevil.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import me.retrodaredevil.controller.*;
import me.retrodaredevil.controller.input.HighestPositionInputPart;
import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class DefaultGameInput extends GameInput {
	private final JoystickPart mainJoystick;
	private final JoystickPart rotateJoystick;
	private final InputPart fireButton;
	private final InputPart slow;
	private final InputPart activatePowerup;

	private final ControllerExtras extras;

	private final Collection<ControllerPart> parts;

	private ControllerInput reliesOn = null;

	public DefaultGameInput(StandardUSBControllerInput controller){
		mainJoystick = controller.leftJoy();
		rotateJoystick = controller.rightJoy();
		fireButton = new HighestPositionInputPart(controller.rightBumper(), controller.leftBumper());
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
		rotateJoystick = new GdxMouseJoystick();
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
	public JoystickPart rotateJoystick() {
		return rotateJoystick;
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
	public Collection<ControllerPart> getAllParts() {
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
