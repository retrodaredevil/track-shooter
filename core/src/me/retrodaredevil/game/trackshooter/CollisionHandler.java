package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import me.retrodaredevil.game.trackshooter.entity.Bullet;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.entity.powerup.PowerupEntity;
import me.retrodaredevil.game.trackshooter.world.World;

import java.util.*;

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
		long start = System.nanoTime();
		final Collection<Entity> entities = world.getEntities();

		List<Entity> possiblyCollides = new ArrayList<>();

		// This map represents all the entities that should be checked for each entity of a certain CollisionIdentity
		Map<CollisionIdentity, List<Entity>> collisionMap = new HashMap<>();
		for(Entity e : entities){
			CollisionIdentity collisionIdentity = e.getCollisionIdentity();
			if(!collisionIdentity.canCollide()){
				continue;
			}
			possiblyCollides.add(e);

			for(CollisionIdentity element : CollisionIdentity.values()){

				if(collisionIdentity.triggersCollision(element)){
					List<Entity> collisionList = collisionMap.get(element);
					if(collisionList == null){
						collisionList = new ArrayList<>();
					}
					collisionList.add(e);
					collisionMap.put(element, collisionList);
				}

			}
		}
		outerLoop : for(Entity e : possiblyCollides){
			List<Entity> collidesWith = collisionMap.get(e.getCollisionIdentity());
			if(collidesWith != null){
				for(Entity test : collidesWith){
					if(e.shouldRemove(world)){
						continue outerLoop;
					}
					if(test.shouldRemove(world)){
						continue;
					}
					if (test.getHitbox().overlaps(e.getHitbox())) { // for player bullet - enemy collisions
						test.onHit(world, e);
						e.onHit(world, test);
					}
				}
			}
		}

		long end = System.nanoTime();
		long took = end - start;
		Gdx.app.debug("took", "" + took);

	}
}
