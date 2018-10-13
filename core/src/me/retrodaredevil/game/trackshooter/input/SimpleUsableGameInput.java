package me.retrodaredevil.game.trackshooter.input;

import me.retrodaredevil.controller.SimpleControllerInput;

public abstract class SimpleUsableGameInput extends SimpleControllerInput implements UsableGameInput {

	private boolean activeInput = false;

	@Override
	public boolean isActiveInput() {
		return activeInput;
	}

	@Override
	public void setActiveInput(boolean b) {
		activeInput = b;
	}
}
