package me.retrodaredevil.game.trackshooter.input;

import com.badlogic.gdx.controllers.Controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import me.retrodaredevil.controller.SimpleControllerInput;
import me.retrodaredevil.controller.input.AxisType;
import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.controller.input.TwoAxisJoystickPart;
import me.retrodaredevil.controller.options.ConfigurableControllerPart;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionValues;
import me.retrodaredevil.controller.output.ControllerRumble;
import me.retrodaredevil.controller.types.RumbleCapableController;
import me.retrodaredevil.controller.types.StandardControllerInput;
import me.retrodaredevil.game.trackshooter.input.implementations.ControllerInputPart;
import me.retrodaredevil.game.trackshooter.input.implementations.ControllerPovJoystick;
import me.retrodaredevil.game.trackshooter.input.implementations.GdxControllerRumble;
import me.retrodaredevil.game.trackshooter.util.Util;

public class StandardUSBControllerInput extends SimpleControllerInput implements StandardControllerInput, RumbleCapableController, ConfigurableControllerPart {

	private final Controller controller;
	private final InputPart start, select,
			yButton, aButton, xButton, bButton,
			leftBumper, rightBumper,
			leftTrigger, rightTrigger,
			leftStick, rightStick;

	private final JoystickPart dPad, leftJoy, rightJoy;

	private final ControllerRumble rumble;

	private final ControlOption isNintendoControllerControlOption;

	public StandardUSBControllerInput(Controller controller){
		isNintendoControllerControlOption = new ControlOption("Are Buttons Nintendo Style",
				"Should be checked if the controller has A as left face button.",
				"controller." + controller.getName() + ".layout", OptionValues.createBooleanOptionValue(false));
		this.controller = controller;

		start = new ControllerInputPart(controller, AxisType.DIGITAL, 9);
		select = new ControllerInputPart(controller, AxisType.DIGITAL, 8);

		yButton = new ControllerInputPart(controller, AxisType.DIGITAL, 0);
		aButton = new ControllerInputPart(controller, AxisType.DIGITAL, 2);
		xButton = new ControllerInputPart(controller, AxisType.DIGITAL, 3);
		bButton = new ControllerInputPart(controller, AxisType.DIGITAL, 1);

		leftBumper = new ControllerInputPart(controller, AxisType.DIGITAL, 4);
		rightBumper = new ControllerInputPart(controller, AxisType.DIGITAL, 5);

		leftTrigger = new ControllerInputPart(controller, AxisType.DIGITAL, 6);
		rightTrigger = new ControllerInputPart(controller, AxisType.DIGITAL, 7);

		leftStick = new ControllerInputPart(controller, AxisType.DIGITAL, 10);
		rightStick = new ControllerInputPart(controller, AxisType.DIGITAL, 11);

//		getDPad = new TwoAxisJoystickPart(dXAxis, dYAxis, true);
		dPad = new ControllerPovJoystick(controller, 0);
		leftJoy = new TwoAxisJoystickPart(new ControllerInputPart(controller, AxisType.FULL_ANALOG, 0), new ControllerInputPart(controller, AxisType.FULL_ANALOG, 1, true));
		rightJoy = new TwoAxisJoystickPart(new ControllerInputPart(controller, AxisType.FULL_ANALOG, 2), new ControllerInputPart(controller, AxisType.FULL_ANALOG, 3, true));

		this.rumble = new GdxControllerRumble(controller);


		// the axises already have parents and we don't want to change them
		addChildren(false, true,
				start, select, yButton, aButton, xButton, bButton,
				leftBumper, rightBumper, leftTrigger, rightTrigger,
				leftStick, rightStick,
				dPad, leftJoy, rightJoy, rumble);

	}
	private boolean isNintendoStyle(){
		return isNintendoControllerControlOption.getOptionValue().getBooleanOptionValue();
	}

	@Override
	public Collection<? extends ControlOption> getControlOptions() {
		return Collections.singleton(isNintendoControllerControlOption);
	}

	@Override
	public JoystickPart getDPad() {
		return dPad;
	}

	@Override
	public JoystickPart getLeftJoy() {
		return leftJoy;
	}

	@Override
	public JoystickPart getRightJoy() {
		return rightJoy;
	}

	@Override
	public InputPart getLeftStick() {
		return leftStick;
	}

	@Override
	public InputPart getRightStick() {
		return rightStick;
	}

	@Override
	public InputPart getStart() {
		return start;
	}

	@Override
	public InputPart getSelect() {
		return select;
	}

	@Override
	public InputPart getFaceUp() {
		return isNintendoStyle() ? xButton : yButton;
	}
	@Override
	public InputPart getFaceDown() {
		return isNintendoStyle() ? bButton : aButton;
	}
	@Override
	public InputPart getFaceLeft() {
		return isNintendoStyle() ? yButton : xButton;
	}
	@Override
	public InputPart getFaceRight() {
		return isNintendoStyle() ? aButton : bButton;
	}

	@Override
	public InputPart getAButton() {
		return aButton;
	}
	@Override
	public InputPart getBButton() {
		return bButton;
	}
	@Override
	public InputPart getXButton() {
		return xButton;
	}
	@Override
	public InputPart getYButton() {
		return yButton;
	}

	@Override
	public InputPart getLeftBumper() {
		return leftBumper;
	}

	@Override
	public InputPart getRightBumper() {
		return rightBumper;
	}

	@Override
	public InputPart getLeftTrigger() {
		return leftTrigger;
	}

	@Override
	public InputPart getRightTrigger() {
		return rightTrigger;
	}

	@Override
	public boolean isConnected() {
		return Util.isControllerConnected(controller);
	}

	@Override
	public ControllerRumble getRumble() {
		return rumble;
	}
}
