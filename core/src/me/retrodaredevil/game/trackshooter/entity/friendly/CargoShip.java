package me.retrodaredevil.game.trackshooter.entity.friendly;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import me.retrodaredevil.game.trackshooter.CollisionIdentity;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.SimpleEntity;
import me.retrodaredevil.game.trackshooter.entity.movement.MoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.OnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.TravelVelocityMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.TravelVelocityOnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.render.ImageRenderComponent;
import me.retrodaredevil.game.trackshooter.util.Resources;
import me.retrodaredevil.game.trackshooter.world.World;

/**
 * Please, call him "Mr. Spaceship"
 */
public class CargoShip extends SimpleEntity {

	private boolean hit = false;

	public CargoShip(float velocity, float distance){

		setHitboxSize(.50f);
		setMoveComponent(new TravelVelocityOnTrackMoveComponent(this){
			{
				getTravelVelocitySetter().setVelocity(velocity);
				setDistanceOnTrack(distance);
			}
		});
		canRespawn = false;
		canSetToRemove = true;
		collisionIdentity = CollisionIdentity.FRIENDLY_ALLY;
	}

	@Override
	public void beforeSpawn(World world) {
		super.beforeSpawn(world);
		setRenderComponent(new ImageRenderComponent(new Image(Resources.CARGO_SHIP), this, 1.0f, 1.0f));
	}

	@Override
	public void update(float delta, World world) {
		super.update(delta, world);
		MoveComponent moveComponent = getMoveComponent();
		if(moveComponent instanceof OnTrackMoveComponent){
			OnTrackMoveComponent trackMove = (OnTrackMoveComponent) moveComponent;
			float directionAdjust = 0;
			if(moveComponent instanceof TravelVelocityMoveComponent){
				TravelVelocityMoveComponent travelVelocityMoveComponent = (TravelVelocityMoveComponent) moveComponent;
				directionAdjust = travelVelocityMoveComponent.getTravelVelocity() < 0 ? 180 : 0;
			}
			this.setRotation(world.getTrack().getForwardDirection(trackMove.getDistanceOnTrack()) + directionAdjust);
		}
	}

	@Override
	public void onHit(World world, Entity other) {
		hit = true;
	}

	@Override
	public boolean shouldRemove(World world) {
		return super.shouldRemove(world) || hit;
	}
}
