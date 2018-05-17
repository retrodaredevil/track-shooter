package me.retrodaredevil.game.trackshooter.level;

import me.retrodaredevil.game.trackshooter.entity.Enemy;
import me.retrodaredevil.game.trackshooter.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * The most basic level. It has enemies!
 */
public class EnemyLevel extends SimpleLevel {

	private List<Enemy> enemyList = new ArrayList<>();
	private boolean resetting = false;

	protected EnemyLevel(int number) {
		super(number);
	}

	@Override
	protected void onUpdate(float delta, World world) {
		World.updateEntityList(enemyList); // remove removed enemies
	}

	@Override
	protected void onStart(World world) {
//		for(Enemy enemy : enemyList){
//
//		}
	}

	@Override
	public boolean isDone() {
		return isStarted() && enemyList.isEmpty();
	}

	@Override
	public boolean resetAll() {
		boolean done = true;
		for(Enemy enemy : enemyList){
			if(!enemy.goToStart()){
				done = false;
			}
		}
		resetting = !done;
		return done;
	}
}
