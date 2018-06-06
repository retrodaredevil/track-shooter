package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.math.Rectangle;
import me.retrodaredevil.game.trackshooter.entity.Bullet;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.entity.powerup.PowerupEntity;
import me.retrodaredevil.game.trackshooter.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollisionHandler implements Updateable {
	/*
	Future references if we change how collision handling works:
	 * https://stackoverflow.com/a/45142822/5434860 good answer on how he dealt with collisions
	 * https://gamedev.stackexchange.com/a/137345 a visitor pattern (lots of methods)
	 */

//	private final List<Entity> friendly = new LinkedList<>(), enemies = new LinkedList<>(),
//			friendBullets = new LinkedList<>(), enemyBullets = new LinkedList<>();
	@Override
	public void update(float delta, World world) {
//		long start = System.nanoTime();
//		friendly.clear();
//		enemies.clear();
//		friendBullets.clear();
//		enemyBullets.clear();
		final Collection<Entity> entities = world.getEntities();

		List<Entity> friendly = new ArrayList<>(), enemies = new ArrayList<>(),
				friendBullets = new ArrayList<>(), enemyBullets = new ArrayList<>();

		for(Entity e : entities){
//			assert !e.shouldRemove(world); this happened once when eating fruit, unable to reproduce so commented out
			CollisionIdentity identity = e.getCollisionIdentity();
			if(identity.canCollide()) {
				if (identity == CollisionIdentity.FRIENDLY) {
					friendly.add(e);
				} else if (identity == CollisionIdentity.FRIENDLY_PROJECTILE) {
					friendBullets.add(e);
				} else if (identity == CollisionIdentity.ENEMY_PROJECTILE || identity == CollisionIdentity.POWERUP) {
					enemyBullets.add(e);
				} else {
					assert identity == CollisionIdentity.ENEMY : "Unknown CollisionIdentity that isn't UNKNOWN: " + identity;
					enemies.add(e);
				}
			}
		}
		if(!enemyBullets.isEmpty() || !enemies.isEmpty()) {
			for (Entity friend : friendly) {
				if(friend.shouldRemove(world)){
					continue;
				}
				Rectangle hitbox = friend.getHitbox();
				for (Entity enemyBullet : enemyBullets) {
					if(enemyBullet.shouldRemove(world)){
						continue;
					}
					if (hitbox.overlaps(enemyBullet.getHitbox())) { // for player - enemy bullet collisions
						friend.onHit(world, enemyBullet);
						enemyBullet.onHit(world, friend);
					}
				}
				for (Entity enemy : enemies) {
					if(enemy.shouldRemove(world)){
						continue;
					}
					Rectangle enemyHitbox = enemy.getHitbox();
					if (hitbox.overlaps(enemyHitbox)) { // for player - enemy collisions
						friend.onHit(world, enemy);
						enemy.onHit(world, friend);
					}
				}
			}
		}
		if(!enemies.isEmpty()) {
			friendBulletLoop : for (Entity friendBullet : friendBullets) {
				for (Entity enemy : enemies) {
					if(friendBullet.shouldRemove(world)){
						continue friendBulletLoop;
					}
					if(enemy.shouldRemove(world)){
						continue;
					}
					if (enemy.getHitbox().overlaps(friendBullet.getHitbox())) { // for player bullet - enemy collisions
						enemy.onHit(world, friendBullet);
						friendBullet.onHit(world, enemy);
					}
				}
			}
		}
//		long end = System.nanoTime();
//		long took = end - start;
//		Gdx.app.debug("took", "" + took);

	}
}
