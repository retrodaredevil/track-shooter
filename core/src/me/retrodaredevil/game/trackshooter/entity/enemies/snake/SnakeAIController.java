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
			float time = level.getLevelTime();
			time %= 60;
			if(time < 15){
				part.switchToOppositeTrackPositionTarget(target);
			} else if(time < 27){
				part.switchToSmartSight(target);
			} else if(time < 33){
				part.switchToOppositeTrackPositionTarget(target);
			} else if(time < 40){
				part.switchToSmartSight(target);
			} else {
				part.switchToManualTarget(target.getX(), target.getY());
			}
		}
	}

}
