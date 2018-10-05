package me.retrodaredevil.game.input;

import me.retrodaredevil.controller.options.RadioOption;

/**
 * A GameInput that is usable by {@link ChangeableGameInput}
 */
public interface UsableGameInput extends GameInput, RadioOption {
	boolean isActiveInput();

	/**
	 * Sets boolean value for whether this is active. May be called repeatedly.
	 * @param b value for active input
	 */
	void setActiveInput(boolean b);
}
