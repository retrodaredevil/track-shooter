package me.retrodaredevil.game.input;

import com.badlogic.gdx.controllers.Controller;

import me.retrodaredevil.controller.input.AutoCachingInputPart;
import me.retrodaredevil.controller.input.AxisType;
import me.retrodaredevil.game.trackshooter.util.Util;

public class ControllerInputPart extends AutoCachingInputPart {
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

		if((type.isFull() && !type.isAnalog()) || type.isRangeOver()){
			throw new UnsupportedOperationException("Controller Single Input does not support AxisType: " + type);
		}
	}
	public ControllerInputPart(Controller controller, AxisType type, int code){
		this(controller, type, code, false);
	}
	@Override
	protected double calculatePosition() {
		AxisType type = getAxisType();
		if(type.isAnalog()){
			double value = controller.getAxis(this.code);
			double mult = inverted ? -1 : 1;
			value *= mult;
			return type.isFull() ? value : ((value + 1.0) / 2.0);
		}
		return (controller.getButton(this.code) == !inverted) ? 1 : 0;
	}

	@Override
	public boolean isConnected() {
		return Util.isControllerConnected(controller);
	}
}
