package me.retrodaredevil.game.trackshooter.input.implementations.helper;

import me.retrodaredevil.controller.input.AxisType;
import me.retrodaredevil.controller.input.InputPart;

public class DigitalChildPositionInputPart extends ChildPositionInputPart {
	public DigitalChildPositionInputPart(InputPart inputPart, DigitalGetter getter) {
		super(inputPart, AxisType.DIGITAL, (childInputPart) -> getter.isDown(childInputPart) ? 1 : 0);
	}
	public interface DigitalGetter{
		boolean isDown(InputPart inputPart);
	}
}
