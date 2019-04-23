package me.retrodaredevil.game.trackshooter.entity.enemies;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import me.retrodaredevil.game.trackshooter.CollisionIdentity;
import me.retrodaredevil.game.trackshooter.entity.Enemy;
import me.retrodaredevil.game.trackshooter.entity.SimpleEntity;
import me.retrodaredevil.game.trackshooter.entity.movement.OnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.TravelRotateVelocityOnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.level.LevelEndState;
import me.retrodaredevil.game.trackshooter.render.components.ImageRenderComponent;
import me.retrodaredevil.game.trackshooter.world.World;

public class Sniper extends SimpleEntity implements Enemy {

	public Sniper(World world){
		super(world);
		setHitboxSize(.5f, .5f);

		collisionIdentity = CollisionIdentity.ENEMY;
//		canLevelEndWithEntityActive = false;
		levelEndStateWhenActive = LevelEndState.CANNOT_END;
		canRespawn = false;
	}

	@Override
	public void beforeSpawn() {
		super.beforeSpawn();
		setRenderComponent(new ImageRenderComponent(world.getMainStage(), new Image(world.getMainSkin().getDrawable("sniper")), this, .6f, .6f));

		OnTrackMoveComponent moveComponent = new TravelRotateVelocityOnTrackMoveComponent(world, this);
		moveComponent.setDistanceOnTrack(world.getTrack().getTotalDistance() / 2.0f);
		setMoveComponent(moveComponent);
	}

	@Override
	public void goToStart() {

	}

	@Override
	public boolean isGoingToStart() {
		return false;
	}

	@Override
	public void goNormalMode() {

	}
}
