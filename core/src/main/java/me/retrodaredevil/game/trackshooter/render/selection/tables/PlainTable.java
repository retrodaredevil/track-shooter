package me.retrodaredevil.game.trackshooter.render.selection.tables;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import me.retrodaredevil.game.trackshooter.render.selection.ContentTableProvider;

public class PlainTable implements ContentTableProvider {

	private final Table contentTable = new Table();
	{
		contentTable.setFillParent(true);
		contentTable.center();
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
	public void render(float delta, Stage stage) {
		stage.addActor(contentTable);
	}

	@Override
	public void dispose() {
		contentTable.remove();
	}
}
