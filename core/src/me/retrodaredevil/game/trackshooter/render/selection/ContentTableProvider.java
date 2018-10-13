package me.retrodaredevil.game.trackshooter.render.selection;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;

public interface ContentTableProvider extends RenderComponent {
	Table getContentTable();

	void resetTable();
}
