package me.retrodaredevil.game.trackshooter.entity.enemies;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import me.retrodaredevil.game.trackshooter.CollisionIdentity;
import me.retrodaredevil.game.trackshooter.effect.Effect;
import me.retrodaredevil.game.trackshooter.effect.TimedSpeedEffect;
import me.retrodaredevil.game.trackshooter.entity.Enemy;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.SimpleEntity;
import me.retrodaredevil.game.trackshooter.entity.movement.SnakeFollowMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.SmoothTravelMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.render.ImageRenderComponent;
import me.retrodaredevil.game.trackshooter.util.CannotHitException;
import me.retrodaredevil.game.trackshooter.util.Resources;
import me.retrodaredevil.game.trackshooter.world.World;

import java.util.ArrayList;
import java.util.List;

public class SnakePart extends SimpleEntity implements Enemy {
	private static final float FOLLOW_DISTANCE = .4f;

	private SnakePart inFront = null; // the part in front of us
	private SnakePart behind = null;   // the part behind us
	private SnakeState state = null; // not null when inFront == null

	private boolean hit = false;

	private final SmoothTravelMoveComponent smoothMove; // used when isHead() == true

	public SnakePart(){
		this.smoothMove = new SmoothTravelMoveComponent(this, Vector2.Zero, 0, 0);
		// ^ some are 0 because the EntityController that the caller will attach to this will change the values
		setHitboxSize(.2f, .2f);
		setRenderComponent(new ImageRenderComponent(new Image(Resources.SNAKE_PART_TEXTURE), this, .4f, .4f));

		canRespawn = false;
		collisionIdentity = CollisionIdentity.ENEMY;
		canLevelEndWithEntityActive = false;

		this.follow(null);
	}

	@Override
	public void addEffect(Effect effect) {
		if(!isHead()) {
			throw new UnsupportedOperationException("Cannot add an effect to body of snake. Must be head.");
		}
		super.addEffect(effect);
	}

	/**
	 * Creates many SnakeParts to create a fully functioning snake but, however, it does not add the created SnakeParts
	 * to a world and does not give them an EntityController
	 * @param amount The amount of SnakeParts there should be
	 * @return A list of SnakeParts with a length of amount
	 */
	public static List<SnakePart> createSnake(int amount){
		List<SnakePart> r = new ArrayList<>();

		SnakePart last = null;
		for(int i = 0; i < 22; i++){
			SnakePart part = new SnakePart();
			part.follow(last);
			r.add(part);

			last = part;
		}

		return r;
	}

	/** @return true if this SnakePart is the head of the snake */
	public boolean isHead(){
		return inFront == null;
	}

	/** @return The number of SnakeParts that are "behind" us or that are "following" us */
	public int numberBehind(){
		int r = 0;
		SnakePart part = this;
		while(part != null){
			part = part.behind;
			r++;
		}
		return r;
	}
	public SnakePart getHead(){
		SnakePart head = this;
		while(head.inFront != null){
			head = head.inFront;
		}
		return head;
	}
	public SnakeState getState(){
		if(!isHead()){
			throw new UnsupportedOperationException("Only the head has the state of the snake");
		}
		if(state == null){
			throw new IllegalStateException("state should have been initialized");
		}
		return state;
	}

	/**
	 * If snakePart == null, this will become the head of the snake
	 * @param snakePart The SnakePart we want to follow (in front of us)
	 */
	public void follow(SnakePart snakePart){
		if(snakePart == null){
			if(this.inFront != null){
				SnakePart currentlyInFront = inFront;
				this.inFront = null;
				currentlyInFront.leadPart(null); // make sure the part we are inFront knows we are detaching
			}
			if(state == null){
				state = new SnakeState();
			}
			setMoveComponent(smoothMove);
			return;
		}
		if(inFront == snakePart){
			return;
		}

		this.inFront = snakePart;
		snakePart.leadPart(this); // the SnakePart in front of us should "lead" us
		setMoveComponent(new SnakeFollowMoveComponent(this, inFront, FOLLOW_DISTANCE, FOLLOW_DISTANCE * .5f));
	}

	/**
	 *
	 * @param snakePart The SnakePart we want to lead (behind us)
	 */
	protected void leadPart(SnakePart snakePart){
		if(snakePart == null){ // we aren't letting the SnakePart inFront us tag along anymore
			if(this.behind != null){
				SnakePart currentlyBehind = behind;
				this.behind = null;
				currentlyBehind.follow(null);
			}
			return;
		}
		if(behind == snakePart){
			return;
		}

		behind = snakePart;
		snakePart.follow(this); // this WON'T result in a stackoverflow because it should do nothing if this is already correct
	}

	@Override
	public void onHit(World world, Entity other) throws CannotHitException {
		Player player = null;
		if(other instanceof Player){
			player = (Player) other;
		} else {
			Entity shooter = other.getShooter();
			if(shooter instanceof Player){
				player = (Player) shooter;
			}
		}
		SnakePart head = getHead();
		head.addEffect(new TimedSpeedEffect(1000, 1.5f));
		SnakePart part = this;
		while(part != null){
			part.hit = true; // kill everyone behind us including ourselves
			part = part.behind;
			if(player != null){
				player.getScoreObject().onKill(part, other, 50);
			}
		}
	}

	@Override
	public boolean shouldRemove(World world) {
		return super.shouldRemove(world) || hit;
	}

	@Override
	public void afterRemove(World world) {
		super.afterRemove(world);
		leadPart(null);
		follow(null);
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

	public static final class SnakeState {
		private long invulnerableUntil = Long.MIN_VALUE;

		private SnakeState(){
		}

		public boolean isInvulnerable(){
			return invulnerableUntil > System.currentTimeMillis();
		}

	}
}
