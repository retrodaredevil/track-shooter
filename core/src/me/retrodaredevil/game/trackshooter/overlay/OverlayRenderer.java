package me.retrodaredevil.game.trackshooter.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import java.util.Objects;

import me.retrodaredevil.game.trackshooter.render.RenderComponent;

public class OverlayRenderer implements RenderComponent {
	private static final int SCORE_SPACES = 7;

	private static final Color textColor = new Color(1, 0, 0, 1);
	private static final Color scoreColor = new Color(1, 1, 1, 1);

    private final BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/main_font.fnt"),
            Gdx.files.internal("fonts/main_font.png"), false, true);

    private final Table table;

//	private final Table currentScoreTable;
//	private final Label scoreLabel;
	private final Table[] currentScoreTables = new Table[4];
	private final Label[] currentScores = new Label[4];

//	private final Table highScoreTable;
	private final Label highScoreLabel;

	private final Overlay overlay;

	public OverlayRenderer(Overlay overlay){
	    this.overlay = Objects.requireNonNull(overlay);

	    table = new Table();
	    table.setFillParent(true);
	    for(int i = 0; i < currentScoreTables.length; i++){
			Table currentScoreTable = new Table();
			currentScoreTables[i] = currentScoreTable;
			table.addActor(currentScoreTable);

			currentScoreTable.setFillParent(true);
			currentScoreTable.add(new Label((i + 1) + "UP", new Label.LabelStyle(font, textColor)));
			currentScoreTable.row();

			Label scoreLabel = new Label("", new Label.LabelStyle(font, scoreColor));
			currentScores[i] = scoreLabel;
			currentScoreTable.add(scoreLabel).padTop(-10);
		}
		currentScoreTables[0].top().left();
		currentScoreTables[1].top().right();
		currentScoreTables[2].bottom().left();
		currentScoreTables[3].bottom().right();

		Table highScoreTable = new Table();
		table.addActor(highScoreTable);
//		highScoreTable.setDebug(true);
		highScoreTable.setFillParent(true);
		highScoreTable.center().top();
		highScoreTable.add(new Label(" HIGH SCORE", new Label.LabelStyle(font, textColor)));
		highScoreTable.row();
		highScoreLabel = new Label("", new Label.LabelStyle(font, scoreColor));
		highScoreTable.add(highScoreLabel).padTop(-10);

	}

	@Override
	public void render(float delta, Stage stage) {
		stage.addActor(table);

		final float scale = Math.min(stage.getWidth(), stage.getHeight()) / 640;
		table.setScale(scale);

        final int numberPlayers = overlay.getNumberPlayers();
        for(int i = 0; i < currentScoreTables.length; i++){
        	Group scoreGroup = currentScoreTables[i];
        	Label scoreLabel = currentScores[i];

        	String score = getScoreText(overlay.getCurrentScore(i));
//        	System.out.println("score for: " + i + " is " + score);
			scoreLabel.setText(score);
			if(i >= numberPlayers && i != 0){
				scoreGroup.setVisible(false);
			} else {
				scoreGroup.setVisible(true);
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
	    font.dispose();
	}
}
