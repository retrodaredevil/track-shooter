package me.retrodaredevil.game.trackshooter.entity;

import me.retrodaredevil.game.trackshooter.world.World;

import java.util.List;

public interface BulletShooter extends Entity {

	/**
	 * Calling this method should add the returned Bullet Entity to the passed world by calling world.addEntity()
	 *
	 * @param world The World the returned Bullet will be added to
	 * @return The created Bullet
	 */
	List<Bullet> shootBullet(World world);

	/**
	 * @param world The World that the bullet might be shot in
	 * @return true if this entity can shoot a bullet
	 */
	boolean canShootBullet(World world);

	void setShotType(Bullet.ShotType shotType);
	Bullet.ShotType getShotType();

}
