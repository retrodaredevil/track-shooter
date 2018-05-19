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
	protected void onStart(World world) {
		super.onStart(world);
		setNestedMoveComponent(new SmoothTravelMoveComponent(entity, startingPosition, speed, rotationalSpeedMultiplier));
//		System.out.println("starting for: " + entity);
	}

	@Override
	protected void onUpdate(float delta, World world) {
		super.onUpdate(delta, world);
//		Gdx.app.debug("smooth reset frame", "" + Gdx.graphics.getFrameId());
		MoveComponent moveComponent = getNestedMoveComponent();
		assert moveComponent != null;
//		Gdx.app.debug("moveComponent", "" + moveComponent.hashCode());

		Vector2 location = entity.getLocation();

		if (moveComponent instanceof SmoothTravelMoveComponent) {
			SmoothTravelMoveComponent smoothTravel = (SmoothTravelMoveComponent) moveComponent;
			smoothTravel.setTarget(startingPosition);
//			System.out.println("setting startingPosition");
		}
		if (location.dst2(startingPosition) < 4) { // if it's less than 2 units away from the starting position
			if (!directTravel.isActive()){
//				System.out.println("set to directTravel.");
				if(moveComponent.canHaveNext()) {
					moveComponent.setNextComponent(directTravel);
				} else {
					setNestedMoveComponent(directTravel);
				}
			}
		}
	}
}
