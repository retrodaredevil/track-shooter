package me.retrodaredevil.game.trackshooter.entity.player;

import com.badlogic.gdx.Gdx;
import me.retrodaredevil.game.trackshooter.entity.Entity;

public class PlayerScore implements Score {
	private int score = 0;
	private int lives = 3;
	private Player player;

	public PlayerScore(Player player){
		this.player = player;
	}

	@Override
	public int getScore() {
		return score;
	}

	@Override
	public int getLives() {
		return lives;
	}

	@Override
	public void onKill(Entity killed, Entity killerSource, int points) {
		score += points;
		Gdx.app.debug("score", "" + score);
	}

	@Override
	public void onDeath(Entity other) {
		lives--;
	}
}
