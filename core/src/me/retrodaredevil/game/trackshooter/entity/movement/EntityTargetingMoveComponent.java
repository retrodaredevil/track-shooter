package me.retrodaredevil.game.trackshooter.entity.movement;

import me.retrodaredevil.game.trackshooter.entity.Entity;

/**
 * A MoveComponent that is targeting a certain Entity.
 */
public interface EntityTargetingMoveComponent extends MoveComponent{

	Entity getEntityTarget();
}
