package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.Collection;

import me.retrodaredevil.game.trackshooter.achievement.AchievementHandler;
import me.retrodaredevil.game.trackshooter.achievement.implementations.DefaultAchievement;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.enemies.shark.Shark;
import me.retrodaredevil.game.trackshooter.entity.enemies.shark.SharkAIController;
import me.retrodaredevil.game.trackshooter.entity.friendly.CargoShip;
import me.retrodaredevil.game.trackshooter.entity.movement.MoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.OnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.level.EnemyLevel;
import me.retrodaredevil.game.trackshooter.level.Level;
import me.retrodaredevil.game.trackshooter.level.LevelGetter;
import me.retrodaredevil.game.trackshooter.level.functions.BonusCargoFunction;
import me.retrodaredevil.game.trackshooter.level.functions.FruitFunction;
import me.retrodaredevil.game.trackshooter.level.functions.SnakeFunction;
import me.retrodaredevil.game.trackshooter.level.functions.StarFishFunction;
import me.retrodaredevil.game.trackshooter.level.functions.TripleShotPowerupFunction;
import me.retrodaredevil.game.trackshooter.util.Points;
import me.retrodaredevil.game.trackshooter.util.Resources;
import me.retrodaredevil.game.trackshooter.world.Track;
import me.retrodaredevil.game.trackshooter.world.Tracks;
import me.retrodaredevil.game.trackshooter.world.World;

/**
 * This is the default level getter used all of the time
 */
public class GameLevelGetter implements LevelGetter {
	private final int STARTING_GRACE_PERIOD = 12;
	private final int END_GRACE_PERIOD = 19;

	private int levelNumber = 0; // still starts at 1 (incremented first thing in start of nextLevel())
	private final Track[] tracks;
	private final Collection<? extends Player> players; // may be mutated
	private final AchievementHandler achievementHandler;

	/**
	 *
	 * @param players The reference to the list of players (NOT a copy). May be mutated after this instance is constructed
	 */
	public GameLevelGetter(Collection<? extends Player> players, AchievementHandler achievementHandler){
		this.players = players;
		this.achievementHandler = achievementHandler;
		this.tracks = new Track[] { Tracks.newMazeTrack(), Tracks.newPointyTrack(), Tracks.newPlusTrack(), Tracks.newKingdomTrack(), Tracks.newCircleTrack() };
	}

