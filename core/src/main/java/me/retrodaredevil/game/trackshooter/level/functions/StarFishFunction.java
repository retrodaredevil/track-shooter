package me.retrodaredevil.game.trackshooter.level.functions;

import com.badlogic.gdx.math.MathUtils;

import java.util.Collection;

import me.retrodaredevil.game.trackshooter.account.achievement.AchievementHandler;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.enemies.StarFish;
import me.retrodaredevil.game.trackshooter.entity.movement.MoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.OnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.level.Level;
import me.retrodaredevil.game.trackshooter.level.LevelEndState;
import me.retrodaredevil.game.trackshooter.level.LevelMode;
import me.retrodaredevil.game.trackshooter.util.MathUtil;
import me.retrodaredevil.game.trackshooter.world.Track;
import me.retrodaredevil.game.trackshooter.world.World;

public class StarFishFunction implements LevelFunction {
	private static final float GRACE_DISTANCE = 10;

	private final World world;
	private final Collection<? extends Entity> entities;
	private final float spawnAfter;
	private final AchievementHandler achievementHandler;

	/**
	 * @param spawnAfter the number of seconds to spawn the StarFish after
	 * @param entities Some collection of entities (usually Players) that are on the track,
	 * @param achievementHandler
	 */
	public StarFishFunction(World world, float spawnAfter, Collection<? extends Entity> entities, AchievementHandler achievementHandler){
		this.world = world;
		this.entities = entities;
		this.spawnAfter = spawnAfter;
		this.achievementHandler = achievementHandler;
	}

	@Override
	public boolean update(float delta, Collection<? super LevelFunction> functionsToAdd) {
		Level level = world.getLevel();

		if(level.getMode() == LevelMode.NORMAL && level.getModeTime() > spawnAfter){
			final int levelNumber = world.getLevel().getNumber();
			float speed = 4.5f;
			if(levelNumber >= 23){
				speed = 10.5f;
			} else if(levelNumber >= 13) {
				speed = 7;
			} else if(levelNumber >= 8){
				speed = 6;
			} else if (levelNumber >= 3){
				speed = 5;
			}

			float distance;
			do { // HEY!!! A USE FOR A DO WHILE LOOP! THIS IS EXCITING!!
				// TODO If there are many, many players, this *could* result in an infinite loop.
				distance = world.getTrack().getTotalDistance() * MathUtils.random();
			} while (!canSpawn(distance, world.getTrack()));
			Entity entity = new StarFish(world, speed, distance, achievementHandler);
			world.getLevel().addEntity(entity);
			return true;
		}
		return false;
	}
	private boolean canSpawn(float spawnDistance, Track track){
		for(Entity entity : entities){
			MoveComponent moveComponent = entity.getMoveComponent();
			if(moveComponent instanceof OnTrackMoveComponent){
				OnTrackMoveComponent trackMove = (OnTrackMoveComponent) moveComponent;
				if(MathUtil.minDistance(trackMove.getDistanceOnTrack(), spawnDistance, track.getTotalDistance()) <= GRACE_DISTANCE){
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void levelEnd() {

	}

	@Override
	public void onModeChange(Level level, LevelMode mode, LevelMode previous) {
	}

	@Override
	public LevelEndState canLevelEnd() {
		return LevelEndState.CAN_END; // starfish aren't that important. Sorry Starry
	}
}
