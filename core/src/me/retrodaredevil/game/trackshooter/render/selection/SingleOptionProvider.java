package me.retrodaredevil.game.trackshooter.render.selection;

import java.util.Collection;

public interface SingleOptionProvider {
	/**
	 * When this is called, the elements the the returned Collection are GUARANTEED to be added to the
	 * list/collection of SingleOptions. This means that you can do whatever logic you need to do to
	 * the returned value before returning it.
	 * @return The options to add
	 */
	Collection<? extends SingleOption> getOptionsToAdd();

	/**
	 * @param singleOption A previously added SingleOption
	 * @return true if the option should be kept, false to remove
	 */
	boolean shouldKeep(SingleOption singleOption);
}
