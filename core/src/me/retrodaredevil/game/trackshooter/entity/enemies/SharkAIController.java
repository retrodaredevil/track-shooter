package me.retrodaredevil.game.trackshooter.entity.enemies;

import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.EntityController;
import me.retrodaredevil.game.trackshooter.entity.movement.MoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.OnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.PointTargetMoveComponent;
import me.retrodaredevil.game.trackshooter.world.World;

public class SharkAIController implements EntityController {

	private Shark shark;
	private Entity target;

	private float trackDistanceAway;

	public SharkAIController(Shark shark, Entity target, float trackDistanceAway){
		this.shark = shark;
		this.target = target;
		this.trackDistanceAway = trackDistanceAway;
	}

	@Override
	public void update(float delta, World world) {
		MoveComponent moveComponent = shark.getMoveComponent();
		if(moveComponent instanceof PointTargetMoveComponent){
			PointTargetMoveComponent pointMove = (PointTargetMoveComponent) moveComponent;
			pointMove.setTarget(world.getTrack(), target, trackDistanceAway);
		}
	}
}
