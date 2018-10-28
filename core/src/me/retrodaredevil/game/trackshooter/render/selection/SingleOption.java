package me.retrodaredevil.game.trackshooter.render.selection;

import java.util.Collection;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;

/**
 * As of right now, this is for representing a single, selectable and updatable option. In the future,
 * this may be able to represent a button, or just anything that can fit on a menu
 * <p>
 * NOTE: This contents that this SingleOption adds to the table may be removed. If/when this happens,
 * this does not mean that this SingleOption will stop being updated. It simply means that you must re-add
 * whatever the implementation of this added.
 */
public interface SingleOption {
	/**
	 * Should update or add anything that is desired to the table. Note that even if something is added,
	 * the table may be reset so if you find that an Actor that you added to the table no longer has a parent,
	 * you must re-add that actor to the table.
	 * <p>
	 * If you add something to the table, it is expected that you also add a new row after adding things
	 * @param contentTableProvider The table to add something to
	 * @param requestedActions The actions that are currently being requested. This is allowed to be modified or added to
	 */
	void renderUpdate(ContentTableProvider contentTableProvider, Collection<? super SelectAction> requestedActions);

	/**
	 * Should reset the control option to its original state
	 */
	void reset();

	/**
	 * Should remove whatever was added to the table in {@link #renderUpdate(ContentTableProvider, Collection)}
	 */
	void remove();

	/**
	 * Called when this option is "selected"
	 * @param delta The delta time in seconds
	 * @param selector The selector joystick
	 * @param select The button to select
	 * @param back The back button
	 * @param requestedActions A collection of actions that the caller would like to do. The implementation should remove actions that are not desired
	 */
	void selectUpdate(float delta, JoystickPart selector, InputPart select, InputPart back, Collection<? super SelectAction> requestedActions);

	/**
	 * Called when this option should be deselected
	 */
	void deselect();


	enum SelectAction {
		/**
		 * Usually used when using the back button
		 */
		EXIT_MENU,
		/**
		 * Used when pressing up/down or in some cases left/right to change the option
		 */
		CHANGE_OPTION
	}
}
