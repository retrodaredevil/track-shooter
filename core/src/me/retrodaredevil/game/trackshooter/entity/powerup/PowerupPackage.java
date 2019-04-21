package me.retrodaredevil.game.trackshooter.entity.powerup;

import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.movement.TravelRotateVelocityOnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.world.World;

public abstract class PowerupPackage extends SimplePowerup {

	private boolean eaten = false;

	/**
	 *
	 * @param velocity The velocity that this will have when on the track. If positive, will go in the positive direction on track and vice versa
	 * @param startingTrackDistance The starging distance on the track
	 */
	public PowerupPackage(World world, float velocity, float startingTrackDistance){
		super(world);
		setHitboxSize(.6f);

		TravelRotateVelocityOnTrackMoveComponent trackMove = new TravelRotateVelocityOnTrackMoveComponent(world, this);
		trackMove.setDistanceOnTrack(startingTrackDistance);
		trackMove.getTravelVelocitySetter().setVelocity(velocity);
		setMoveComponent(trackMove);
	}

	@Override
	public void onHit(Entity other)  {
		if(!(other instanceof Player)){
			eaten = true;
			return;
		}
		Player player = (Player) other;
		onHit(player);
		eaten = true;
	}

	@Override
	public boolean shouldRemove() {
		return super.shouldRemove() || eaten;
	}

	protected abstract void onHit(Player player);
}
