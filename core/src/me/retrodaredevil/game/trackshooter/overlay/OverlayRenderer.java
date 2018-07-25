package me.retrodaredevil.game.trackshooter.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import java.util.Objects;

import me.retrodaredevil.game.trackshooter.render.RenderComponent;
import me.retrodaredevil.game.trackshooter.render.TextActor;

public class OverlayRenderer implements RenderComponent {
	private static final int SCORE_SPACES = 7;

	private static final Color textColor = new Color(1, 0, 0, 1);
	private static final Color scoreColor = new Color(1, 1, 1, 1);

    private final BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/main_font.fnt"),
            Gdx.files.internal("fonts/main_font.png"), false, true);

	private final Table currentScoreTable;
	private final Label scoreLabel;

	private final Table highScoreTable;
	private final Label highScoreLabel;

	private final Overlay overlay;

	public OverlayRenderer(Overlay overlay){
	    this.overlay = Objects.requireNonNull(overlay);

	    currentScoreTable = new Table();
	    currentScoreTable.setFillParent(true);
	    currentScoreTable.left().top();
        currentScoreTable.add(new Label("1UP", new Label.LabelStyle(font, textColor)));
		currentScoreTable.row();
		scoreLabel = new Label("", new Label.LabelStyle(font, scoreColor));
		currentScoreTable.add(scoreLabel);

		highScoreTable = new Table();
		highScoreTable.setFillParent(true);
		highScoreTable.center().top();
		highScoreTable.add(new Label("HIGH SCORE", new Label.LabelStyle(font, textColor)));
		highScoreTable.row();
		highScoreLabel = new Label("", new Label.LabelStyle(font, scoreColor));
		highScoreTable.add(highScoreLabel);

//
//		highScoreWordsText = new TextActor(textFont, "HIGH SCORE ");
//		highScoreText = new TextActor(scoreFont, "");
	}

	@Override
	public void render(float delta, Stage stage) {
        if(currentScoreTable.getStage() != stage){
            stage.addActor(currentScoreTable);
        }
		if(highScoreTable.getStage() != stage){
			stage.addActor(highScoreTable);
		}

		scoreLabel.setText(getScoreText(overlay.getCurrentScore()));
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
