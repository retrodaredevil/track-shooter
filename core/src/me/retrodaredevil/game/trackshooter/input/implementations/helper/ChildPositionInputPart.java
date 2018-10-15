package me.retrodaredevil.game.trackshooter.input.implementations.helper;

import me.retrodaredevil.controller.input.AutoCachingInputPart;
import me.retrodaredevil.controller.input.AxisType;
import me.retrodaredevil.controller.input.InputPart;

public class ChildPositionInputPart extends AutoCachingInputPart {

	private final InputPart inputPart;
	private final PositionGetter getter;

	/**
	 *
	 * @param inputPart An InputPart that cannot have a parent
	 * @param axisType The AxisType
	 * @param getter The getter to get the position
	 */
	public ChildPositionInputPart(InputPart inputPart, AxisType axisType, PositionGetter getter){
		super(axisType, true);
		this.inputPart = inputPart;
		this.getter = getter;
		addChildren(false, false, inputPart);
	}

	@Override
	protected double calculatePosition() {
		return getter.getPosition(inputPart);
	}

	@Override
	public boolean isConnected() {
		return inputPart.isConnected();
	}
	public interface PositionGetter{
		double getPosition(InputPart childInputPart);
	}
}
