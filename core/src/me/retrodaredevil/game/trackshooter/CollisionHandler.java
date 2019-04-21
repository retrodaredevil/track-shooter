package me.retrodaredevil.game.trackshooter;

import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.world.World;

import java.util.*;

public class CollisionHandler implements Updateable {
	/*
	Future references if we change how collision handling works:
	 * https://stackoverflow.com/a/45142822/5434860 good answer on how he dealt with collisions
	 * https://gamedev.stackexchange.com/a/137345 a visitor pattern (lots of methods)
	 */
	private final World world;

	public CollisionHandler(World world) {
		this.world = world;
	}

	@Override
	public void update(float delta) {
		final Collection<Entity> entities = world.getEntities();

		Collection<Entity> possiblyCollides = new ArrayList<>();
		// This map represents all the entities that should be checked for each entity of a certain CollisionIdentity
		Map<CollisionIdentity, Collection<Entity>> collisionMap = new EnumMap<>(CollisionIdentity.class);
		for(Entity e : entities){
			CollisionIdentity collisionIdentity = e.getCollisionIdentity();
			if(!collisionIdentity.canCollide()){
				continue;
			}
			possiblyCollides.add(e);

			for(CollisionIdentity element : collisionIdentity.getTriggers()){
				Collection<Entity> collisionList = collisionMap.get(element);
				if(collisionList == null){
					collisionList = new ArrayList<>();
					collisionMap.put(element, collisionList); // only call put() once
				}
				collisionList.add(e);
			}
		}
		outerLoop : for(Entity e : possiblyCollides){
			Collection<Entity> collidesWith = collisionMap.get(e.getCollisionIdentity());
			if(collidesWith != null){
				for(Entity test : collidesWith){
					if(e.shouldRemove()){
						continue outerLoop;
					}
					if(test.shouldRemove()){
						continue;
					}
					if (test.getHitbox().overlaps(e.getHitbox())) { // for player bullet - enemy collisions
						test.onHit(e);
						e.onHit(test);
					}
				}
			}
		}
	}
}
