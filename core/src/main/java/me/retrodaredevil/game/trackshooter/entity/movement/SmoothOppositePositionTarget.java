package me.retrodaredevil.game.trackshooter.entity.movement;

import com.badlogic.gdx.math.Vector2;

import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.world.Track;
import me.retrodaredevil.game.trackshooter.world.World;

public class SmoothOppositePositionTarget extends NestedComponentMoveComponent implements EntityTargetingMoveComponent {
	private final World world;
	private final Entity target;
	private final SmoothTravelMoveComponent smoothTravel;

	/**
	 *
	 * @param world The World object
	 * @param target The entity (that's on the track) to target opposite from
	 * @param smoothTravel The MoveComponent of the entity that's going to target the passed {@code target}
	 */
	public SmoothOppositePositionTarget(World world, Entity target, SmoothTravelMoveComponent smoothTravel) {
		super(null, false, true);
		this.world = world;
		this.target = target;
		this.smoothTravel = smoothTravel;
	}

	@Override
	public Entity getEntityTarget() {
		return target;
	}

	@Override
	protected void onUpdate(float delta) {
		setNestedMoveComponent(smoothTravel);
		MoveComponent targetMove = target.getMoveComponent();
		if(targetMove instanceof OnTrackMoveComponent){
			OnTrackMoveComponent trackMove = (OnTrackMoveComponent) targetMove;
			float position = trackMove.getDistanceOnTrack();
			Track track = world.getTrack();
			Vector2 target = track.getDesiredLocation(position + track.getTotalDistance() / 2f);
			smoothTravel.setTargetPosition(target);
		} else {
			throw new IllegalStateException("target's MoveComponent must be an OnTrackMoveComponent");
		}
		super.onUpdate(delta);
	}
}
