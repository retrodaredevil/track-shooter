package me.retrodaredevil.game.trackshooter.entity.enemies;

import com.badlogic.gdx.math.MathUtils;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.EntityController;
import me.retrodaredevil.game.trackshooter.entity.movement.MoveComponent;
import me.retrodaredevil.game.trackshooter.entity.movement.SmoothTravelMoveComponent;
import me.retrodaredevil.game.trackshooter.world.World;

public class SnakeAIController implements EntityController {
	private static final float IN_FRONT_DISTANCE = 5;
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
		MoveComponent moveComponent = part.getMoveComponent();
		if(moveComponent instanceof SmoothTravelMoveComponent){
			SmoothTravelMoveComponent travelMove = (SmoothTravelMoveComponent) moveComponent;
			float rotation = target.getRotation();
			travelMove.setTarget(target.getLocation().add(
					MathUtils.cosDeg(rotation) * IN_FRONT_DISTANCE,
					MathUtils.sinDeg(rotation) * IN_FRONT_DISTANCE
			));
			int numberParts = part.numberBehind() + 1; // add one because numberBehind() doesn't include part
//			Gdx.app.debug("parts", "" + numberParts);
			float speed = 5;
			float rotMultiplier = 2;
			if(numberParts <= 10){
				speed = 15 - numberParts;
				rotMultiplier = 4.5f - (numberParts * .25f);
			}
			travelMove.setVelocity(speed);
			travelMove.setRotationalSpeedMultiplier(rotMultiplier);
		}
	}
}
