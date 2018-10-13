package me.retrodaredevil.game.trackshooter.input.implementations;

import java.util.Objects;

import me.retrodaredevil.controller.input.AutoCachingInputPart;
import me.retrodaredevil.controller.input.AxisType;
import me.retrodaredevil.controller.input.InputPart;

public class ReleaseButtonPress extends AutoCachingInputPart {
	private final InputPart inputPart;

	/**
	 * @param inputPart An input part. Cannot already have a parent
	 */
	public ReleaseButtonPress(InputPart inputPart) {
		super(inputPart.getAxisType(), true);
		this.inputPart = Objects.requireNonNull(inputPart);
		addChildren(false, false, inputPart);
	}

	@Override
	public AxisType getAxisType() {
		return inputPart.getAxisType();
	}

	@Override
	protected double calculatePosition() {
		if(inputPart.isReleased()){
			return 1;
		}
		return 0;
	}

	@Override
	public boolean isConnected() {
		return inputPart.isConnected();
	}
}
