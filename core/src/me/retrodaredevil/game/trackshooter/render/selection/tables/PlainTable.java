package me.retrodaredevil.game.trackshooter.render.selection.tables;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import me.retrodaredevil.game.trackshooter.render.selection.ContentTableProvider;

/**
 * A {@link ContentTableProvider} that is a simple table that also draws the stage
 */
public class PlainTable implements ContentTableProvider {

	private final Stage stage;
	private final Table contentTable = new Table();
	{
		contentTable.setFillParent(true);
		contentTable.center();
	}

	public PlainTable(Stage stage) {
		this.stage = stage;
	}

	@Override
	public Table getContentTable() {
		return contentTable;
	}

	@Override
	public void resetTable() {
		contentTable.clearChildren();
	}

	@Override
	public boolean isHorizontal() {
		return false;
	}

	@Override
	public void render(float delta) {
		stage.addActor(contentTable);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void dispose() {
		contentTable.remove();
	}
}
