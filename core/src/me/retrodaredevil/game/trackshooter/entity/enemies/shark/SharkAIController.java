package me.retrodaredevil.game.trackshooter.entity.enemies.shark;

import java.util.List;

import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.EntityController;
import me.retrodaredevil.game.trackshooter.entity.movement.MoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.OnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.SmoothTravelMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.TargetPositionMoveComponent;
import me.retrodaredevil.game.trackshooter.world.World;

public class SharkAIController implements EntityController {

	private final Shark shark;
	private final Entity target;

	private float trackDistanceAway;
	private float timeMultiplier;

	/**
	 *
	 * @param shark The Shark entity for this controller to control
	 * @param targets The list of targets with at least one element
	 * @param trackDistanceAway The amount of distance away on the track the shark should target from
	 * @param timeMultiplier The how much distance should the target distance change each second
	 */
	public SharkAIController(Shark shark, List<? extends Entity> targets, float trackDistanceAway, float timeMultiplier){
		this.shark = shark;
		this.target = targets.get(0); // TODO incorporate all targets
		this.trackDistanceAway = trackDistanceAway;
		this.timeMultiplier = timeMultiplier;
	}

	@Override
	public void update(float delta, World world) {
		MoveComponent moveComponent = shark.getMoveComponent();
		if(moveComponent instanceof TargetPositionMoveComponent){
			TargetPositionMoveComponent pointMove = (TargetPositionMoveComponent) moveComponent;
			long time = System.currentTimeMillis();
			double offset = time / 1000D;
			offset %= world.getTrack().getTotalDistance();
			offset *= timeMultiplier;
			MoveComponent targetMove = target.getMoveComponent();
			if(targetMove instanceof OnTrackMoveComponent){
				OnTrackMoveComponent trackMove = (OnTrackMoveComponent) targetMove;
				pointMove.setTargetPosition(world.getTrack().getDesiredLocation(trackMove.getDistanceOnTrack() + trackDistanceAway + (float) offset));
			}
//			pointMove.setTarget(world.getTrack(), target, trackDistanceAway + (float) offset);
		}
	}
}
