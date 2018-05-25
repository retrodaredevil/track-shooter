package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.math.Rectangle;
import me.retrodaredevil.game.trackshooter.entity.Bullet;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.Hittable;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.entity.powerup.Powerup;
import me.retrodaredevil.game.trackshooter.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollisionHandler implements Updateable {
//	private final List<Hittable> friendly = new LinkedList<>(), enemies = new LinkedList<>(),
//			friendBullets = new LinkedList<>(), enemyBullets = new LinkedList<>();
	@Override
	public void update(float delta, World world) {
//		long start = System.nanoTime();
//		friendly.clear();
//		enemies.clear();
//		friendBullets.clear();
//		enemyBullets.clear();
		final Collection<Entity> entities = world.getEntities();

		List<Hittable> friendly = new ArrayList<>(), enemies = new ArrayList<>(),
				friendBullets = new ArrayList<>(), enemyBullets = new ArrayList<>();

		for(Entity e : entities){
//			assert !e.shouldRemove(world); this happened once when eating fruit, unable to reproduce so commented out
			if(e instanceof Hittable) {
				Hittable h = (Hittable) e;
				if (e instanceof Player) {
					friendly.add(h);
				} else if (e instanceof Bullet) {
					if (e.getShooter() instanceof Player) {
						friendBullets.add(h);
					} else {
						enemyBullets.add(h);
					}
				} else {
					enemies.add(h);
				}
			}
		}
		if(!enemyBullets.isEmpty() || !enemies.isEmpty()) {
			for (Hittable friend : friendly) {
				if(friend.shouldRemove(world)){
					continue;
				}
				Rectangle hitbox = friend.getHitbox();
				for (Hittable enemyBullet : enemyBullets) {
					if(enemyBullet.shouldRemove(world)){
						continue;
					}
					if (hitbox.overlaps(enemyBullet.getHitbox())) { // for player - enemy bullet collisions
						friend.onHit(world, enemyBullet);
						enemyBullet.onHit(world, friend);
					}
				}
				for (Hittable enemy : enemies) {
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
			friendBulletLoop : for (Hittable friendBullet : friendBullets) {
				for (Hittable enemy : enemies) {
					if(friendBullet.shouldRemove(world)){
						continue friendBulletLoop;
					}
					if(enemy.shouldRemove(world) || enemy instanceof Powerup){
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
