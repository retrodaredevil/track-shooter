package me.retrodaredevil.game.trackshooter.entity.powerup;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.movement.TravelRotateVelocityOnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.render.components.ImageRenderComponent;
import me.retrodaredevil.game.trackshooter.render.components.ShakeImageRenderComponent;
import me.retrodaredevil.game.trackshooter.util.EntityUtil;
import me.retrodaredevil.game.trackshooter.util.Resources;
import me.retrodaredevil.game.trackshooter.world.World;

public class Fruit extends SimplePowerup {

	private Resources.Points points;

	private boolean eaten = false;

	protected Fruit(Resources.Points points, float velocity, float startingTrackDistance){
		super();
		this.points = points;
		setHitboxSize(.6f);

		TravelRotateVelocityOnTrackMoveComponent trackMove = new TravelRotateVelocityOnTrackMoveComponent(this);
		trackMove.setDistanceOnTrack(startingTrackDistance);
		trackMove.getTravelVelocitySetter().setVelocity(velocity);
		setMoveComponent(trackMove);
	}
	public static Fruit createFruit(Resources.Points points, float startingTrackDistance, Image image){
		Fruit fruit = new Fruit(points, 1.5f, startingTrackDistance);
		ImageRenderComponent renderComponent = new ShakeImageRenderComponent(image,
				fruit, .8f, .8f, 250, new Vector2(0, .03f));
		renderComponent.setFacingDirection(0);
		fruit.setRenderComponent(renderComponent);
		return fruit;
	}

	@Override
	public void onHit(World world, Entity other)  {
		if(!(other instanceof Player)){
			eaten = true;
			return;
		}
		Player player = (Player) other;
		player.getScoreObject().onKill(this, player, points.getWorth());
		eaten = true;
		displayScore(world);
	}

	protected void displayScore(World world){
		EntityUtil.displayScore(world, this.getLocation(), points.getDrawable());
	}

	@Override
	public boolean shouldRemove(World world) {
		return super.shouldRemove(world) || eaten;
	}
}
