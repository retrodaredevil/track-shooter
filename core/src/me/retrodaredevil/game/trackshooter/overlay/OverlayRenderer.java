package me.retrodaredevil.game.trackshooter.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.Objects;

import me.retrodaredevil.game.trackshooter.render.RenderComponent;
import me.retrodaredevil.game.trackshooter.render.TextActor;

public class OverlayRenderer implements RenderComponent {
	private static final int SCORE_SPACES = 7;

	private static final BitmapFont textFont = new BitmapFont(Gdx.files.internal("fonts/main_font.fnt"),
			Gdx.files.internal("fonts/main_font.png"), false, true){
		{
			setColor(1, 0, 0, 1);
		}
	};

	private static final BitmapFont scoreFont = new BitmapFont(Gdx.files.internal("fonts/main_font.fnt"),
			Gdx.files.internal("fonts/main_font.png"), false, true){
		{
			setColor(1, 1, 1, 1);
		}
	};

	private final TextActor oneUpText;
	private final TextActor scoreText;

	private final TextActor highScoreWordsText;
	private final TextActor highScoreText;

	private final Overlay overlay;

	public OverlayRenderer(Overlay overlay){
	    this.overlay = Objects.requireNonNull(overlay);
		oneUpText = new TextActor(textFont, "1UP");
		scoreText = new TextActor(scoreFont, "");

		highScoreWordsText = new TextActor(textFont, "HIGH SCORE ");
		highScoreText = new TextActor(scoreFont, "");
	}

	@Override
	public void render(float delta, Stage stage) {
		if(oneUpText.getStage() != stage){
			stage.addActor(oneUpText);
		}
		if(scoreText.getStage() != stage){
			stage.addActor(scoreText);
		}
		if(highScoreWordsText.getStage() != stage){
			stage.addActor(highScoreWordsText);
		}
		if(highScoreText.getStage() != stage){
			stage.addActor(highScoreText);
		}


		scoreText.setText(getScoreText(overlay.getCurrentScore()));
		final float scoreYPosition = stage.getHeight() - scoreText.getHeight() - oneUpText.getHeight();
		scoreText.setPosition(0, scoreYPosition);

		final float textYPosition = stage.getHeight() - oneUpText.getHeight();
		oneUpText.setPosition(scoreText.getWidth() / 2.0f - oneUpText.getWidth() / 2.0f, textYPosition);

		final float centerXPosition = stage.getWidth() / 2.0f;
		highScoreText.setText(getScoreText(overlay.getHighScore()));
		highScoreText.setPosition(centerXPosition - highScoreText.getWidth() / 2.0f, scoreYPosition);

		highScoreWordsText.setPosition(centerXPosition - highScoreWordsText.getWidth() / 2.0f, textYPosition);

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
		textFont.dispose();
		scoreFont.dispose();
	}
}
