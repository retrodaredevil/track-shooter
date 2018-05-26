package me.retrodaredevil.game.trackshooter.level.functions;

import com.badlogic.gdx.math.MathUtils;
import me.retrodaredevil.game.trackshooter.entity.powerup.Cherry;
import me.retrodaredevil.game.trackshooter.entity.powerup.Fruit;
import me.retrodaredevil.game.trackshooter.level.Level;
import me.retrodaredevil.game.trackshooter.level.LevelMode;
import me.retrodaredevil.game.trackshooter.world.World;

import java.util.Queue;

public class FruitFunction implements LevelFunction {
	private static final long ADD_AT = 15000; // 15 seconds
	private static final long REMOVE_AT = ADD_AT + 10000; // stay for 10 seconds (remove at 25 seconds)
	private Fruit fruit = null;

	public FruitFunction(){
	}

	@Override
	public boolean update(float delta, World world, Queue<LevelFunction> functionsToAdd) {
		Level level = world.getLevel();
		long modeTime = level.getModeTime();
		if(fruit != null){
			if(fruit.isRemoved()){
				return true; // the fruit must have been eaten so we are done here
			}
			if(modeTime >= REMOVE_AT){
				fruit.setToRemove();
				return true;
			}
			return false;
		}

		// fruit == null
		if(level.getMode() == LevelMode.NORMAL && level.getModeTime() > 15000){
			this.fruit = createFruit(world);
			level.addEntity(world, fruit);
		}
		return false;
	}

	@Override
	public void levelEnd(World world) {
		// do nothing because the level will automatically remove the fruit
	}

	/**
	 * Should create the fruit object but SHOULD NOT add it to world's entities
	 * @param world The world the Fruit will be added too
	 * @return The Fruit to be added to world
	 */
	protected Fruit createFruit(World world){
		return new Cherry(getFruitStartingDistance(world));
	}
	protected float getFruitStartingDistance(World world){
		return world.getTrack().getTotalDistance() * MathUtils.random();
	}

	@Override
	public void onModeChange(Level level, LevelMode mode, LevelMode previous) {
		if(fruit != null){
			fruit.setToRemove();
		}
	}
}
