package me.retrodaredevil.game.trackshooter.entity.player;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import me.retrodaredevil.game.trackshooter.CollisionIdentity;
import me.retrodaredevil.game.trackshooter.entity.movement.TravelRotateVelocityOnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.item.PowerupActivateListenerItem;
import me.retrodaredevil.game.trackshooter.entity.Bullet;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.SimpleEntity;
import me.retrodaredevil.game.trackshooter.level.Level;
import me.retrodaredevil.game.trackshooter.level.LevelMode;
import me.retrodaredevil.game.trackshooter.render.ImageRenderComponent;
import me.retrodaredevil.game.trackshooter.util.CannotHitException;
import me.retrodaredevil.game.trackshooter.util.Constants;
import me.retrodaredevil.game.trackshooter.util.MathUtil;
import me.retrodaredevil.game.trackshooter.util.Resources;
import me.retrodaredevil.game.trackshooter.world.World;

import java.util.*;

public class Player extends SimpleEntity {
	private static final int MAX_BULLETS = 4;
	private static final float TRIPLE_OFFSET = 15;  // degrees
	private static final float SHOT_GUN_RANGE_DEGREES = 40; // degrees
	private static final int SHOT_GUN_BULLETS = 5;
	private static final int FULL_BULLETS = 10;

	private static final float BULLET_DISTANCE = 20;
	private static final float SHOT_GUN_DISTANCE = 7;
	private static final float SHOT_GUN_RANDOM_EXTEND_RANGE = 5; // can now go a max of this + SHOT_GUN_DISTANCE

	private Map<Bullet.ShotType, List<List<Bullet>>> activeBulletsMap = new HashMap<>();
//	private List<Bullet> activeBullets = new ArrayList<>(); // you must update using World.updateEntityList(activeBullets);
	private Score score;

	private boolean hit = false;
	private boolean triplePowerup = false;


	public Player(){
		setMoveComponent(new TravelRotateVelocityOnTrackMoveComponent(this));
		setHitboxSize(.7f);
		score = new PlayerScore(this);
		canRespawn = true;
		collisionIdentity = CollisionIdentity.FRIENDLY;
	}

	public void setTriplePowerup(boolean b){
		this.triplePowerup = b;
	}

	@Override
	public void beforeSpawn(World world) {
		super.beforeSpawn(world);
//		hit = false; // set in afterRemove()
		assert !hit : "afterRemove() didn't set hit to false!";
		setRenderComponent(new ImageRenderComponent(new Image(Resources.PLAYER_TEXTURE), this, .8f, .8f));
	}

	public Score getScoreObject(){
		return score;
	}

	@Override
	public void update(float delta, World world) {
		super.update(delta, world);

//		 old code that changes the shotType every 10 / ShotType.values().length seconds
//		long now = System.currentTimeMillis();
//		float percent = (now % 10000) / 10000.0f;
//		Bullet.ShotType[] values = Bullet.ShotType.values();
//		float between = 1.0f / (values.length);
//		for(int i = 0; i < values.length; i++){
//			if((i + 1) * between >= percent){
//				this.shotType = values[i];
//				break;
//			}
//		}
	}


	@Override
	public void onHit(World world, Entity other) {
		CollisionIdentity identity = other.getCollisionIdentity();
		if(other.getShooter() == this || identity == CollisionIdentity.FRIENDLY_PROJECTILE || identity == CollisionIdentity.FRIENDLY){
			throw new CannotHitException(other, this);
		}
		if(identity == CollisionIdentity.POWERUP){
			return;
		}
		hit = true;
		score.onDeath(other);
	}

	@Override
	public void afterRemove(World world) {
		super.afterRemove(world);
		hit = false; // makes sure shouldRemove() only returns true once
	}

	@Override
	public boolean shouldRemove(World world) {
		return super.shouldRemove(world) || hit || score.getLives() <= 0;
	}

	private Bullet.ShotType checkNullShotType(Bullet.ShotType shotType){
		if(shotType == null){
			shotType = triplePowerup ? Bullet.ShotType.TRIPLE : Bullet.ShotType.STRAIGHT;
		}
		return shotType;
	}
	public void activatePowerup(World world){
		Collection<PowerupActivateListenerItem> items = getItems(PowerupActivateListenerItem.class);
		if(items == null){
			return;
		}
		for(PowerupActivateListenerItem item : items){
			boolean didSomething = item.activatePowerup(world, this);
			if(didSomething){
				break;
			}
		}
	}

