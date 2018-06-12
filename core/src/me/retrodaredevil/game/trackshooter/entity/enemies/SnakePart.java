package me.retrodaredevil.game.trackshooter.entity.enemies;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import me.retrodaredevil.game.trackshooter.CollisionIdentity;
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

public class SnakePart extends SimpleEntity implements Enemy {

	private SnakePart inFront = null; // the part in front of us
	private SnakePart behind = null;   // the part behind us

	private boolean hit = false;

	private final SmoothTravelMoveComponent smoothMove; // used when isHead() == true

	public SnakePart(){
		this.smoothMove = new SmoothTravelMoveComponent(this, Vector2.Zero, 5, 1);
		setHitboxSize(.4f, .4f);
		setRenderComponent(new ImageRenderComponent(new Image(Resources.SNAKE_PART_TEXTURE), this, .5f, .5f));

		canRespawn = false;
		collisionIdentity = CollisionIdentity.ENEMY;

		this.follow(null);
	}
	public boolean isHead(){
		return inFront == null;
	}
	public SnakePart getHead(){
		SnakePart head = this;
		while(head.inFront != null){
			head = head.inFront;
		}
		return head;
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
			setMoveComponent(smoothMove);
			return;
		}
		if(inFront == snakePart){
			return;
		}

		this.inFront = snakePart;
		snakePart.leadPart(this); // the SnakePart in front of us should "lead" us
		setMoveComponent(new SnakeFollowMoveComponent(this, inFront, .5f, .25f));
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
