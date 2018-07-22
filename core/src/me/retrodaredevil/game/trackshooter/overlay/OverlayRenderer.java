package me.retrodaredevil.game.trackshooter.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;

import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;
import me.retrodaredevil.game.trackshooter.render.TextActor;

public class OverlayRenderer implements RenderComponent {
	private static final int SCORE_SPACES = 8;

	private final BitmapFont textFont = new BitmapFont(
			Gdx.files.internal("fonts/main_font.fnt"), Gdx.files.internal("fonts/main_font.png"), false, true
	){
		{
			setColor(1, 0, 0, 1);
		}
	};

	private final BitmapFont scoreFont = new BitmapFont(
			Gdx.files.internal("fonts/main_font.fnt"), Gdx.files.internal("fonts/main_font.png"), false, true
	){
		{
			setColor(1, 1, 1, 1);
		}
	};

	private final TextActor oneUpText;
	private final TextActor scoreText;

	private final Player player;

	public OverlayRenderer(Player player){
		this.player = player;
		oneUpText = new TextActor(textFont, " 1UP");
		scoreText = new TextActor(scoreFont, "");
	}

	@Override
	public void render(float delta, Stage stage) {
		if(scoreText.getStage() != stage){
			stage.addActor(scoreText);
		}
		if(oneUpText.getStage() != stage){
			stage.addActor(oneUpText);
		}


		String scoreString = "" + player.getScoreObject().getScore();
		if(scoreString.length() == 1){
			scoreString = "0" + scoreString;
		}
		StringBuilder score = new StringBuilder();
		while(score.length() < SCORE_SPACES - scoreString.length()){
			score.append(' ');
		}
		score.append(scoreString);
		scoreText.setText(score.toString());
		scoreText.setPosition(0, stage.getHeight() - scoreText.getHeight() - oneUpText.getHeight());

		oneUpText.setPosition(scoreText.getWidth() / 2.0f - oneUpText.getWidth() / 2.0f, stage.getHeight() - oneUpText.getHeight());
	}

	@Override
	public void dispose() {
		textFont.dispose();
		scoreFont.dispose();
	}
}
