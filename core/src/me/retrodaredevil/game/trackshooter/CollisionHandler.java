package me.retrodaredevil.game.trackshooter;

import me.retrodaredevil.game.trackshooter.entity.Bullet;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.Hittable;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollisionHandler implements Updateable {
	@Override
	public void update(float delta, World world) {
		Collection<Entity> entities = new ArrayList<>(world.getEntities()); // cloned version of current entities

		List<Hittable> friendly = new ArrayList<>();
		List<Hittable> enemies = new ArrayList<>();

		for(Entity e : entities){
			if(e instanceof Player){
				friendly.add((Hittable) e);
			} else if (e instanceof Bullet) {
				if(((Bullet) e).getShooter() instanceof Player){
					friendly.add((Hittable) e);
				} else {
					enemies.add((Hittable) e);
				}
			} else if (e instanceof Hittable){
				enemies.add((Hittable) e);
			}
		}
//		Gdx.app.debug("friendly", friendly.toString());
//		Gdx.app.debug("enemies", enemies.toString());
		for(Hittable friend : friendly){
			for(Hittable enemy : enemies){
				if(friend.getHitbox().overlaps(enemy.getHitbox())){
					friend.onHit(world, enemy);
					enemy.onHit(world, friend);
					System.out.println("worked");
				}
			}
		}
	}
}
