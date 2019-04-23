package me.retrodaredevil.game.trackshooter.render.parts;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.util.Collection;
import java.util.Objects;

import me.retrodaredevil.game.trackshooter.render.RenderObject;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;

public class OverlayRenderer implements RenderComponent {
	private static final int SCORE_SPACES = 7;
	private static final int MAX_LIVES_DISPLAYED = 5;
	private static final int MAX_ITEMS_DISPLAYED = 5;


	private final Group group = new Table(){{setFillParent(true);}};

	private final Table[] cornerTables = new Table[4];
	private final Label[] currentScores = new Label[4];

	// each element in this array is a Table where only a certain number of its children will be
	// visible meaning that this should only contain images to represent lives and nothing else
	private final Table[] livesTables = new Table[4];
	private final Table[] itemsTables = new Table[4];

	private final Label highScoreLabel;
	private final Label levelCounterLabel; // Later will will replace this with pac-man or galaga style level counter

	private final Overlay overlay;
	private final RenderObject renderObject;
	private final Stage stage;

	private final Button pauseButton;

	OverlayRenderer(Overlay overlay, RenderObject renderObject, Stage stage){
		this.overlay = Objects.requireNonNull(overlay);
		this.renderObject = Objects.requireNonNull(renderObject);
		this.stage = stage;

		final Skin skin = renderObject.getMainSkin();
		final BitmapFont font = skin.getFont("game_label");

		// ===== Pause Button ======
		Table buttonTable = new Table();
		group.addActor(buttonTable);
		buttonTable.setFillParent(true);
		buttonTable.add(pauseButton = new TextButton("pause", renderObject.getUISkin())).width(80).height(50);
		buttonTable.top().right();
		pauseButton.setVisible(false); // by default not visible


		// ====== Player Scores ========
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
				livesTable.add(new Image(Player.Type.NORMAL.getDrawable(renderObject.getMainSkin()))).width(24).height(24);
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

		// ======== High Score ========
		Table highScoreTable = new Table();
		group.addActor(highScoreTable);
		highScoreTable.setFillParent(true);
		highScoreTable.center().top();
		highScoreTable.add(new Label(" HIGH SCORE", new Label.LabelStyle(font, skin.getColor("score_label"))));
		highScoreTable.row();
		highScoreLabel = new Label("", new Label.LabelStyle(font, skin.getColor("score")));
		highScoreTable.add(highScoreLabel).padTop(-10);

		// ========= Level Counter =======
		Table levelCounterTable = new Table();
		group.addActor(levelCounterTable);
		levelCounterTable.setFillParent(true);
		levelCounterTable.center().bottom();
		levelCounterLabel = new Label("", new Label.LabelStyle(font, skin.getColor("score_label")));
		levelCounterTable.add(levelCounterLabel);
	}
	public Button getPauseButton(){
		return pauseButton;
	}

	@Override
	public void render(float delta) {
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
			Drawable drawable = overlay.getPlayerType(i).getDrawable(renderObject.getMainSkin());
			for(Actor a : livesTable.getChildren()){
				a.setVisible(numberDrawn < numberToDraw);

				Image image = (Image) a;
				image.setDrawable(drawable);

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
		levelCounterLabel.setText("" + overlay.getLevelNumber());
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
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
