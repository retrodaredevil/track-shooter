package me.retrodaredevil.game.trackshooter.level;

import com.badlogic.gdx.Gdx;
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

	protected void addEnemy(World world, Enemy enemy){
		world.addEntity(enemy);
		enemyList.add(enemy);
	}

	@Override
	protected void onUpdate(float delta, World world) {
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
	}


	@Override
	public boolean isDone() {
		return isStarted() && enemyList.isEmpty();
	}

	@Override
	protected void onModeChange(LevelMode mode, LevelMode previousMode) {
		Gdx.app.debug("mode", mode.toString());
		if(mode == LevelMode.RESET) {
			System.out.println("resetting");
			reset = true;
		} else if(mode == LevelMode.NORMAL){
			for(Enemy enemy : enemyList){
				enemy.goNormalMode();
			}
		}
	}

}
