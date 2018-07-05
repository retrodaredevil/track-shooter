package me.retrodaredevil.game.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.game.trackshooter.util.Util;

public class ControllerInputPart extends InputPart {
	private Controller controller;
	private int code;
	private boolean inverted;

	/**
	 *
	 * @param controller The controller
	 * @param type The AxisType
	 * @param code The code for the button or axis
	 * @param inverted true if this is inverted. Should be true for most y axises to when up, it is positive
	 */
	public ControllerInputPart(Controller controller, AxisType type, int code, boolean inverted){
		super(type);
		this.controller = controller;
		this.code = code;
		this.inverted = inverted;

		if(getAxisType() == AxisType.FULL_DIGITAL){
			throw new UnsupportedOperationException("Controller Single Input does not support FULL_DIGITAL");
		}
	}
	public ControllerInputPart(Controller controller, AxisType type, int code){
		this(controller, type, code, false);
	}
	@Override
	protected double calculatePosition() {
		AxisType type = getAxisType();
		boolean fullAnalog = type == AxisType.FULL_ANALOG;
		if(fullAnalog || type == AxisType.ANALOG){
			double value = controller.getAxis(this.code);
			double mult = inverted ? -1 : 1;
			value *= mult;
			return fullAnalog ? value : ((value + 1.0) / 2.0);
		}
		return (controller.getButton(this.code) == !inverted) ? 1 : 0;
	}

	@Override
	public boolean isConnected() {
		return Util.isControllerConnected(controller);
	}
}
