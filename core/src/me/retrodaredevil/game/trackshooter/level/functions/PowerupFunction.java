package me.retrodaredevil.game.trackshooter.level.functions;

import com.badlogic.gdx.Gdx;

import java.util.Collection;

import me.retrodaredevil.game.trackshooter.entity.powerup.PowerupEntity;
import me.retrodaredevil.game.trackshooter.level.Level;
import me.retrodaredevil.game.trackshooter.level.LevelEndState;
import me.retrodaredevil.game.trackshooter.level.LevelMode;
import me.retrodaredevil.game.trackshooter.world.World;

public abstract class PowerupFunction implements LevelFunction {
	protected final World world;
	private final long addAt; // 15 seconds
	private final long removeAt; // stay for 10 seconds (remove at 25 seconds)
	private PowerupEntity powerup = null;

	protected PowerupFunction(World world, long addAt, long stayTime){
		this.world = world;
		this.addAt = addAt;
		this.removeAt = addAt + stayTime;
	}

	@Override
	public boolean update(float delta, Collection<? super LevelFunction> functionsToAdd) {
		Level level = world.getLevel();
		long modeTime = level.getModeTimeMillis();
		if(powerup != null){
			if(powerup.isRemoved()){
				return true; // the powerup must have been eaten so we are done here
			}
			if(modeTime >= removeAt){ // remove powerup if time is up
				powerup.setToRemove();
				return true;
			}
			return false;
		}

		// powerup == null
		if(level.getMode() == LevelMode.NORMAL && modeTime > addAt){
			this.powerup = createPowerup();
			level.addEntity(powerup);
			if(!powerup.canSetToRemove()){
				Gdx.app.error("created powerup", "We will be unable to remove powerup. Expect crash in future!");
			}
		}
		return false;
	}

	@Override
	public void levelEnd() {
		// do nothing because the level will automatically remove the powerup
	}

	/**
	 * Should create the powerup object but SHOULD NOT add it to world's entities
	 * @return The Powerup to be added to world
	 */
	protected abstract PowerupEntity createPowerup();


	@Override
	public void onModeChange(Level level, LevelMode mode, LevelMode previous) {
		if(powerup != null && !powerup.isRemoved()){
			powerup.setToRemove();
		}
	}

	@Override
	public LevelEndState canLevelEnd() {
		return LevelEndState.CAN_END;
	}
}
