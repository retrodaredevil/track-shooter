package me.retrodaredevil.game.trackshooter.entity.enemies;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import me.retrodaredevil.game.trackshooter.CollisionIdentity;
import me.retrodaredevil.game.trackshooter.entity.Enemy;
import me.retrodaredevil.game.trackshooter.entity.SimpleEntity;
import me.retrodaredevil.game.trackshooter.entity.movement.OnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.render.ImageRenderComponent;
import me.retrodaredevil.game.trackshooter.util.Resources;
import me.retrodaredevil.game.trackshooter.world.World;

public class Sniper extends SimpleEntity implements Enemy {

	public Sniper(){

		setHitboxSize(.5f, .5f);
		setRenderComponent(new ImageRenderComponent(new Image(Resources.SNIPER_TEXTURE), this, .6f, .6f));

		collisionIdentity = CollisionIdentity.ENEMY;
		canLevelEndWithEntityActive = false;
		canRespawn = false;
	}

	@Override
	public void beforeSpawn(World world) {
		super.beforeSpawn(world);

		OnTrackMoveComponent moveComponent = new OnTrackMoveComponent(this);
		moveComponent.setDistance(world.getTrack().getTotalDistance() / 2.0f);
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
