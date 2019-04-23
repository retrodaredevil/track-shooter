package me.retrodaredevil.game.trackshooter.render.selection.tables;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import me.retrodaredevil.game.trackshooter.render.RenderObject;
import me.retrodaredevil.game.trackshooter.render.selection.ContentTableProvider;

/**
 * Makes a dialog table width a scroll pane
 */
public class DialogTable implements ContentTableProvider {
	private final Stage stage;
	private final Dialog dialog;
	private final Cell<?> scrollPaneCell;
	private final Table contentTable;
	public DialogTable(Stage stage, String title, RenderObject renderObject){
		this.stage = stage;

		dialog = new Dialog(title, renderObject.getUISkin());
		dialog.setMovable(false);
		dialog.setResizable(false);

		ScrollPane scrollPane = new ScrollPane(contentTable = new Table());
		scrollPane.setScrollingDisabled(true, false);
		scrollPane.setFillParent(true);

		final Table dialogTable = dialog.getContentTable();
		scrollPaneCell = dialogTable.add(scrollPane);
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
		stage.addActor(dialog);
		dialog.setSize(stage.getWidth() * .92f, stage.getHeight() * .85f);
		dialog.setPosition(stage.getWidth() / 2f - dialog.getWidth() / 2f, stage.getHeight() * .04f);
		scrollPaneCell.width(dialog.getWidth());
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void dispose() {
		dialog.remove();
	}
}
