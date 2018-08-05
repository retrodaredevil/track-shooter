package me.retrodaredevil.game.trackshooter.entity.enemies.snake;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import me.retrodaredevil.game.trackshooter.CollisionIdentity;
import me.retrodaredevil.game.trackshooter.effect.Effect;
import me.retrodaredevil.game.trackshooter.effect.TimedSpeedEffect;
import me.retrodaredevil.game.trackshooter.entity.DifficultEntity;
import me.retrodaredevil.game.trackshooter.entity.Enemy;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.EntityDifficulty;
import me.retrodaredevil.game.trackshooter.entity.SimpleEntity;
import me.retrodaredevil.game.trackshooter.entity.movement.MoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.RotationalVelocityMultiplierSetter;
import me.retrodaredevil.game.trackshooter.entity.movement.SmartSightMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.SmoothTravelMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.TravelVelocitySetter;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.render.ImageRenderComponent;
import me.retrodaredevil.game.trackshooter.util.CannotHitException;
import me.retrodaredevil.game.trackshooter.util.Resources;
import me.retrodaredevil.game.trackshooter.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * A SnakePart is an entity that is able to "follow" another SnakePart. If this SnakePart isn't following
 * another SnakePart, its behaviour will be different and its MoveComponent should be a SmoothTravelMoveComponent.
 * <br/><br/>
 * The SmoothTravelMoveComponent, by default does nothing and its travel speed and rotational speed are set
 * to 0. It is the job of the EntityController to change these values.
 * <br/><br/>
 * However, if mode of the Level is not
 * LevelMode.NORMAL, it is likely the the target has been set to (0, 0) (done automatically.) In this case
 * you should not change the target unless you are making sure it is (0, 0) since this SnakePart would need
 * to reset to its starting position.
 */
public class SnakePart extends SimpleEntity implements Enemy, DifficultEntity {
	private static final float HITBOX_SIZE_RATIO = .625f;
	private static final float DONE_GOING_TO_START_DISTANCE2 = 3 * 3;

	private final EntityDifficulty difficulty;
	private final MoveComponent returnToStart;
	private final ImageRenderComponent renderComponent;
	private SmartSightMoveComponent targetRotationCache = null;

	private SnakePart inFront = null; // the part in front of us
	private SnakePart behind = null;   // the part behind us

	private float followDistance;

	private boolean hit = false;



	public SnakePart(EntityDifficulty difficulty){
		this.difficulty = difficulty;
		this.returnToStart = new SmoothTravelMoveComponent(this, Vector2.Zero, 0, 0); // values that are 0 will be reset
		this.renderComponent = new ImageRenderComponent(new Image(Resources.SNAKE_PART_TEXTURE), this, 0, 0); // width and height will be changed later
		setRenderComponent(renderComponent);
		setSize(.4f, true);

		canRespawn = false;
		collisionIdentity = CollisionIdentity.ENEMY;
		canLevelEndWithEntityActive = false;

		this.follow(null);
	}

	/**
	 * Creates many SnakeParts to create a fully functioning snake but, however, it does not add the created SnakeParts
	 * to a world and does not give them an EntityController
	 * @param amount The amount of SnakeParts there should be
	 * @return A list of SnakeParts with a length of amount
	 */
	public static List<SnakePart> createSnake(int amount, EntityDifficulty difficulty){
		List<SnakePart> r = new ArrayList<>();

		SnakePart last = null;
		for(int i = 0; i < amount; i++){
			SnakePart part = new SnakePart(difficulty);
			part.follow(last);
			r.add(part);

			last = part;
		}

		return r;
	}

	/**
	 * NOTE: This is allowed to be called as many times as you want since it will be cached and is
	 * recommended to do so. (from an EntityController perspective)
	 * @param target The target to start targeting
	 */
	public void switchToSmartSight(Entity target){
		if(!isHead()){
			throw new UnsupportedOperationException("Only the head can use this method.");
		}
		if(targetRotationCache == null || targetRotationCache.getEntityTarget() != target){
			targetRotationCache = new SmartSightMoveComponent(this, target);
			updateSpeedAndRotation(null, targetRotationCache);
		}
		setMoveComponent(targetRotationCache);
	}


	public void setSize(float size, boolean applyToAll){
		if(followDistance == size){
			//  && (inFront == null || inFront.followDistance == size) && (behind == null || behind.followDistance == size)
			return;
		}
		final float hitboxSize = size * HITBOX_SIZE_RATIO;
		setHitboxSize(hitboxSize, hitboxSize);
		renderComponent.setSize(size, size);
		this.followDistance = size;
		if(applyToAll) {
			if (inFront != null) {
				inFront.setSize(size, true);
			}
			if (behind != null) {
				behind.setSize(size, true);
			}
		}
	}