	@Override
	public Level nextLevel(World theWorldToPass) {
		levelNumber++; // future programmers you're welcome that I put this on a separate line.
		final Track track = tracks[(levelNumber - 1) % tracks.length];
		return new EnemyLevel(theWorldToPass, levelNumber, track) {
			@Override
			protected void onStart() {
				super.onStart();
				if(levelNumber > 5){
					achievementHandler.manualAchieveIfSupported(DefaultAchievement.CLEAR_LEVEL_5);
					achievementHandler.manualRevealIfSupported(DefaultAchievement.CLEAR_LEVEL_10);
				}
				if(levelNumber > 10){
					achievementHandler.manualAchieveIfSupported(DefaultAchievement.CLEAR_LEVEL_10);
					achievementHandler.manualRevealIfSupported(DefaultAchievement.CLEAR_LEVEL_30);
				}
				if(levelNumber > 30){
					achievementHandler.manualAchieveIfSupported(DefaultAchievement.CLEAR_LEVEL_30);
				}
				final boolean isEasy = levelNumber >= 10 && (levelNumber - 2) % 8 == 0; // 10, 18, 26 // galaga level reference
				final Track track = world.getTrack();
				for(Player player : players){ // move all players to a random spot
					float distance = MathUtils.random(track.getTotalDistance());
					MoveComponent move = player.getMoveComponent();
					if(move instanceof OnTrackMoveComponent){
						((OnTrackMoveComponent) move).setDistanceOnTrack(distance);
					} else if(levelNumber == 1){
						player.setLocation(0, 0);
					}
					if(levelNumber == 1) { // point to the center
						Vector2 position = track.getDesiredLocation(distance);
						player.setRotation(Vector2.Zero.cpy().sub(position).angle());
					}
				}

				addFunction(new FruitFunction(world));
				if(levelNumber != 1 && levelNumber != 3 && levelNumber % 4 != 0 && !isEasy){ // on all levels except 1, 3 and any multiples of 4
					for(Player player : players) {
						addFunction(new SnakeFunction(world, player));
					}
				}
				if(levelNumber % 2 == 1){
					addFunction(new TripleShotPowerupFunction(world));
				} else if(levelNumber >= 4) { // all even levels >= 4
					Entity cargoEntity = new CargoShip(world, .8f * MathUtils.randomSign(), MathUtils.random(track.getTotalDistance()));
					this.addEntity(cargoEntity);
					final Points points;
					if(levelNumber >= 12){
						points = Resources.Points.P5000;
					} else if (levelNumber >= 6){
						points = Resources.Points.P3000;
					} else {
						points = Resources.Points.P1000;
					}
					addFunction(new BonusCargoFunction(world, cargoEntity, players, points, achievementHandler));
				}
				if(levelNumber > 3 && levelNumber % 3 == 0 && levelNumber % 9 != 0 && !isEasy){ // 6, 12, 15, 21
					float spawnAfter = 20;
					if(levelNumber >= 20){
						spawnAfter = 5;
					} else if(levelNumber >= 10){
						spawnAfter = 13;
					}
					addFunction(new StarFishFunction(world, spawnAfter, players, achievementHandler));
				} else {
					if(levelNumber <= 2){
						addFunction(new StarFishFunction(world, 100, players, achievementHandler));
					} else {
						addFunction(new StarFishFunction(world, 50, players, achievementHandler));
					}
				}


				final int amountLevelNumber;
				if(levelNumber >= STARTING_GRACE_PERIOD){
					amountLevelNumber = levelNumber >= END_GRACE_PERIOD ? levelNumber - (END_GRACE_PERIOD - STARTING_GRACE_PERIOD) : STARTING_GRACE_PERIOD;
				} else {
					amountLevelNumber = levelNumber;
				}
				final int amount = 4 + (amountLevelNumber / 2); // add a shark every 2 levels
				final float spacing = world.getTrack().getTotalDistance() / amount;
				final int waitTimeIndexShift = MathUtils.random(amount);
				final boolean invertWaitTimeIndex = MathUtils.randomBoolean();
				for(int i = 0; i < amount; i++){
					final int sign = ((i % 2) * 2) - 1; // instead of using Math.pow(-1, i), we use this
					final float trackDistanceAway = sign * ((i / 2f) * spacing);
					final float angle = (i * 360f) / amount + 45;
					Vector2 location = new Vector2(MathUtils.cosDeg(angle), MathUtils.sinDeg(angle));

					float waitTimeIndex = (i + waitTimeIndexShift) % amount;
					if(invertWaitTimeIndex){
						waitTimeIndex = amount - waitTimeIndex - 1;
					}
					final float waitBeforeMoveTime;
					if(levelNumber <= 3 || isEasy) {
						waitBeforeMoveTime = Math.min(waitTimeIndex * waitTimeIndex * .5f, 10);
					} else if(levelNumber >= 18) {
						waitBeforeMoveTime = 0;
					} else if(levelNumber >= 12){
						waitBeforeMoveTime = waitTimeIndex / levelNumber * 2;
					} else if(levelNumber >= 9){
						waitBeforeMoveTime = waitTimeIndex * .25f;
					} else if(levelNumber >= 6){
						waitBeforeMoveTime = waitTimeIndex * .5f;
					} else {
						waitBeforeMoveTime = waitTimeIndex;
					}

					Shark shark = new Shark(world, location, angle, waitBeforeMoveTime);
					shark.setEntityController(new SharkAIController(world, shark, players, trackDistanceAway, sign * (2f + (i * .5f / amount))));
					// start in the start position
					shark.setLocation(location, angle);

					addEntity(shark);
				}
			}
		};
	}
}
