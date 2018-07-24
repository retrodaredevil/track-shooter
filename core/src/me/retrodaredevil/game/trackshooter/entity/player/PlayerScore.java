package me.retrodaredevil.game.trackshooter.entity.player;

import com.badlogic.gdx.Gdx;
import me.retrodaredevil.game.trackshooter.entity.Entity;

public class PlayerScore implements Score {
	private int score = 0;
	private int lives = 3;
	private final Player player;

	private int numberShots = 0;
	private int totalNumberShots = 0;

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

    @Override
    public void onShot(int numberOfShots) {
	    if(numberOfShots < 1){
	        throw new IllegalArgumentException("onShot() called with numberOfShots < 1");
        }
        this.numberShots++;
        this.totalNumberShots += numberOfShots;
    }

    @Override
    public int getNumberShots() {
        return numberShots;
    }

    @Override
    public int getTotalNumberShots() {
        return totalNumberShots;
    }
}
