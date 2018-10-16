package me.retrodaredevil.game.trackshooter.entity.powerup;

import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.movement.OnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.TravelRotateVelocityOnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.util.CannotHitException;
import me.retrodaredevil.game.trackshooter.world.World;

public abstract class PowerupPackage extends SimplePowerup {

	private boolean eaten = false;

	/**
	 *
	 * @param velocity The velocity that this will have when on the track. If positive, will go in the positive direction on track and vice versa
	 * @param startingTrackDistance The starging distance on the track
	 */
	public PowerupPackage(float velocity, float startingTrackDistance){
		setHitboxSize(.6f);

		TravelRotateVelocityOnTrackMoveComponent trackMove = new TravelRotateVelocityOnTrackMoveComponent(this);
		trackMove.setDistanceOnTrack(startingTrackDistance);
		trackMove.getTravelVelocitySetter().setVelocity(velocity);
		setMoveComponent(trackMove);
	}

	@Override
	public void onHit(World world, Entity other)  {
		if(!(other instanceof Player)){
			eaten = true;
			return;
		}
		Player player = (Player) other;
		onHit(world, player);
		eaten = true;
	}

	@Override
	public boolean shouldRemove(World world) {
		return super.shouldRemove(world) || eaten;
	}

	protected abstract void onHit(World world, Player player);
}
