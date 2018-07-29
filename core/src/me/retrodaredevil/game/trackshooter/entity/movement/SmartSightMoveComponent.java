package me.retrodaredevil.game.trackshooter.entity.movement;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import me.retrodaredevil.game.trackshooter.entity.DifficultEntity;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.EntityDifficulty;
import me.retrodaredevil.game.trackshooter.level.LevelMode;
import me.retrodaredevil.game.trackshooter.util.MathUtil;
import me.retrodaredevil.game.trackshooter.world.World;

public class SmartSightMoveComponent extends SmoothTravelMoveComponent {

	private static final float IN_FRONT_DISTANCE = 5;
	private static final float MAX_AWAY = 12;

	private static final float VIEW_ANGLE = 90; // * 2 total


	private final DifficultEntity entity;
	private final Entity target;
	public SmartSightMoveComponent(DifficultEntity entity, Entity target) {
		super(entity, Vector2.Zero, 0, 0);
		this.entity = entity;
		this.target = target;
	}

	public Entity getEntityTarget(){
		return target;
	}

	@Override
	protected void onUpdate(float delta, World world) {
//		if(!entity.isHead()){
//			throw new UnsupportedOperationException(getClass().getSimpleName() + " doesn't support non-head SnakeParts");
//		}
//		System.out.println("updating TargetRotation with velocity: " + getTravelVelocity());

		if(world.getLevel().getMode() != LevelMode.NORMAL){ // we don't handle returning to normal position
			System.err.println(getClass().getSimpleName() + " is active in mode: " + world.getLevel().getMode());
		}
		doTarget();
		super.onUpdate(delta, world);
	}


	/**
	 * Sets the target on the smoothTravel and possibly increases the travel speed of passed smoothTravel
	 */
	private void doTarget(){
		EntityDifficulty difficulty = entity.getDifficulty();

		// ==== Calculate target ====
		float rotation = target.getRotation();
		float angle = entity.getLocation().sub(target.getX(), target.getY()).angle();
		float angleAway = MathUtil.minDistance(rotation, angle, 360); // the amount of degrees the target is away from looking right at the head
		if(difficulty.value >= EntityDifficulty.HARD.value && angleAway < 25){ // give speed boast to snake when player is looking right at it
			float speed = this.getTravelVelocity();
			speed += (angleAway / 25) * 3;
			getTravelVelocitySetter().setVelocity(speed);
		}
		if(angleAway < VIEW_ANGLE){ // the target is looking at the head
			float percent = (float) Math.pow((VIEW_ANGLE - angleAway) / VIEW_ANGLE, .5); // the lower the view angle, the higher this number
			Vector2 location = target.getLocation();
			Vector2 distantPoint = target.getLocation().add(
					MathUtils.cosDeg(angle) * MAX_AWAY,
					MathUtils.sinDeg(angle) * MAX_AWAY
			);
			location.lerp(distantPoint, percent); // location is mutated
			this.setTargetPosition(location);
		} else {
			this.setTargetPosition(target.getLocation().add(
					MathUtils.cosDeg(rotation) * IN_FRONT_DISTANCE,
					MathUtils.sinDeg(rotation) * IN_FRONT_DISTANCE
			));
		}

	}
}
