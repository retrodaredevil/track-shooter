package me.retrodaredevil.game.trackshooter.entity.enemies.snake;

import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.EntityController;
import me.retrodaredevil.game.trackshooter.level.Level;
import me.retrodaredevil.game.trackshooter.level.LevelMode;
import me.retrodaredevil.game.trackshooter.world.World;

/**
 * An EntityController that supports head and non-head SnakeParts
 */
public class SnakeAIController implements EntityController {

	private final SnakePart part;
	private final Entity target;


	public SnakeAIController(SnakePart part, Entity target){
		this.part = part;
		this.target = target;
	}
	@Override
	public void update(float delta, World world) {
		if(!part.isHead()){
			return;
		}
		Level level = world.getLevel();
		if(level.getMode() == LevelMode.NORMAL){
			if(level.getModeTimeMillis() >= 35000){
				part.switchToManualTarget(target.getX(), target.getY());
			} else {
				part.switchToSmartSight(target);
			}
		}
	}

}
