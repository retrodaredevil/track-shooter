package me.retrodaredevil.game.trackshooter.entity.player;

import com.badlogic.gdx.Gdx;
import me.retrodaredevil.game.trackshooter.entity.Entity;

public class PlayerScore implements Score {
    private final int startingLives;
    private final int[] extraLivesAt;
    private final int extraLifeEvery;
    private final Player player;

	private int score = 0;
	private int deaths = 0;
	private int numberShots = 0;
	private int totalNumberShots = 0;
	private int shotsHit = 0;

	public PlayerScore(Player player, int startingLives, int[] extraLivesAt, int extraLifeEvery){
		this.player = player;
		this.startingLives = startingLives;
		this.extraLivesAt = extraLivesAt;
		this.extraLifeEvery = extraLifeEvery;
	}
	public PlayerScore(Player player){
	    this(player, 3, new int[]{ 10000 }, 30000);
    }

	@Override
	public int getScore() {
		return score;
	}

	@Override
	public int getLives() {
	    int lives = startingLives - deaths;
	    for(int extraLifeAt : extraLivesAt){
	        if(score > extraLifeAt){
	            lives++;
            }
        }
        lives += (score / extraLifeEvery); // rounds down because integer division
		return lives;
	}

	@Override
	public void onKill(Entity killed, Entity killerSource, int points) {
		score += points;
		Gdx.app.debug("score", "" + score);
	}

	@Override
	public void onDeath(Entity other) {
	    deaths++;
	}

    @Override
    public void onBulletHit(Entity hitEntity, Entity playerProjectile) {
	    shotsHit++;
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

    @Override
    public int getNumberShotsHit() {
	    return shotsHit;
    }
}
