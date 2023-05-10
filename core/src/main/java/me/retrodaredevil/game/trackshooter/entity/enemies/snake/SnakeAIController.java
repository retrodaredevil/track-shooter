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

	private final World world;
	private final SnakePart part;
	private final Entity target;


	public SnakeAIController(World world, SnakePart part, Entity target){
		this.world = world;
		this.part = part;
		this.target = target;
	}
	@Override
	public void update(float delta) {
		if(!part.isHead()){
			return;
		}
		Level level = world.getLevel();
		if(level.getMode() == LevelMode.NORMAL){
			float time = level.getModeTime();
			time %= 60;
			boolean success = true;
			if(time < 12){
				part.switchToSmartSight(target);
			} else if(time < 15){
				success = part.switchToOppositeTrackPositionTarget(target);
			} else if(time < 27){
				part.switchToSmartSight(target);
			} else if(time < 33){
				success = part.switchToOppositeTrackPositionTarget(target);
			} else if(time < 40){
				part.switchToSmartSight(target);
			} else {
				part.switchToManualTarget(target.getX(), target.getY());
			}
			if(!success){
				part.switchToSmartSight(target);
			}
		}
	}

}
