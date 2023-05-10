package me.retrodaredevil.game.trackshooter.entity;

import me.retrodaredevil.game.trackshooter.world.World;

/**
 * Should be used for entities that stay on the screen for a limited amount of time
 *
 * Should not be referenced using instanceof, should just be inherited
 */
public class TimedEntity extends SimpleEntity {

	private Float startTime = null;
	private final float time;

	TimedEntity(World world, float time){
		super(world);
		this.time = time;
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		if(startTime == null){
			startTime = world.getTime();
		}
	}

	@Override
	public boolean shouldRemove() {
		return startTime != null && startTime + time <= world.getTime();
	}
}
