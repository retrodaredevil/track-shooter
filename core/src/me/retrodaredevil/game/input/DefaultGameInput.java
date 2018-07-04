package me.retrodaredevil.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import me.retrodaredevil.controller.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class DefaultGameInput extends GameInput {
	private final JoystickPart mainJoystick;
	private final JoystickPart rotateJoystick;
	private final InputPart fireButton;
	private final InputPart slow;
	private final InputPart activatePowerup;

	private final Joysticks joysticks;

	private final Collection<ControllerPart> parts;

	private ControllerInput reliesOn = null;

	public DefaultGameInput(StandardUSBControllerInput controller){
		mainJoystick = controller.leftJoy();
		rotateJoystick = controller.rightJoy();
		fireButton = new HighestPositionInputPart(controller.rightBumper(), controller.leftBumper());
		slow = controller.leftStick();
		activatePowerup = controller.faceLeft();
		joysticks = new Joysticks(mainJoystick, rotateJoystick);

		parts = Collections.emptyList(); // parts are already handled by controller

		reliesOn = controller;
	}
	public DefaultGameInput(){
		if(Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope)){
			mainJoystick = new GdxGyroJoystick();
		} else {
			mainJoystick = FourKeyJoystick.newWASDJoystick();
		}
//		rotateJoystick = FourKeyJoystick.newArrowKeyJoystick();
		rotateJoystick = new GdxMouseJoystick();
		fireButton = new HighestPositionInputPart(new KeyInputPart(Input.Keys.SPACE), new KeyInputPart(Input.Buttons.LEFT, true));
		slow = new KeyInputPart(Input.Keys.SHIFT_LEFT);
		activatePowerup = new KeyInputPart(Input.Keys.F);
		joysticks = new Joysticks(mainJoystick, rotateJoystick);

		parts = Arrays.asList(mainJoystick, rotateJoystick, fireButton, slow, activatePowerup);
		setParentsToThis(parts, false, false);
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
	public Joysticks getJoysticks() {
		return joysticks;
	}

	@Override
	public Collection<ControllerPart> getAllParts() {
		return parts;
	}

	@Override
	public boolean isConnected(ControllerManager manager) {
		for(ControllerPart part : parts){
			if(!part.isConnected(manager)){
				return false;
			}
		}
		return reliesOn == null || reliesOn.isConnected(manager);
	}
}
