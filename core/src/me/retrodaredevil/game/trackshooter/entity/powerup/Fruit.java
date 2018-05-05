package me.retrodaredevil.game.trackshooter.entity.powerup;

import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.SimpleEntity;
import me.retrodaredevil.game.trackshooter.entity.movement.OnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.util.CannotHitException;
import me.retrodaredevil.game.trackshooter.world.World;

public class Fruit extends SimpleEntity implements Powerup {

	private int points;

	private boolean eaten = false;

	public Fruit(int points, float velocity, float startingTrackDistance){
		super();
		this.points = points;
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
		player.getScoreObject().onKill(this, player, points);
		eaten = true;
	}

	@Override
	public boolean shouldRemove(World world) {
		return super.shouldRemove(world) || eaten;
	}
}
