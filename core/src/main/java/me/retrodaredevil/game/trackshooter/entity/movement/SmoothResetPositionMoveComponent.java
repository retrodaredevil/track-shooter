package me.retrodaredevil.game.trackshooter.entity.movement;

import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.world.World;

public class SmoothResetPositionMoveComponent extends ResetPositionMoveComponent {

	private final DirectTravelMoveComponent directTravel;

	private float speed;
	private float rotationalSpeedMultiplier;

	public SmoothResetPositionMoveComponent(Entity entity, Vector2 startingPosition, float startingRotation,
	                                        float speed, float rotationalSpeedMultiplier) {
		super(entity, startingPosition, startingRotation);
		this.speed = speed;
		this.rotationalSpeedMultiplier = rotationalSpeedMultiplier;
		directTravel = new DirectTravelMoveComponent(entity, startingPosition, speed, startingRotation, 360);
	}

	@Override
	protected void onStart() {
		super.onStart();
		checkMoveComponent();
	}
	private void checkMoveComponent(){

		MoveComponent moveComponent = getNestedMoveComponent();

		Vector2 location = entity.getLocation();
		if (location.dst2(startingPosition) < 4) { // if it's less than 2 units away from the starting position
			if (!directTravel.isActive()){
				if(moveComponent != null && moveComponent.canHaveNext()) {
					moveComponent.setNextComponent(directTravel);
				} else {
					setNestedMoveComponent(directTravel);
				}
			}
			return;
		}
		if(moveComponent == null){
			setNestedMoveComponent(new SmoothTravelMoveComponent(entity, startingPosition, speed, rotationalSpeedMultiplier));
			return;
		}
		if (moveComponent instanceof TargetPositionMoveComponent) { // true when instanceof SmoothTravelMoveComponent (it's like this to be more generic)
			TargetPositionMoveComponent smoothTravel = (TargetPositionMoveComponent) moveComponent;
			smoothTravel.setTargetPosition(startingPosition);
		} else {
			System.err.println("Unexpected move component: " + moveComponent);
		}
	}

	@Override
	protected void onUpdate(float delta) {
		super.onUpdate(delta);
		checkMoveComponent();
	}
}
