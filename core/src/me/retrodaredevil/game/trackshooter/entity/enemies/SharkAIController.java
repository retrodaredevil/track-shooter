package me.retrodaredevil.game.trackshooter.entity.enemies;

import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.EntityController;
import me.retrodaredevil.game.trackshooter.entity.movement.MoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.PointTargetMoveComponent;
import me.retrodaredevil.game.trackshooter.world.World;

public class SharkAIController implements EntityController {

	private Shark shark;
	private Entity target;

	private float trackDistanceAway;
	private float timeMultiplier;

	/**
	 *
	 * @param shark The Shark entity for this controller to control
	 * @param target The entity that the Shark should base its track distance off of
	 * @param trackDistanceAway The amount of distance away on the track the shark should target from
	 * @param timeMultiplier The how much distance should the target distance change by each second
	 */
	public SharkAIController(Shark shark, Entity target, float trackDistanceAway, float timeMultiplier){
		this.shark = shark;
		this.target = target;
		this.trackDistanceAway = trackDistanceAway;
		this.timeMultiplier = timeMultiplier;
	}

	@Override
	public void update(float delta, World world) {
		MoveComponent moveComponent = shark.getMoveComponent();
		if(moveComponent instanceof PointTargetMoveComponent){
			PointTargetMoveComponent pointMove = (PointTargetMoveComponent) moveComponent;
			long time = System.currentTimeMillis();
			double offset = time / 1000D;
			offset %= world.getTrack().getTotalDistance();
			offset *= timeMultiplier;
			pointMove.setTarget(world.getTrack(), target, trackDistanceAway + (float) offset);
		}
	}
}
