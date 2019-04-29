package me.retrodaredevil.game.trackshooter.entity.enemies;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import me.retrodaredevil.game.trackshooter.CollisionIdentity;
import me.retrodaredevil.game.trackshooter.achievement.AchievementHandler;
import me.retrodaredevil.game.trackshooter.achievement.implementations.DefaultGameEvent;
import me.retrodaredevil.game.trackshooter.entity.Enemy;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.SimpleEntity;
import me.retrodaredevil.game.trackshooter.entity.movement.TravelVelocityOnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.level.LevelEndState;
import me.retrodaredevil.game.trackshooter.render.components.ImageRenderComponent;
import me.retrodaredevil.game.trackshooter.world.World;

public class StarFish extends SimpleEntity implements Enemy {
	/** Cannot flip direction twice within this time. (In seconds)*/
	private static final float NO_FLIP_TIME = .5f;

	private final TravelVelocityOnTrackMoveComponent moveComponent;
	private final float speed;
	private final AchievementHandler achievementHandler;
	private Float lastFlip = null;
	private boolean remove;

	/**
	 * @param speed The speed of the starfish where the sign doesn't matter
	 * @param startingDistance The starting distance of this starfish
	 * @param achievementHandler
	 */
	public StarFish(World world, float speed, float startingDistance, AchievementHandler achievementHandler){
		super(world);
		this.speed = speed;
		moveComponent = new TravelVelocityOnTrackMoveComponent(world, this);
		this.achievementHandler = achievementHandler;
		moveComponent.setDistanceOnTrack(startingDistance);
		setMoveComponent(moveComponent);
		setHitboxSize(.5f);

		canSetToRemove = true; // so the level can remove it
		levelEndStateWhenActive = LevelEndState.CAN_END; // default value // this line is just to be more explicit
		collisionIdentity = CollisionIdentity.ENEMY_EATER; // enemy and eats fruit
		canRespawn = false;
	}

	@Override
	public void beforeSpawn() {
		super.beforeSpawn();
		moveComponent.getTravelVelocitySetter().setVelocity(MathUtils.randomSign() * speed);
		ImageRenderComponent renderComponent = new ImageRenderComponent(new Image(world.getMainSkin().getDrawable("starfish")), this, .6f, .6f);
		renderComponent.setFacingDirection(0);
		setRenderComponent(renderComponent);
//		moveComponent.setDistanceOnTrack(world.getTrack().getTotalDistance() * MathUtils.random());

	}

	@Override
	public void onHit(Entity other) {
		flipDirection();
	}

	private void flipDirection(){
		final float time = world.getTime();
		if(lastFlip == null || time - lastFlip > NO_FLIP_TIME) {
			moveComponent.getTravelVelocitySetter().setVelocity(-1 * moveComponent.getTravelVelocity());
			lastFlip = time;
			achievementHandler.incrementIfSupported(DefaultGameEvent.REDIRECT_STARFISH, 1);
		}
	}

	@Override
	public boolean shouldRemove() {
		return super.shouldRemove() || remove;
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
