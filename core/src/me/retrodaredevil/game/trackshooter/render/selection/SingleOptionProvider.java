package me.retrodaredevil.game.trackshooter.render.selection;

import java.util.Collection;

public interface SingleOptionProvider {
	/**
	 * When this is called, the elements of the returned Collection are GUARANTEED to be added to the
	 * list/collection of SingleOptions. This means that you can do whatever logic you need to do to
	 * the returned value before returning it.
	 * @return The options to add
	 */
	Collection<? extends SingleOption> getOptionsToAdd();

	/**
	 * As of the current implementation, only calling this method and only when that call returns false,
	 * is a SingleOption allowed to be removed.
	 * <p>
	 * If this method is called, then the passed singleOption MUST be removed (Assume this returns false). Depending on the implementation,
	 * it is possible that {@link #getOptionsToAdd()} will re-add it (The implementation may mutate itself based on the result it returns which is why you
	 * must remove it if this returns false: When the implementation returns false, it expects you to remove it)
	 * @param singleOption A previously added SingleOption
	 * @return true if the option should be kept, false to remove
	 */
	boolean shouldKeep(SingleOption singleOption);
}
