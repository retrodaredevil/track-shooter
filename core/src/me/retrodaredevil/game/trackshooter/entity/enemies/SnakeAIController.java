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
	private static final float MAX_AWAY = 12;

	private static final float VIEW_ANGLE = 90; // * 2 total

	private final SnakePart part;
	private final Entity target;
	private final Difficulty difficulty;
	public SnakeAIController(SnakePart part, Entity target, Difficulty difficulty){
		this.part = part;
		this.target = target;
		this.difficulty = difficulty;
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
				if(this.difficulty.value >= Difficulty.NORMAL.value){
					if(this.difficulty.value >= Difficulty.HARD.value) {
						speed = 15 - numberParts;
					} else if(numberParts <= 5){
						speed = 5 + (5 - numberParts) * .5f;
					}
					rotMultiplier = 4.5f - (numberParts * .25f);
				}
				size = .4f - (numberParts - 10) * .04f;
			}
			travelMove.setRotationalSpeedMultiplier(rotMultiplier);
			part.setSize(size, true);


			// ==== Calculate target ====
			float rotation = target.getRotation();
			float angle = part.getLocation().sub(target.getX(), target.getY()).angle();
			float angleAway = MathUtil.minDistance(rotation, angle, 360); // the amount of degrees the target is away from looking right at the head
			if(this.difficulty.value >= Difficulty.HARD.value && angleAway < 25){ // give speed boast to snake when player is looking right at it
				speed += (angleAway / 25) * 3;
			}
			if(angleAway < VIEW_ANGLE){ // the target is looking at the head
				float percent = (float) Math.pow((VIEW_ANGLE - angleAway) / VIEW_ANGLE, .5); // the lower the view angle, the higher this number
				Vector2 location = target.getLocation();
				Vector2 distantPoint = target.getLocation().add(
						MathUtils.cosDeg(angle) * MAX_AWAY,
						MathUtils.sinDeg(angle) * MAX_AWAY
				);
				location.lerp(distantPoint, percent); // location is mutated
				travelMove.setTarget(location);
			} else {
				travelMove.setTarget(target.getLocation().add(
						MathUtils.cosDeg(rotation) * IN_FRONT_DISTANCE,
						MathUtils.sinDeg(rotation) * IN_FRONT_DISTANCE
				));
			}

			travelMove.setVelocity(speed);
		}
	}

	public enum Difficulty{
		EASY(1), NORMAL(2), HARD(3), EXTREME(4);

		private int value;
		Difficulty(int value){
			this.value = value;
		}
	}
}
