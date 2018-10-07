package me.retrodaredevil.game.trackshooter.entity.enemies.shark;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.Optional;

import me.retrodaredevil.game.trackshooter.CollisionIdentity;
import me.retrodaredevil.game.trackshooter.entity.*;
import me.retrodaredevil.game.trackshooter.entity.movement.*;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.level.LevelEndState;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;
import me.retrodaredevil.game.trackshooter.render.components.SharkRenderComponent;
import me.retrodaredevil.game.trackshooter.util.EntityUtil;
import me.retrodaredevil.game.trackshooter.util.Resources;
import me.retrodaredevil.game.trackshooter.world.World;

public class Shark extends SimpleEntity implements Enemy, Entity {
	private static final float VELOCITY_SPEED = 5; // units per second
	private static final float ROTATIONAL_SPEED = 2; // rotations per second

	private int lives = 3;
	private int spinLives = 40; // hitting many times while spinning is hard and is worth double points

	private final MoveComponent resetPosition; // when we call Shark#setMoveComponent(), make sure to setNextComponent(null):
	private final MoveComponent smoothTravel;

	private RenderComponent fullRender = null; // NOTE These will be disposed as the Shark loses lives
	private RenderComponent hitRender = null;
	private RenderComponent wornRender = null;

	public Shark(Vector2 startingPosition, float startingRotation){
		resetPosition = new SmoothResetPositionMoveComponent(this, startingPosition, startingRotation, VELOCITY_SPEED, ROTATIONAL_SPEED);
		smoothTravel = new SmoothTravelMoveComponent(this, new Vector2(0, 0), VELOCITY_SPEED, ROTATIONAL_SPEED);

		setHitboxSize(.7f);
		canRespawn = false;
		collisionIdentity = CollisionIdentity.ENEMY;
		levelEndStateWhenActive = LevelEndState.CANNOT_END;
	}

	@Override
	public void beforeSpawn(World world) {
		super.beforeSpawn(world);
		// TODO maybe make SharkRenderComponent more generic for other entities to use if needed and...\n
		// possibly only rely on one RenderComponent if that is more elegant
		fullRender = new SharkRenderComponent(Resources.Shark.FULL_HEALTH.getSprites(world.getRenderObject()), this, 1.0f, 1.0f);
		hitRender = new SharkRenderComponent(Resources.Shark.MIDDLE_HEALTH.getSprites(world.getRenderObject()), this, 1.0f, 1.0f);
		wornRender = new SharkRenderComponent(Resources.Shark.LOW_HEALTH.getSprites(world.getRenderObject()), this, 1.0f, 1.0f);
	}

	@Override
	public void update(float delta, World world) {
		super.update(delta, world);
		if(lives <= 1){
			setRenderComponent(wornRender);
		} else if(lives == 2){
			setRenderComponent(hitRender);
		} else {
			setRenderComponent(fullRender);
		}
	}

	@Override
	public void disposeRenderComponent() {
		fullRender.dispose();
		hitRender.dispose();
		wornRender.dispose();
	}

	@Override
	public void goToStart() {
		resetPosition.setNextComponent(null);
		MoveComponent moveComponent = getMoveComponent();
		if(moveComponent != null && moveComponent.canHaveNext()){
			moveComponent.setNextComponent(resetPosition);
		} else {
			setMoveComponent(resetPosition);
		}
	}

	@Override
	public void goNormalMode() {
		setMoveComponent(smoothTravel);

	}

	@Override
	public boolean isGoingToStart() {
		if(resetPosition.isActive()){
			return true;
		}
		MoveComponent component = getMoveComponent();
		if(component == null){
			return false;
		}
		if(component == resetPosition){ // can happen for one frame
			return true;
		}
		while(true){ // does one of the linked MoveComponents contain final variable "resetPosition"?
			component = component.getNextComponent();
			if(component == resetPosition){
				return true;
			} else if(component == null){
				break;
			}
		}
		return false;
	}

	@Override
	public void onHit(World world, Entity other)  {
		MoveComponent wantedComponent = getMoveComponent();
		boolean spinning = false;
		SpinMoveComponent lastSpin = null;
		if(wantedComponent instanceof SpinMoveComponent){
			spinning = true;
			lastSpin = (SpinMoveComponent) wantedComponent;
			wantedComponent = wantedComponent.getNextComponent();
			while(wantedComponent instanceof TimedMoveComponent){
				// if Shark is still and the player shoots them, we search until there are no more spins or waits.
				wantedComponent = wantedComponent.getNextComponent();
			}
		}
		SpinMoveComponent spin;
		long spinTime = (long) (1000 * (1.75f + .5f * MathUtils.random()));
		if(lastSpin != null){
//			assert spinning; // commented out because according to intellij, unnecessary check
			spin = new SpinMoveComponent(this, spinTime, lastSpin.getRotationalVelocity() * -1);
		} else {
			spin = new SpinMoveComponent(this, spinTime, 360 * 2 * MathUtils.randomSign());
		}
		spin.setNextComponent(wantedComponent);
		this.setMoveComponent(spin);
		if(spinning){
			spinLives--;
		} else {
			lives--;
		}
		boolean spinDeath = spinLives <= 0;
		if(lives <= 0 || spinDeath) {
			Resources.Points points = spinDeath ? Resources.Points.P400 : Resources.Points.P200;
			EntityUtil.displayScore(world, this.getLocation(), points.getDrawable(world.getRenderObject())); // display 200

			Player player = null;
			if (other instanceof Player) {
				player = (Player) other;
			} else {
				Entity shooter = other.getShooter();
				if (shooter instanceof Player) {
					player = (Player) shooter;
				}
			}
			if(player != null) {
				player.getScoreObject().onKill(this, other, points.getWorth());
			}
		}

	}


	@Override
	public boolean shouldRemove(World world) {
		return lives <= 0 || spinLives <= 0;
	}
}
