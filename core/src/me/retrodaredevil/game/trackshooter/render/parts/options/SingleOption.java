package me.retrodaredevil.game.trackshooter.render.parts.options;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.Collection;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;

/**
 * As of right now, this is for representing a single, selectable and updatable option. In the future,
 * this may be able to represent a button, or just anything that can fit on a menu
 */
public interface SingleOption {
	void update(Table table, OptionMenu optionMenu);

	/**
	 * Should reset the control option to its original value
	 */
	void reset();

	/**
	 * Should remove whatever was added to the table in {@link #update(Table, OptionMenu)}
	 */
	void remove();

	/**
	 * Called when this option is "selected"
	 * @param delta
	 * @param selector The selector joystick
	 * @param select The button to select
	 * @param back The back button
	 * @param requestedActions A collection of actions that the caller would like to do. The implementation should remove actions that are not desired
	 */
	void selectUpdate(float delta, JoystickPart selector, InputPart select, InputPart back, Collection<SelectAction> requestedActions);
	void deselect();


	enum SelectAction {
		EXIT_MENU, CHANGE_OPTION
	}
}
