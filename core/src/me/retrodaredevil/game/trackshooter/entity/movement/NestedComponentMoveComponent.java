package me.retrodaredevil.game.trackshooter.entity.movement;

import com.badlogic.gdx.Gdx;
import me.retrodaredevil.game.trackshooter.world.World;

/**
 * Handles a "nested" MoveComponent so an instance can have multiple behaviours similar to how an Entity has multiple
 * behaviours utilizing MoveComponents in the first place.
 * <p>
 * By extending this class, you are able to utilize a "nested" MoveComponent. This class handles updating
 * it and expects that the nested component is added in the overridden onStart() AFTER super.onStart() is called.
 * When this instance ends, it will remove the nested move component if there is any.
 * <p>
 * This class is not meant to be used as a substitute to extending a certain MoveComponent class,
 * it should be used to change MoveComponents on the fly while providing custom behaviour or
 * providing custom behaviour to a MoveComponent that's already initialized.
 * <p>
 * This also has the side affect that some instanceof calls will obviously return false unless
 * you implement particular interfaces. This is one of the reasons extending is better (if possible)
 */
public abstract class NestedComponentMoveComponent extends SimpleMoveComponent {
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
		if(getNestedMoveComponent() != null){
			Gdx.app.error("warning", getClass().getSimpleName() + " is starting when it already has a Nested Move Component. Just a warning.\n" +
					"It is recommended that the desired nested move component is added after super.onStart() is called or even after that in onUpdate().");
		}
	}

	@Override
	protected void onEnd() {
		setNestedMoveComponent(null);
	}

	@Override
	protected void onUpdate(float delta, World world) {
		if(nestedMoveComponent != null){
			nestedMoveComponent.update(delta, world);
		}
	}
}
