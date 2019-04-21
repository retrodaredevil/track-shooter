package me.retrodaredevil.game.trackshooter.level;

import me.retrodaredevil.game.trackshooter.entity.Enemy;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.world.Track;
import me.retrodaredevil.game.trackshooter.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * This type of level handles Enemies that are in getEntities().
 * <p>
 * When implementing, you should override onStart() and call addEntity() for each entity/enemy you want to add. Also remember
 * to call super.onStart()
 */
public abstract class EnemyLevel extends SimpleLevel {

	private boolean reset = false;

	public EnemyLevel(World world, int number, Track track) {
		super(world, number, track);
	}

	@Override
	protected void onStart() {
	}

	@Override
	protected void onEnd() {
	}

	@Override
	protected void onUpdate(float delta) {
		if(getMode() == LevelMode.RESET){
			if(reset){
				for(Entity entity : this.getEntities()){
					if(entity instanceof Enemy) {
						((Enemy) entity).goToStart();
					}
				}
				reset = false;
			} else {
				boolean resetDone = true;
				for(Entity entity : this.getEntities()){
					if(entity instanceof Enemy) {
						if (((Enemy) entity).isGoingToStart()) {
							resetDone = false;
						}
					}
				}
				if (resetDone) {
					setMode(LevelMode.STANDBY);
				}
			}
		}
	}

	@Override
	protected void onModeChange(LevelMode mode, LevelMode previousMode) {
//		Gdx.app.debug("mode", mode.toString());
		if(mode == LevelMode.RESET) {
			reset = true;
		} else if(mode == LevelMode.NORMAL){
			for(Entity entity : this.getEntities()){
				if(entity instanceof Enemy) {
					((Enemy) entity).goNormalMode();
				}
			}
		}
	}

}
