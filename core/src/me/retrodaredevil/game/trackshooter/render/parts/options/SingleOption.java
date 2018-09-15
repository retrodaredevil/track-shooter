package me.retrodaredevil.game.trackshooter.render.parts.options;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

public interface SingleOption {
	void update(Table table, OptionMenu optionMenu);
	void reset();
	void remove();
}
