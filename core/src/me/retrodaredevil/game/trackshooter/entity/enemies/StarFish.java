package me.retrodaredevil.game.trackshooter.entity.enemies;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import me.retrodaredevil.game.trackshooter.CollisionIdentity;
import me.retrodaredevil.game.trackshooter.entity.Enemy;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.SimpleEntity;
import me.retrodaredevil.game.trackshooter.entity.movement.TravelVelocityOnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.level.LevelEndState;
import me.retrodaredevil.game.trackshooter.render.components.ImageRenderComponent;
import me.retrodaredevil.game.trackshooter.world.World;

public class StarFish extends SimpleEntity implements Enemy {

	private final TravelVelocityOnTrackMoveComponent moveComponent;
	private final float speed;
	private boolean remove;

	/**
	 * @param speed The speed of the starfish where the sign doesn't matter
	 * @param startingDistance The starting distance of this starfish
	 */
	public StarFish(float speed, float startingDistance){
		this.speed = speed;
		moveComponent = new TravelVelocityOnTrackMoveComponent(this);
		moveComponent.setDistanceOnTrack(startingDistance);
		setMoveComponent(moveComponent);
		setHitboxSize(.5f);

		canSetToRemove = true; // so the level can remove it
		levelEndStateWhenActive = LevelEndState.CAN_END; // default value // this line is just to be more explicit
		collisionIdentity = CollisionIdentity.ENEMY_EATER; // enemy and eats fruit
		canRespawn = false;
	}

	@Override
	public void beforeSpawn(World world) {
		super.beforeSpawn(world);
		moveComponent.getTravelVelocitySetter().setVelocity(MathUtils.randomSign() * speed);
		ImageRenderComponent renderComponent = new ImageRenderComponent(new Image(world.getMainSkin().getDrawable("starfish")), this, .6f, .6f);
		renderComponent.setFacingDirection(0);
		setRenderComponent(renderComponent);
//		moveComponent.setDistanceOnTrack(world.getTrack().getTotalDistance() * MathUtils.random());

	}

	@Override
	public void onHit(World world, Entity other) {
		flipDirection();
	}

	private void flipDirection(){
		moveComponent.getTravelVelocitySetter().setVelocity(-1 * moveComponent.getTravelVelocity());
	}

	@Override
	public boolean shouldRemove(World world) {
		return super.shouldRemove(world) || remove;
	}

	@Override
	public void goToStart() {
		remove = true;
	}

	@Override
	public boolean isGoingToStart() {
		return true; // don't start normal mode unless this is removed
	}

	@Override
	public void goNormalMode() {
	}
}
