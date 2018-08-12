package me.retrodaredevil.game.trackshooter.util;

import me.retrodaredevil.game.trackshooter.entity.Entity;

/**
 * Should be thrown when a Hittable has their onHit method called and they don't know how to react to it
 */
public class CannotHitException extends RuntimeException {

	public CannotHitException(Entity other, Entity self, String message){
		super("Entity: " + other + " cannot hit this. (" + self + ") reason: '" + message + "'");
	}
	public CannotHitException(Entity other, Entity self){
		this(other, self, "no reason provided");
	}

}
