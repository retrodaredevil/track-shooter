package me.retrodaredevil.game.input;

import com.badlogic.gdx.controllers.Controller;
import me.retrodaredevil.input.ControllerManager;
import me.retrodaredevil.input.SingleInput;

public class ControllerSingleInput extends SingleInput {
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
	public ControllerSingleInput(Controller controller, AxisType type, int code, boolean inverted){
		super(type);
		this.controller = controller;
		this.code = code;
		this.inverted = inverted;

		if(getAxisType() == AxisType.FULL_DIGITAL){
			throw new UnsupportedOperationException("Controller Single Input does not support FULL_DIGITAL");
		}
	}
	public ControllerSingleInput(Controller controller, AxisType type, int code){
		this(controller, type, code, false);
	}
	@Override
	protected double calculatePosition() {
		AxisType type = getAxisType();
		boolean fullAnalog = type == AxisType.FULL_ANALOG;
		if(fullAnalog || type == AxisType.ANALOG){
			float value = controller.getAxis(this.code);
			double mult = inverted ? -1 : 1;
			return fullAnalog ? value * mult : ((value + 1.0) / 2.0) * mult;
		}
		return controller.getButton(this.code) == !inverted ? 1 : 0;
	}

	@Override
	public boolean isConnected(ControllerManager manager) {
		return true;
	}
}