	public float getFollowDistance(){
		return followDistance;
	}

	@Override
	public void addEffect(Effect effect) {
		if(!isHead()) {
			throw new UnsupportedOperationException("Cannot add an effect to body of snake. Must be head. If intended, remove these if and throw statements.");
		}
		super.addEffect(effect);
	}

	/** @return true if this SnakePart is the head of the snake */
	public boolean isHead(){
		return inFront == null;
	}

	/** @return The number of SnakeParts that are "behind" us or that are "following" us */
	public int getNumberBehind(){
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

	@Override
	public EntityDifficulty getDifficulty() {
		return difficulty;
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
			setMoveComponent(returnToStart);
			return;
		}
		if(inFront == snakePart){
			return;
		}

		this.inFront = snakePart;
		snakePart.leadPart(this); // the SnakePart in front of us should "lead" us
		setMoveComponent(new SnakeFollowMoveComponent(this, inFront, .5f));
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
	public void update(float delta, World world) {
		if(this.isHead()){
			// ==== Calculate velocity and size ====
			int numberParts = this.getNumberBehind() + 1; // add one because getNumberBehind() doesn't include head
			updateSize(numberParts);
			MoveComponent move = getMoveComponent();

			updateSpeedAndRotation(numberParts, move);
			if(move != returnToStart){
				updateSpeedAndRotation(numberParts, returnToStart); // update the speed of this just in case something else is using it
			}
//			System.out.println("I am a head: " + Integer.toHexString(this.hashCode()) + " move component: "
//					+ move.getClass().getSimpleName() + " entity controller: " + getEntityController().getClass().getSimpleName());

		} else {
			if(inFront != null && inFront.isRemoved()){
				System.err.println("We aren't a head but we should be!");
			}
		}
		super.update(delta, world);
	}
	private void updateSize(Integer numberParts){
		if(!this.isHead()){
			throw new UnsupportedOperationException("This method is only allowed to be called on a head SnakePart");
		}
		if(numberParts == null){
			numberParts = this.getNumberBehind() + 1;
		}
		float size = .4f;
		if(numberParts <= 10){
			size = .4f - (numberParts - 10) * .04f;
		}
		setSize(size, true);
	}

	/**
	 * @param numberParts The total number of parts or null if this method should calculate that itself.
	 * @param moveComponent The move component to set the rotational velocity of and of the travel velocity of
	 */
	private void updateSpeedAndRotation(Integer numberParts, MoveComponent moveComponent){
		if(!this.isHead()){
			throw new UnsupportedOperationException("This method is only allowed to be called on a head SnakePark");
		}
		final boolean canMultiplyRotation = moveComponent instanceof RotationalVelocityMultiplierSetter;
		final boolean canSetTravelSpeed = moveComponent instanceof TravelVelocitySetter;
		if(!canSetTravelSpeed && !canMultiplyRotation){
			return; // we can't do anything so why calculate anything
		}
		if(numberParts == null){
			numberParts = this.getNumberBehind() + 1;
		}
		float speed = 5;
		float rotMultiplier = 2;
		if (numberParts <= 10) {
			if (difficulty.value >= EntityDifficulty.NORMAL.value) {
				if (difficulty.value >= EntityDifficulty.HARD.value) {
					speed = 15 - numberParts;
				} else if (numberParts <= 5) {
					speed = 5 + (5 - numberParts) * .5f;
				}
				rotMultiplier = 4.5f - (numberParts * .25f);
			}
		}
		if(canMultiplyRotation) {
			((RotationalVelocityMultiplierSetter) moveComponent).setRotationalMultiplier(rotMultiplier);
		}
		if(canSetTravelSpeed){
			((TravelVelocitySetter) moveComponent).getTravelVelocitySetter().setVelocity(speed);
		}
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
		killAll(this, false, player, other);
		/* code for centipede like behaviour
		this.hit = true;
		if(player != null) {
			player.getScoreObject().onKill(this, other, 50);
		}
		leadPart(null);
		follow(null);
		*/
	}
	private static void killAll(final SnakePart start, boolean inFront, Player player, Entity cause){
		SnakePart part = start;
		while(part != null){
			part.hit = true; // kill everyone behind us including ourselves
			part = inFront ? part.inFront : part.behind;
			if(player != null){
				player.getScoreObject().onKill(part, cause, 50);
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
		if(isHead()) {
			setMoveComponent(returnToStart);
		}
	}

	@Override
	public boolean isGoingToStart() {
		return getLocation().dst2(Vector2.Zero) > DONE_GOING_TO_START_DISTANCE2;
	}

	@Override
	public void goNormalMode() {
		// let the AI control this
	}

}