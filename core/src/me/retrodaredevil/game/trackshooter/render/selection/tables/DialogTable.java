package me.retrodaredevil.game.trackshooter.render.selection.tables;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import me.retrodaredevil.game.trackshooter.render.RenderObject;
import me.retrodaredevil.game.trackshooter.render.selection.ContentTableProvider;

public class DialogTable implements ContentTableProvider {
	private final Dialog dialog;
	private final Table contentTable;
	public DialogTable(String title, RenderObject renderObject){

		dialog = new Dialog(title, renderObject.getUISkin());
		dialog.setMovable(false);
		dialog.setResizable(false);
		contentTable = new Table();
		dialog.getContentTable().add(new ScrollPane(contentTable)).center();
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
	public void render(float delta, Stage stage) {

		stage.addActor(dialog);
		dialog.setSize(stage.getWidth() * .92f, stage.getHeight() * .7f);
		dialog.setPosition(stage.getWidth() / 2f - dialog.getWidth() / 2f, stage.getHeight() / 2f - dialog.getHeight() / 2f);
	}

	@Override
	public void dispose() {
		dialog.remove();
	}
}
