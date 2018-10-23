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
	private final Dialog dialog;
	private final ScrollPane scrollPane;
	private final Cell<?> scrollPaneCell;
	private final Table contentTable;
	public DialogTable(String title, RenderObject renderObject){

		dialog = new Dialog(title, renderObject.getUISkin());
		dialog.setMovable(false);
		dialog.setResizable(false);

		scrollPane = new ScrollPane(contentTable = new Table());
		scrollPane.setScrollingDisabled(true, false);

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
	public void render(float delta, Stage stage) {

		stage.addActor(dialog);
		dialog.setSize(stage.getWidth() * .92f, stage.getHeight() * .7f);
		dialog.setPosition(stage.getWidth() / 2f - dialog.getWidth() / 2f, stage.getHeight() / 2f - dialog.getHeight() / 2f);
		System.out.println("contentTable width: " + contentTable.getWidth());
		System.out.println("scrollPane width: " + scrollPane.getWidth());
		System.out.println("dialog width: " + dialog.getWidth());
		scrollPaneCell.width(dialog.getWidth());
	}

	@Override
	public void dispose() {
		dialog.remove();
	}
}
