package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.InputProcessor;

import java.util.Collection;

public interface InputFocusable {
	boolean isWantsToFocus();

	/**
	 *
	 * @return The input priority. A higher value represents a higher priority
	 */
	int getFocusPriority();



	/**
	 * When this is called, it means that this InputFocusable is receiving focus and that these
	 * InputProcessors will be focused on.
	 * @return A Collection usually with a size of 1 representing the InputProcessor(s) that should gain focus
	 */
	Collection<? extends InputProcessor> getInputProcessorsToFocus();
}
