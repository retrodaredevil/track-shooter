package me.retrodaredevil.game.trackshooter.entity.player;

import com.badlogic.gdx.Gdx;

import me.retrodaredevil.controller.output.ControllerRumble;
import me.retrodaredevil.game.trackshooter.account.achievement.AchievementHandler;
import me.retrodaredevil.game.trackshooter.achievement.DefaultGameEvent;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.enemies.shark.Shark;
import me.retrodaredevil.game.trackshooter.entity.enemies.snake.SnakePart;
import me.retrodaredevil.game.trackshooter.entity.powerup.Fruit;
import me.retrodaredevil.game.trackshooter.entity.powerup.PowerupPackage;

public class PlayerScore implements Score {
    private final int startingLives;
    private final int[] extraLivesAt;
    private final int extraLifeEvery;
    private final Player player;
    private final RumbleGetter rumbleGetter;
    private final AchievementHandler achievementHandler;

	private int score = 0;
	private int deaths = 0;
	private int numberShots = 0;
	private int totalNumberShots = 0;
	private int shotsHit = 0;

	private boolean isEnded = false;

	public PlayerScore(Player player, int startingLives, int[] extraLivesAt, int extraLifeEvery, RumbleGetter rumbleGetter, AchievementHandler achievementHandler){
		this.player = player;
		this.startingLives = startingLives;
		this.extraLivesAt = extraLivesAt;
		this.extraLifeEvery = extraLifeEvery;
		this.rumbleGetter = rumbleGetter;
		this.achievementHandler = achievementHandler;
	}

	/**
	 * @param player The player
	 * @param rumbleGetter The rumble getter for the player. This is allowed to return null.
	 */
	public PlayerScore(Player player, RumbleGetter rumbleGetter, AchievementHandler achievementHandler){
	    this(player, 3, new int[]{ 10000 }, 30000, rumbleGetter, achievementHandler);
    }

	@Override
	public int getScore() {
		return score;
	}

	@Override
	public int getLives() {
	    int lives = startingLives - deaths;
	    for(int extraLifeAt : extraLivesAt){
	        if(score >= extraLifeAt){
	            lives++;
            }
        }
        lives += (score / extraLifeEvery); // rounds down because integer division
		return lives;
	}

	@Override
	public void onKill(Entity killed, Entity killerSource, int points) {
		if(isEnded){
			return;
		}
		onScore(points);
		if(killed instanceof Shark){
			achievementHandler.incrementIfSupported(DefaultGameEvent.SHARKS_KILLED, 1);
		} else if (killed instanceof SnakePart){
			if(((SnakePart) killed).isHead()){
				achievementHandler.incrementIfSupported(DefaultGameEvent.SNAKES_KILLED, 1);
			}
		} else if(killed instanceof Fruit){
			achievementHandler.incrementIfSupported(DefaultGameEvent.FRUIT_CONSUMED, 1);
		} else if(killed instanceof PowerupPackage){
			achievementHandler.incrementIfSupported(DefaultGameEvent.POWER_UPS_COLLECTED, 1);
		}
	}

	@Override
	public void onScore(int points) {
		if(isEnded){
			return;
		}
		score += points;
		achievementHandler.submitScore(score);
	}

	@Override
	public void onDeath(Entity other) {
	    deaths++;
	    ControllerRumble rumble = rumbleGetter.getRumble();
	    if(rumble != null && rumble.isConnected()){
	    	long time = 300;
	    	if(getLives() <= 0){
	    		time = 600;
			}
	    	rumble.rumbleTime(time, 1);
		}
	}

    @Override
    public void onBulletHit(Entity hitEntity, Entity playerProjectile) {
		if(isEnded){
			return;
		}
	    shotsHit++;
    }

    @Override
    public void onShot(int numberOfShots) {
	    if(numberOfShots < 1){
	        throw new IllegalArgumentException("onShot() called with numberOfShots < 1");
        }
		if(isEnded){
			throw new IllegalStateException("The player cannot shoot when the game has ended!");
		}
        this.numberShots++;
        this.totalNumberShots += numberOfShots;
//        achievementHandler.incrementIfSupported(DefaultGameEvent.SHOTS_FIRED, numberOfShots); // TODO I commented this out because the debug messages were annoying
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

	@Override
	public void onGameEnd() {
		if(isEnded){
			throw new IllegalStateException("The game cannot end twice!");
		}
		achievementHandler.submitScore(score);
		isEnded = true;
	}

	@Override
	public void printOut() {
		Gdx.app.log("final score", "" + this.getScore());
		Gdx.app.log("Shots", "" + this.getNumberShots());
		Gdx.app.log("Total Shots", "" + this.getTotalNumberShots());
		Gdx.app.log("Hits", "" + this.getNumberShotsHit());
		int hits = this.getNumberShotsHit();
		int misses = (this.getTotalNumberShots() - this.getNumberShotsHit());
		if (misses > 0) {
			float hitMiss = Math.round(hits * 1000.0f / misses) / 10.0f;
			Gdx.app.log("hit/miss ratio", hitMiss + "%");
		} else {
			Gdx.app.log("hit/miss ratio", "undefined");
		}
	}
	public interface RumbleGetter {
		/** @return null or the ControllerRumble */
		ControllerRumble getRumble();
	}
}
