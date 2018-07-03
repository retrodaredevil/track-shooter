package me.retrodaredevil.game.trackshooter.entity.enemies;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.EntityController;
import me.retrodaredevil.game.trackshooter.entity.movement.MoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.SmoothTravelMoveComponent;
import me.retrodaredevil.game.trackshooter.level.LevelMode;
import me.retrodaredevil.game.trackshooter.util.MathUtil;
import me.retrodaredevil.game.trackshooter.world.World;

public class SnakeAIController implements EntityController {
	private static final float IN_FRONT_DISTANCE = 5;
	private static final float MAX_AWAY = 10;
	private SnakePart part;
	private Entity target;
	public SnakeAIController(SnakePart part, Entity target){
		this.part = part;
		this.target = target;
	}
	@Override
	public void update(float delta, World world) {
		if(!part.isHead()){
			return;
		}
		if(world.getLevel().getMode() != LevelMode.NORMAL){ // we don't handle returning to normal position
			return;
		}
		MoveComponent moveComponent = part.getMoveComponent();
		if(moveComponent instanceof SmoothTravelMoveComponent){
			SmoothTravelMoveComponent travelMove = (SmoothTravelMoveComponent) moveComponent;

			// ==== Calculate velocity and size ====
			int numberParts = part.numberBehind() + 1; // add one because numberBehind() doesn't include head
			float speed = 5;
			float rotMultiplier = 2;
			float size = .4f;
			if(numberParts <= 10){
				speed = 15 - numberParts;
				rotMultiplier = 4.5f - (numberParts * .25f);
				size = .4f - (numberParts - 10) * .04f;
			}
			travelMove.setVelocity(speed);
			travelMove.setRotationalSpeedMultiplier(rotMultiplier);
			part.setSize(size, true);


			// ==== Calculate target ====
			float rotation = target.getRotation();
			float angle = part.getLocation().sub(target.getX(), target.getY()).angle();
			float angleAway = MathUtil.minDistance(rotation, angle, 360); // the amount of degrees the target is away from looking right at the head
			if(angleAway < 40){ // the target is looking at the head
				float percent = (40 - angleAway) / 40;
				Vector2 location = target.getLocation();
				Vector2 distantPoint = target.getLocation().add(
						MathUtils.cosDeg(rotation) * MAX_AWAY,
						MathUtils.sinDeg(rotation) * MAX_AWAY
				);
				location.lerp(distantPoint, percent); // location is mutated
				travelMove.setTarget(location);
			} else {
				travelMove.setTarget(target.getLocation().add(
						MathUtils.cosDeg(rotation) * IN_FRONT_DISTANCE,
						MathUtils.sinDeg(rotation) * IN_FRONT_DISTANCE
				));
			}
		}
	}
}
