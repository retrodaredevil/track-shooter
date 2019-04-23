package me.retrodaredevil.game.trackshooter.entity.enemies.shark;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

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

	private final MoveComponent smoothTravel;
	private final MoveComponent resetPosition; // when we call Shark#setMoveComponent(), make sure to setNextComponent(null):
	private final float waitBeforeMoveTime;

	private int lives = 3;
	private int spinLives = 40; // hitting many times while spinning is hard and is worth double points

	private RenderComponent fullRender = null; // NOTE These will be disposed as the Shark loses lives
	private RenderComponent hitRender = null;
	private RenderComponent wornRender = null;

	public Shark(World world, Vector2 startingPosition, float startingRotation, float waitBeforeMoveTime){
		super(world);
		this.waitBeforeMoveTime = waitBeforeMoveTime;
		smoothTravel = new SmoothTravelMoveComponent(this, new Vector2(0, 0), VELOCITY_SPEED, ROTATIONAL_SPEED);
		resetPosition = new SmoothResetPositionMoveComponent(this, startingPosition, startingRotation, VELOCITY_SPEED, ROTATIONAL_SPEED);

		setHitboxSize(.7f);
		canRespawn = false;
		collisionIdentity = CollisionIdentity.ENEMY;
		levelEndStateWhenActive = LevelEndState.CANNOT_END;
	}

	@Override
	public void beforeSpawn() {
		super.beforeSpawn();
		// TODO maybe make SharkRenderComponent more generic for other entities to use if needed and...\n
		// possibly only rely on one RenderComponent if that is more elegant
		fullRender = new SharkRenderComponent(world.getMainStage(), Resources.Shark.FULL_HEALTH.getSprites(world.getRenderObject()), this, 1.0f, 1.0f);
		hitRender = new SharkRenderComponent(world.getMainStage(), Resources.Shark.MIDDLE_HEALTH.getSprites(world.getRenderObject()), this, 1.0f, 1.0f);
		wornRender = new SharkRenderComponent(world.getMainStage(), Resources.Shark.LOW_HEALTH.getSprites(world.getRenderObject()), this, 1.0f, 1.0f);
	}

	@Override
	public void update(float delta) {
		super.update(delta);
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
		float multiplier = 1;
		if(lives == 1){
			multiplier = 0;
		} else if(lives == 2){
			multiplier = .25f;
		}
		setMoveComponent(new TimedMoveComponent(world, waitBeforeMoveTime * multiplier, smoothTravel));

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
	public void onHit(Entity other)  {
		MoveComponent wantedComponent = getMoveComponent();
		SpinMoveComponent lastSpin = null;
		if(wantedComponent instanceof SpinMoveComponent){ // current component is spinning
			lastSpin = (SpinMoveComponent) wantedComponent;
			wantedComponent = wantedComponent.getNextComponent();
		}
		while(wantedComponent instanceof TimedMoveComponent){
			// if Shark is still and the player shoots them, we search until there are no more spins or waits.
			wantedComponent = wantedComponent.getNextComponent();
		}
		SpinMoveComponent spin;
		float spinTime =  1.75f + .5f * MathUtils.random();
		if(lastSpin != null){
			spin = new SpinMoveComponent(world, spinTime, this, lastSpin.getRotationalVelocity() * -1);
		} else {
			spin = new SpinMoveComponent(world, spinTime, this, 360 * 2 * MathUtils.randomSign());
		}
		spin.setNextComponent(wantedComponent);
		this.setMoveComponent(spin);
		if(lastSpin != null){ // we are spinning
			spinLives--;
		} else {
			lives--;
		}
		final boolean spinDeath = spinLives <= 0;
		Player player = null;
		if (other instanceof Player) {
			player = (Player) other;
		} else {
			Entity shooter = other.getShooter();
			if (shooter instanceof Player) {
				player = (Player) shooter;
			}
		}
		if(lives <= 0 || spinDeath) {
			Resources.Points points = spinDeath ? Resources.Points.P400 : Resources.Points.P200;
			EntityUtil.displayScore(world, this.getLocation(), points.getDrawable(world.getRenderObject())); // display points

			if(player != null) {
				player.getScoreObject().onKill(this, other, points.getWorth());
			}
		} else if (lastSpin == null){ // their lives counter went down
			if(player != null){
				player.getScoreObject().onScore(lives == 2 ? 10 : 30);
			}
		}

	}


	@Override
	public boolean shouldRemove() {
		return lives <= 0 || spinLives <= 0;
	}
}
