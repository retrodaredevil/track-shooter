package me.retrodaredevil.game.trackshooter.level.functions;

import com.badlogic.gdx.math.MathUtils;

import java.util.Collection;

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

	private final Collection<? extends Entity> entities;
	private final float spawnAfter;

	/**
	 * @param spawnAfter the number of seconds to spawn the StarFish after
	 * @param entities Some collection of entities (usually Players) that are on the track,
	 *                    that will be used so the starfish doesn't spawn on top of them
	 */
	public StarFishFunction(float spawnAfter, Collection<? extends Entity> entities){
		this.entities = entities;
		this.spawnAfter = spawnAfter;
	}

	@Override
	public boolean update(float delta, World world, Collection<? super LevelFunction> functionsToAdd) {
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
			Entity entity = new StarFish(speed, distance);
			world.getLevel().addEntity(world, entity);
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
	public void levelEnd(World world) {

	}

	@Override
	public void onModeChange(Level level, LevelMode mode, LevelMode previous) {
	}

	@Override
	public LevelEndState canLevelEnd(World world) {
		return LevelEndState.CAN_END; // starfish aren't that important. Sorry Starry
	}
}
