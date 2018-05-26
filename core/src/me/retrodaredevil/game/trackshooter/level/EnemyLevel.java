package me.retrodaredevil.game.trackshooter.level;

import me.retrodaredevil.game.trackshooter.entity.Enemy;
import me.retrodaredevil.game.trackshooter.world.Track;
import me.retrodaredevil.game.trackshooter.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * The most basic level. It has enemies!
 * <p>
 * When implementing, you should override onStart() and call addEnemy() for each enemy you want to add. Also remember
 * to call super.onStart()
 */
public abstract class EnemyLevel extends SimpleLevel {

	private List<Enemy> enemyList = new ArrayList<>();
	private boolean reset = false;

	public EnemyLevel(int number, Track track) {
		super(number, track);
	}

	@Override
	protected void onStart(World world) {
	}

	@Override
	protected void onEnd(World world) {
	}

	@Override
	protected boolean onUpdate(float delta, World world) {
		World.updateEntityList(enemyList); // remove removed enemies

		if(getMode() == LevelMode.RESET){
			if(reset){
				for(Enemy enemy : enemyList){
					enemy.goToStart();
				}
				reset = false;
			} else {
				boolean resetDone = true;
				for (Enemy enemy : enemyList) {
					if (enemy.isGoingToStart()) {
						resetDone = false;
					}
				}
				if (resetDone) {
					setMode(LevelMode.STANDBY);
				}
			}
		}
		return enemyList.isEmpty(); // will be done if all the enemies are killed
	}

	protected void addEnemy(World world, Enemy enemy){
		addEntity(world, enemy); // allow the level to handle the entity
		enemyList.add(enemy); // we need to handle enemies ourselves
	}

	@Override
	protected void onModeChange(LevelMode mode, LevelMode previousMode) {
//		Gdx.app.debug("mode", mode.toString());
		if(mode == LevelMode.RESET) {
			reset = true;
		} else if(mode == LevelMode.NORMAL){
			for(Enemy enemy : enemyList){
				enemy.goNormalMode();
			}
		}
	}

}
