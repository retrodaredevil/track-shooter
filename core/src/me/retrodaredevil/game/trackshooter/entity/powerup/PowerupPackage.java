package me.retrodaredevil.game.trackshooter.entity.powerup;

import me.retrodaredevil.game.trackshooter.effect.Effect;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.movement.OnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.util.CannotHitException;
import me.retrodaredevil.game.trackshooter.world.World;

public abstract class PowerupPackage extends SimplePowerup {

	private boolean eaten = false;

	public PowerupPackage(float velocity, float startingTrackDistance){
		setHitboxSize(.6f, .6f);

		OnTrackMoveComponent trackMove = new OnTrackMoveComponent(this);
		trackMove.setDistance(startingTrackDistance);
		trackMove.setVelocity(velocity);
		setMoveComponent(trackMove);
	}

	@Override
	public void onHit(World world, Entity other) throws CannotHitException {
		if(!(other instanceof Player)){
			throw new CannotHitException(other, this);
		}
		Player player = (Player) other;
		player.addEffect(createEffect(player));
		eaten = true;
	}

	@Override
	public boolean shouldRemove(World world) {
		return super.shouldRemove(world) || eaten;
	}

	protected abstract Effect createEffect(Player player);
}
