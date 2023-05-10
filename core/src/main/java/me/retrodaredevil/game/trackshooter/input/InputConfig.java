package me.retrodaredevil.game.trackshooter.input;

import me.retrodaredevil.controller.input.InputPart;

public class InputConfig {
	private final InputQuirk inputQuirk;
	private final InputPart overrideRotateAxis;

	public InputConfig(InputQuirk inputQuirk, InputPart overrideRotateAxis) {
		this.inputQuirk = inputQuirk;
		this.overrideRotateAxis = overrideRotateAxis;
	}
	public InputConfig(InputQuirk inputQuirk) {
		this(inputQuirk, null);
	}

	public InputQuirk getInputQuirk() {
		return inputQuirk;
	}

	public InputPart getOverrideRotateAxis() {
		return overrideRotateAxis;
	}
}
