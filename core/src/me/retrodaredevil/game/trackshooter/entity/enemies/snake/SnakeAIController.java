package me.retrodaredevil.game.trackshooter.entity.enemies.snake;

import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.EntityController;
import me.retrodaredevil.game.trackshooter.level.LevelMode;
import me.retrodaredevil.game.trackshooter.world.World;

/**
 * An EntityController that supports head and non-head SnakeParts
 */
public class SnakeAIController implements EntityController {
	public static final float DEFAULT_SPEED = 5;
	public static final float DEFAULT_TURN_MULTIPLIER = 2;

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
		if(world.getLevel().getMode() == LevelMode.NORMAL){
			part.switchToSmartSight(target);
//			System.out.println("target rotation for: " + Integer.toHexString(this.hashCode()));
//			System.out.println(((SmoothTravelMoveComponent) part.getMoveComponent()).getTravelVelocity());
		}
	}

}
