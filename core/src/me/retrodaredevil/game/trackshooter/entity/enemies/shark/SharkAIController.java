package me.retrodaredevil.game.trackshooter.entity.enemies.shark;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.EntityController;
import me.retrodaredevil.game.trackshooter.entity.movement.MoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.OnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.SmoothTravelMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.TargetPositionMoveComponent;
import me.retrodaredevil.game.trackshooter.util.MathUtil;
import me.retrodaredevil.game.trackshooter.world.Track;
import me.retrodaredevil.game.trackshooter.world.World;

public class SharkAIController implements EntityController {

	private final Shark shark;
//	private final Entity target;
	private final Collection<? extends Entity> targets;

	private final float trackDistanceAway;
	private final float timeMultiplier;

	/**
	 *
	 * @param shark The Shark entity for this controller to control
	 * @param targets The reference to the list of targets (NOT a copy)
	 * @param trackDistanceAway The amount of distance away on the track the shark should target from. Internally, this number changes based on the time
	 * @param timeMultiplier The how much distance should the target distance change each second
	 */
	public SharkAIController(Shark shark, Collection<? extends Entity> targets, float trackDistanceAway, float timeMultiplier){
		this.shark = shark;
		this.targets = targets;
		this.trackDistanceAway = trackDistanceAway;
		this.timeMultiplier = timeMultiplier;
	}

	@Override
	public void update(float delta, World world) {
		MoveComponent moveComponent = shark.getMoveComponent();
		if(moveComponent instanceof TargetPositionMoveComponent){
			TargetPositionMoveComponent pointMove = (TargetPositionMoveComponent) moveComponent;
			long time = world.getTimeMillis();
			double offset = time / 1000D;
			offset %= world.getTrack().getTotalDistance();
			offset *= timeMultiplier;
			Float averageDistance = getAverageDistance(world.getTrack());
			if(averageDistance != null) {
				pointMove.setTargetPosition(world.getTrack().getDesiredLocation(averageDistance + trackDistanceAway + (float) offset));
			} else {
				pointMove.setTargetPosition(world.getTrack().getDesiredLocation(trackDistanceAway + (float) offset));
			}
//			pointMove.setTarget(world.getTrack(), target, trackDistanceAway + (float) offset);
		}
	}
	private Float getAverageDistance(Track track){
		final List<Float> tempChangeFromTrackOrigin = new ArrayList<>(); // no null elements

		for(Entity target : targets){
			MoveComponent targetMove = target.getMoveComponent();
			if(targetMove instanceof OnTrackMoveComponent && !target.isRemoved()){
				OnTrackMoveComponent trackMove = (OnTrackMoveComponent) targetMove;
				float distance = trackMove.getDistanceOnTrack();
				tempChangeFromTrackOrigin.add(MathUtil.minChange(distance, 0 , track.getTotalDistance()));
			}
		}
		int numberOnTrack = tempChangeFromTrackOrigin.size();
		if(numberOnTrack == 0){
			return null;
		}

		float sum = 0;
		for(float toAdd : tempChangeFromTrackOrigin){
			sum += toAdd;
		}
		return sum / numberOnTrack;
	}
}