	/**
	 * @param world The world to shoot the bullets in
	 * @param shotType The specified shot type or null
	 * @return A list of all the bullets shot or null if we didn't shoot any
	 */
	public List<Bullet> shootBullet(World world, Bullet.ShotType shotType) {
		shotType = checkNullShotType(shotType);
		if(!canShootBullet(world, shotType)){
			return null;
		}
		final CollisionIdentity collisionIdentity = CollisionIdentity.FRIENDLY_PROJECTILE;

		List<Bullet> bullets = new ArrayList<>();
		switch(shotType){
			case TRIPLE:
				Bullet bullet = Bullet.createFromEntity(this, Constants.BULLET_SPEED, 0, BULLET_DISTANCE, collisionIdentity);
				bullets.add(bullet);

				Bullet bullet2 = Bullet.createFromEntity(this, Constants.BULLET_SPEED, TRIPLE_OFFSET, BULLET_DISTANCE, collisionIdentity);
				bullets.add(bullet2);

				Bullet bullet3 = Bullet.createFromEntity(this, Constants.BULLET_SPEED, -TRIPLE_OFFSET, BULLET_DISTANCE, collisionIdentity);
				bullets.add(bullet3);
				break;
			case SHOT_GUN:
				for(int i = 0; i < SHOT_GUN_BULLETS; i++){
					float rotation = (MathUtils.random() * 2.0f * SHOT_GUN_RANGE_DEGREES) - SHOT_GUN_RANGE_DEGREES;
					Bullet shotBullet = Bullet.createFromEntity(this, Constants.SHOT_GUN_BULLET_SPEED, rotation,
							SHOT_GUN_DISTANCE + MathUtils.random(SHOT_GUN_RANDOM_EXTEND_RANGE), collisionIdentity);
					bullets.add(shotBullet);
				}
				break;
			case FULL:
				final float speed = Constants.BULLET_SPEED;
				final float SPACE_BETWEEN = 360.0f / FULL_BULLETS;
				float offset = (float) ((System.currentTimeMillis() / 30.0) % 360.0);
				offset += SPACE_BETWEEN * MathUtils.random(FULL_BULLETS); // make it so the first bullet is random
				for(int i = 0; i < FULL_BULLETS; i++){
					float rotation = i * SPACE_BETWEEN;
					rotation += offset;
					rotation = MathUtil.mod(rotation, 360);
					Bullet fullBullet = new Bullet(this, this.getLocation(),
							new Vector2(speed * MathUtils.cosDeg(rotation), speed * MathUtils.sinDeg(rotation)),
							rotation, BULLET_DISTANCE, collisionIdentity);
					bullets.add(fullBullet);
				}

				break;
			default:
				Bullet straightBullet = Bullet.createFromEntity(this, Constants.BULLET_SPEED, 0, BULLET_DISTANCE, collisionIdentity);
				bullets.add(straightBullet);
				break;
		}
		for(Bullet bullet : bullets) {
			world.addEntity(bullet);
		}
//		activeBullets.add(bullets.get(0));
		List<List<Bullet>> shotsList = activeBulletsMap.get(shotType);
		if(shotsList == null){
			shotsList = new ArrayList<>();
			activeBulletsMap.put(shotType, shotsList);
		}
		shotsList.add(bullets);
		Resources.BULLET_SOUND.play(1, 4, 0);
		getScoreObject().onShot(bullets.size());
		return bullets;
	}

	private boolean canShootBullet(World world, Bullet.ShotType shotType) {
		Level level = world.getLevel();
		if(level.getMode() != LevelMode.NORMAL || level.isEndingSoon()){
			return false;
		}
		updateActive(world);
		int max;
		int amount = 0;
		switch(shotType){
			case SHOT_GUN: case FULL:
				max = 1;
				List<List<Bullet>> shotGunShots = activeBulletsMap.get(Bullet.ShotType.SHOT_GUN);
				List<List<Bullet>> fullShots = activeBulletsMap.get(Bullet.ShotType.FULL);
				if(shotGunShots != null)
					amount += shotGunShots.size(); // combine total shots for each
				if(fullShots != null)
					amount += fullShots.size();
				break;
			default:
				max = MAX_BULLETS;
				List<List<Bullet>> straightShots = activeBulletsMap.get(Bullet.ShotType.STRAIGHT);
				List<List<Bullet>> tripleShots = activeBulletsMap.get(Bullet.ShotType.TRIPLE);
				if(straightShots != null)
					amount += straightShots.size(); // combine total shots for each
				if(tripleShots != null)
					amount += tripleShots.size();
				break;
		}
		return amount < max;
	}
	private void updateActive(World world){
		for(Map.Entry<Bullet.ShotType, List<List<Bullet>>> entry : activeBulletsMap.entrySet()){
			List<List<Bullet>> shotsList = entry.getValue();
			for(ListIterator<List<Bullet>> it = shotsList.listIterator(); it.hasNext(); ){
				List<Bullet> shot = it.next();
				World.updateEntityList(shot);
				if(shot.isEmpty()){ // remove the shot if all the bullets were removed
					it.remove();
				}
			}
		}
	}
}
