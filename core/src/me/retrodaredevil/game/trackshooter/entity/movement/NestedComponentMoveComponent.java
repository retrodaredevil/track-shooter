package me.retrodaredevil.game.trackshooter.entity.movement;

import me.retrodaredevil.game.trackshooter.world.World;

/**
 * Handles a "nested" MoveComponent so an instance can have multiple behaviours similar to how an Entity has multiple
 * behaviours utilizing MoveComponents in the first place.
 */
abstract class NestedComponentMoveComponent extends SimpleMoveComponent {
	private MoveComponent nestedMoveComponent;

	protected NestedComponentMoveComponent(MoveComponent nextComponent, boolean canHaveNext, boolean canRecycle) {
		super(nextComponent, canHaveNext, canRecycle);
	}

	public MoveComponent getNestedMoveComponent(){
		return nestedMoveComponent;
	}
	protected void setNestedMoveComponent(MoveComponent moveComponent){
		if(this.nestedMoveComponent != moveComponent && this.nestedMoveComponent != null){
			this.nestedMoveComponent.end();
		}
		this.nestedMoveComponent = moveComponent;
	}

	@Override
	protected void onStart(World world) {
	}

	@Override
	protected void onEnd() {
	}

	@Override
	protected void onUpdate(float delta, World world) {
		if(nestedMoveComponent != null){
			nestedMoveComponent.update(delta, world);
		}
	}
}
