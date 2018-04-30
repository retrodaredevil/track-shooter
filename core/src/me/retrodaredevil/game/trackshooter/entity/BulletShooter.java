package me.retrodaredevil.game.trackshooter.entity;

import me.retrodaredevil.game.trackshooter.world.World;

public interface BulletShooter extends Entity {

	/**
	 * Calling this method should add the returned Bullet Entity to the passed world by calling world.addEntity()
	 *
	 * @param world The World the returned Bullet will be added to
	 * @return The created Bullet or null if it can't shoot one at the moment
	 */
	Bullet shootBullet(World world);
}
