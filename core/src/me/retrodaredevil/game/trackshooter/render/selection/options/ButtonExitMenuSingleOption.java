package me.retrodaredevil.game.trackshooter.render.selection.options;

import com.badlogic.gdx.scenes.scene2d.ui.Button;

import me.retrodaredevil.game.trackshooter.render.selection.SelectAction;
import me.retrodaredevil.game.trackshooter.util.Size;

public class ButtonExitMenuSingleOption extends ButtonSingleOption {
	public ButtonExitMenuSingleOption(Button button, Size size) {
		super(button, size, requestingActions -> requestingActions.add(SelectAction.EXIT_MENU));
	}
}
