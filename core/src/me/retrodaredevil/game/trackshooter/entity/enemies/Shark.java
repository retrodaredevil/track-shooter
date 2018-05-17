package me.retrodaredevil.game.trackshooter.entity.enemies;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.entity.*;
import me.retrodaredevil.game.trackshooter.entity.movement.*;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.render.SharkRenderComponent;
import me.retrodaredevil.game.trackshooter.util.CannotHitException;
import me.retrodaredevil.game.trackshooter.util.EntityUtil;
import me.retrodaredevil.game.trackshooter.util.MathUtil;
import me.retrodaredevil.game.trackshooter.util.Resources;
import me.retrodaredevil.game.trackshooter.world.World;

public class Shark extends SimpleEntity implements Hittable, Enemy {
	private static final int POINTS = 200; // 200 points for killing a Shark
	private static final float VELOCITY_SPEED = 5; // units per second

	private int lives = 3;
	private int spinLives = 40; // hitting many times while spinning is hard and is worth double points

	private final Vector2 startingPosition;
	private final float startingRotation;

	public Shark(long pause, Vector2 startingPosition, float startingRotation){
		this.startingPosition = startingPosition.cpy();
		this.startingRotation = startingRotation;
		setRenderComponent(new SharkRenderComponent(Resources.SHARK_REGIONS, this, 1.0f, 1.0f));
		setMoveComponent(new TimedMoveComponent(pause, new SmoothTravelMoveComponent(this, new Vector2(0, 0), VELOCITY_SPEED, 2)));
		setHitboxSize(.7f, .7f);
	}

	@Override
	public boolean goToStart() {
		Vector2 location = getLocation();
		if(MathUtil.minDistance(startingRotation, getRotation(), 360) < 1 && location.epsilonEquals(startingPosition)){
			setLocation(startingPosition);
			setRotation(startingRotation);
			setMoveComponent(null);
			return true;
		}
		MoveComponent moveComponent = getMoveComponent();
		if(moveComponent == null){
			setMoveComponent(createDirectTravel());
		} else {
			if (moveComponent instanceof SmoothTravelMoveComponent) {
				SmoothTravelMoveComponent smoothTravel = (SmoothTravelMoveComponent) moveComponent;
				smoothTravel.setTarget(startingPosition);
			}
			if (location.dst2(startingPosition) < 4) { // if it's less than 2 units away from the starting position
				if (!(moveComponent instanceof DirectTravelMoveComponent) && !(moveComponent.getNextComponent() instanceof DirectTravelMoveComponent)) {
					moveComponent.setNextComponent(createDirectTravel());
				}
			}
		}
		return false;
	}
	private MoveComponent createDirectTravel(){
		return new DirectTravelMoveComponent(this, startingPosition, VELOCITY_SPEED, startingRotation, 360);
	}

	@Override
	public void onHit(World world, Entity other) throws CannotHitException {
		if(other.getShooter() == this || (!(other instanceof Bullet) && !(other instanceof Player))){
			throw new CannotHitException(other, this);
		}
//		Gdx.app.debug("hit by", other.toString() + " at " + Gdx.app.getGraphics().getFrameId());
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
//			assert spinning;
			spin = new SpinMoveComponent(this, spinTime, lastSpin.getSpinPerSecond() * -1);
		} else {
			spin = new SpinMoveComponent(this, spinTime, 360 * 2 * (MathUtils.randomBoolean() ? 1 : -1));
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
			float multiplier = spinDeath ? 2 : 1;
			EntityUtil.displayScore(world, this.getLocation(), Resources.EVEN_BONUS_REGIONS[spinDeath ? 1 : 0]); // display 200

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
				player.getScoreObject().onKill(this, other, (int) (POINTS * multiplier));
			}
		}

	}


	@Override
	public boolean shouldRemove(World world) {
		return lives <= 0 || spinLives <= 0;
	}
}
