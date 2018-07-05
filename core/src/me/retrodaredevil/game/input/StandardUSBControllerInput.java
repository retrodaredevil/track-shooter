package me.retrodaredevil.game.input;

import com.badlogic.gdx.controllers.Controller;
import me.retrodaredevil.controller.*;
import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.input.TwoAxisJoystickPart;
import me.retrodaredevil.game.trackshooter.util.Util;

import java.util.Arrays;
import java.util.Collection;

public class StandardUSBControllerInput extends StandardControllerInput {

	private final Controller controller;
	private final InputPart start, select,
			faceUp, faceDown, faceLeft, faceRight,
			leftBumper, rightBumper,
			leftTrigger, rightTrigger,
			leftStick, rightStick;

	private final JoystickPart dPad, leftJoy, rightJoy;
	private final ControllerExtras extras;

	private final Collection<ControllerPart> parts;

	public StandardUSBControllerInput(Controller controller){
		this.controller = controller;
		final InputPart.AxisType FULL_ANALOG = new InputPart.AxisType(true, true);
		final InputPart.AxisType DIGITAL = new InputPart.AxisType(false, false);

		InputPart leftXAxis = new ControllerInputPart(controller, FULL_ANALOG, 0);
		InputPart leftYAxis = new ControllerInputPart(controller, FULL_ANALOG, 1, true);

		InputPart rightXAxis = new ControllerInputPart(controller, FULL_ANALOG, 2);
		InputPart rightYAxis = new ControllerInputPart(controller, FULL_ANALOG, 3, true);

		InputPart dXAxis = new ControllerInputPart(controller, FULL_ANALOG, 4);
		InputPart dYAxis = new ControllerInputPart(controller, FULL_ANALOG, 5, true);

		start = new ControllerInputPart(controller, DIGITAL, 9);
		select = new ControllerInputPart(controller, DIGITAL, 8);

		faceUp = new ControllerInputPart(controller, DIGITAL, 0);
		faceDown = new ControllerInputPart(controller, DIGITAL, 2);
		faceLeft = new ControllerInputPart(controller, DIGITAL, 3);
		faceRight = new ControllerInputPart(controller, DIGITAL, 1);

		leftBumper = new ControllerInputPart(controller, DIGITAL, 4);
		rightBumper = new ControllerInputPart(controller, DIGITAL, 5);

		leftTrigger = new ControllerInputPart(controller, DIGITAL, 6);
		rightTrigger = new ControllerInputPart(controller, DIGITAL, 7);

		leftStick = new ControllerInputPart(controller, DIGITAL, 10);
		rightStick = new ControllerInputPart(controller, DIGITAL, 11);

		dPad = new TwoAxisJoystickPart(dXAxis, dYAxis);
		leftJoy = new TwoAxisJoystickPart(leftXAxis, leftYAxis);
		rightJoy = new TwoAxisJoystickPart(rightXAxis, rightYAxis);

		extras = new ControllerExtras();
		extras.setRumble(new GdxControllerRumble(controller));

		parts = Arrays.asList(leftXAxis, leftYAxis, rightXAxis, rightYAxis,
				start, select, faceUp, faceDown, faceLeft, faceRight,
				leftBumper, rightBumper, leftTrigger, rightTrigger,
				leftStick, rightStick,
				dPad, leftJoy, rightJoy);

		// the axises already have parents and we don't want to change them
		setParentsToThis(parts, false, true);

	}

	@Override
	public JoystickPart dPad() {
		return dPad;
	}

	@Override
	public JoystickPart leftJoy() {
		return leftJoy;
	}

	@Override
	public JoystickPart rightJoy() {
		return rightJoy;
	}

	@Override
	public InputPart leftStick() {
		return leftStick;
	}

	@Override
	public InputPart rightStick() {
		return rightStick;
	}

	@Override
	public InputPart start() {
		return start;
	}

	@Override
	public InputPart select() {
		return select;
	}

	@Override
	public InputPart faceUp() {
		return faceUp;
	}

	@Override
	public InputPart faceDown() {
		return faceDown;
	}

	@Override
	public InputPart faceLeft() {
		return faceLeft;
	}

	@Override
	public InputPart faceRight() {
		return faceRight;
	}

	@Override
	public InputPart leftBumper() {
		return leftBumper;
	}

	@Override
	public InputPart rightBumper() {
		return rightBumper;
	}

	@Override
	public InputPart leftTrigger() {
		return leftTrigger;
	}

	@Override
	public InputPart rightTrigger() {
		return rightTrigger;
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
		return Util.isControllerConnected(controller);
	}
}
