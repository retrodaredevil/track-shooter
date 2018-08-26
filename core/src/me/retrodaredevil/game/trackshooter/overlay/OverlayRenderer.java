package me.retrodaredevil.game.trackshooter.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import me.retrodaredevil.game.trackshooter.RenderObject;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;
import me.retrodaredevil.game.trackshooter.util.Resources;

public class OverlayRenderer implements RenderComponent {
	private static final int SCORE_SPACES = 7;
	private static final int MAX_LIVES_DISPLAYED = 5;
	private static final int MAX_ITEMS_DISPLAYED = 5;

//	private static final Color textColor = new Color(1, 0, 0, 1);
//	private static final Color scoreColor = new Color(1, 1, 1, 1);

//	private final BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/main_font.fnt"),
//			Gdx.files.internal("fonts/main_font.png"), false, true);

	private final Group group = new Table(){{setFillParent(true);}};
//	private final Group group = new Stack();

	private final Table[] cornerTables = new Table[4];
	private final Label[] currentScores = new Label[4];

	// each element in this array is a Table where only a certain number of its children will be
	// visible meaning that this should only contain images to represent lives and nothing else
	private final Table[] livesTables = new Table[4];
	private final Table[] itemsTables = new Table[4];

	private final Label highScoreLabel;

	private final Overlay overlay;

	public OverlayRenderer(Overlay overlay, RenderObject renderObject){
		this.overlay = Objects.requireNonNull(overlay);

		final Skin skin = renderObject.getMainSkin();

		BitmapFont font = skin.getFont("game_label");

		for(int i = 0; i < cornerTables.length; i++){
			Table cornerTable = new Table();
			cornerTables[i] = cornerTable;
			group.addActor(cornerTable);
			cornerTable.setFillParent(true);
			cornerTable.add(new Label((i + 1) + "UP", new Label.LabelStyle(font, skin.getColor("score_label"))));

			cornerTable.row();

			Label scoreLabel = new Label("", new Label.LabelStyle(font, skin.getColor("score")));
			currentScores[i] = scoreLabel;
			cornerTable.add(scoreLabel).padTop(-10);

			cornerTable.row();

			Table livesTable = new Table();
			livesTables[i] = livesTable;
			cornerTable.add(livesTable);
			livesTable.setHeight(24);
			for(int j = 0; j < MAX_LIVES_DISPLAYED; j++) {
				livesTable.add(new Image(skin.getDrawable("player"))).width(24).height(24);
			}

			cornerTable.row();

			Table itemsTable = new Table();
			itemsTables[i] = itemsTable;
			cornerTable.add(itemsTable);
		}
		cornerTables[0].top().left();
		cornerTables[1].top().right();
		cornerTables[2].bottom().left();
		cornerTables[3].bottom().right();

		Table highScoreTable = new Table();
		group.addActor(highScoreTable);
		highScoreTable.setFillParent(true);
		highScoreTable.center().top();
		highScoreTable.add(new Label(" HIGH SCORE", new Label.LabelStyle(font, skin.getColor("score_label"))));
		highScoreTable.row();
		highScoreLabel = new Label("", new Label.LabelStyle(font, skin.getColor("score")));
		highScoreTable.add(highScoreLabel).padTop(-10);

	}

	@Override
	public void render(float delta, Stage stage) {
//		group.setSize(stage.getWidth(), stage.getHeight());
		stage.addActor(group);


		final int numberPlayers = overlay.getNumberPlayers();
		for(int i = 0; i < cornerTables.length; i++){
			Group cornerTable = cornerTables[i];

			// ==== Visibility ====
			cornerTable.setVisible(i < numberPlayers || i == 0);

			// ==== Score ====
			Label scoreLabel = currentScores[i];
			String score = getScoreText(overlay.getCurrentScore(i));
			scoreLabel.setText(score);

			// ==== Lives ====
			Table livesTable = livesTables[i];
			final int numberToDraw = overlay.getShipsToDraw(i);
			int numberDrawn = 0;
			for(Actor a : livesTable.getChildren()){
				a.setVisible(numberDrawn < numberToDraw);
				numberDrawn++;
			}

			// ==== Items ====
			Table itemsTable = itemsTables[i];
			Collection<Image> itemImages = overlay.getItemImages(i);
			int numberCorrect = 0; // may use -1 as magic number
			for(Actor a : itemsTable.getChildren()){
				if(a instanceof Image){
					Image image = (Image) a;
					if(itemImages.contains(image)){
						numberCorrect++;
					} else {
						numberCorrect = -1;
						break;
					}
				} else {
					throw new IllegalStateException("The itemsTable can only contain images!");
				}
			}
			if (numberCorrect != itemImages.size()) {
				itemsTable.clearChildren();
				int j = 0;
				for(Image image : itemImages){
					if(j >= MAX_ITEMS_DISPLAYED){
						break;
					}
					itemsTable.add(image).center().size(24);
					j++;
				}
			}
		}
		highScoreLabel.setText(getScoreText(overlay.getHighScore()));

	}
	private static String getScoreText(int scoreValue){
		String scoreString = "" + scoreValue;
		if(scoreString.length() == 1){
			scoreString = "0" + scoreString;
		}
		StringBuilder score = new StringBuilder();
		while(score.length() < SCORE_SPACES - scoreString.length()){
			score.append(' ');
		}
		score.append(scoreString);
		return score.toString();
	}

	@Override
	public void dispose() {
//		font.dispose();
	}
}
