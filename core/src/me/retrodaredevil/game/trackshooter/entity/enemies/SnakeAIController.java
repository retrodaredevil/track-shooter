package me.retrodaredevil.game.trackshooter.entity.enemies;

import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.EntityController;
import me.retrodaredevil.game.trackshooter.entity.movement.MoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.SmoothTravelMoveComponent;
import me.retrodaredevil.game.trackshooter.world.World;

public class SnakeAIController implements EntityController {
	private SnakePart part;
	private Entity target;
	public SnakeAIController(SnakePart part, Entity target){
		this.part = part;
		this.target = target;
	}
	@Override
	public void update(float delta, World world) {
		if(!part.isHead()){
			return;
		}
		MoveComponent moveComponent = part.getMoveComponent();
		if(moveComponent instanceof SmoothTravelMoveComponent){
			SmoothTravelMoveComponent travelMove = (SmoothTravelMoveComponent) moveComponent;
			travelMove.setTarget(world.getTrack(), target, 0);
		}
	}
}
