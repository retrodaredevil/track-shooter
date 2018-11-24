package me.retrodaredevil.game.trackshooter.input.implementations;

import com.badlogic.gdx.controllers.Controller;

import me.retrodaredevil.controller.input.AutoCachingInputPart;
import me.retrodaredevil.controller.input.AxisType;
import me.retrodaredevil.game.trackshooter.util.Util;

public class ControllerInputPart extends AutoCachingInputPart {
	private final Controller controller;
	private final int code;
	private final boolean inverted;
	private final boolean isAxis;

	/**
	 *
	 * @param controller The controller
	 * @param type The AxisType
	 * @param code The code for the button or axis. If this is < 0, then {@link #isConnected()} will return false.
	 *             However, this should not be used for a disconnected input part. Use {@link me.retrodaredevil.controller.input.DummyInputPart} for that.
	 * @param inverted true if this is inverted. Should be true for most y axises to when up, it is positive
	 * @param isAxis If true, the passed code is a code to be used with controller.getAxis(), otherwise, controller.getButton() will be used
	 */
	public ControllerInputPart(Controller controller, AxisType type, int code, boolean inverted, boolean isAxis){
		super(type);
		this.controller = controller;
		this.code = code;
		this.inverted = inverted;
		this.isAxis = isAxis;

		if(type.isRangeOver()){
			throw new UnsupportedOperationException("Controller Single Input does not support AxisType: " + type);
		}
	}
	public ControllerInputPart(Controller controller, AxisType type, int code, boolean inverted){
		this(controller, type, code, inverted,type.isAnalog() || type.isFull());
	}
	public ControllerInputPart(Controller controller, AxisType type, int code){
		this(controller, type, code, false);
	}
	@Override
	protected double calculatePosition() {
		AxisType type = getAxisType();
		if(isAxis){
			final double value = controller.getAxis(this.code) * (inverted ? -1 : 1);
			return type.isFull() ? value : ((value + 1.0) / 2.0);
		}
		return (controller.getButton(this.code) == !inverted) ? 1 : 0;
	}

	@Override
	public boolean isConnected() {
		return Util.isControllerConnected(controller) && code >= 0;
	}
}
