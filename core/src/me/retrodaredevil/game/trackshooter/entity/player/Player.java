package me.retrodaredevil.game.trackshooter.entity.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import me.retrodaredevil.game.trackshooter.CollisionIdentity;
import me.retrodaredevil.game.trackshooter.achievement.AchievementHandler;
import me.retrodaredevil.game.trackshooter.entity.movement.TravelRotateVelocityOnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.item.PowerupActivateListenerItem;
import me.retrodaredevil.game.trackshooter.entity.Bullet;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.SimpleEntity;
import me.retrodaredevil.game.trackshooter.level.Level;
import me.retrodaredevil.game.trackshooter.level.LevelMode;
import me.retrodaredevil.game.trackshooter.render.components.ImageRenderComponent;
import me.retrodaredevil.game.trackshooter.util.CannotHitException;
import me.retrodaredevil.game.trackshooter.util.Constants;
import me.retrodaredevil.game.trackshooter.util.MathUtil;
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


	private final Map<Bullet.ShotType, List<List<Bullet>>> activeBulletsMap = new EnumMap<>(Bullet.ShotType.class);
	private final Score score;
	private final Type playerType;

	private boolean hit = false;
	private boolean triplePowerup = false;


	public Player(World world, PlayerScore.RumbleGetter rumbleGetter, AchievementHandler achievementHandler, Type playerType){
		super(world);
		this.playerType = playerType;
		setMoveComponent(new TravelRotateVelocityOnTrackMoveComponent(world, this));
//		setMoveComponent(new FreeVelocityMoveComponent(this));
		setHitboxSize(.7f);
		score = new PlayerScore(this, rumbleGetter, achievementHandler);
		canRespawn = true;
		collisionIdentity = CollisionIdentity.FRIENDLY;
	}

	public void setTriplePowerup(boolean b){
		this.triplePowerup = b;
	}
	public Type getPlayerType() {
		return playerType;
	}

	@Override
	public void beforeSpawn() {
		super.beforeSpawn();
//		hit = false; // set in afterRemove()
		assert !hit : "afterRemove() didn't set hit to false!";
		setRenderComponent(new ImageRenderComponent(new Image(playerType.getDrawable(world.getMainSkin())), this, .8f, .8f));
	}

	public Score getScoreObject(){
		return score;
	}

	@Override
	public void update(float delta) {
		super.update(delta);
	}


	@Override
	public void onHit(Entity other) {
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
	public void afterRemove() {
		super.afterRemove();
		hit = false; // makes sure shouldRemove() only returns true once
	}

	@Override
	public boolean shouldRemove() {
		return super.shouldRemove() || hit || score.getLives() <= 0;
	}

	private Bullet.ShotType checkNullShotType(final Bullet.ShotType shotType){
		if(shotType == null){
			return triplePowerup ? Bullet.ShotType.TRIPLE : Bullet.ShotType.STRAIGHT;
		}
		return shotType;
	}
	public void activatePowerup(){
		Collection<PowerupActivateListenerItem> items = getItems(PowerupActivateListenerItem.class);
		if(items == null){
			return;
		}
		for(PowerupActivateListenerItem item : items){
			boolean didSomething = item.activatePowerup(this);
			if(didSomething){
				break;
			}
		}
	}

	/**
	 * @param shotType The specified shot type or null
	 * @return A list of all the bullets shot. May be an empty list
	 */
	public List<Bullet> shootBullet(Bullet.ShotType shotType) {
		shotType = checkNullShotType(shotType);
		if(!canShootBullet(shotType)){
			return Collections.emptyList();
		}
//		System.out.println("going to shoot a bullet. level mode: " + world.getLevel().getMode()
//				+ " mode time: " + world.getLevel().getModeTime() + " levelNumber: " + world.getLevel().getNumber());
		final CollisionIdentity collisionIdentity = CollisionIdentity.FRIENDLY_PROJECTILE;

		List<Bullet> bullets = new ArrayList<>();
		switch(shotType){
			case TRIPLE:
				Bullet bullet = Bullet.createFromEntity(world, this, Constants.BULLET_SPEED, 0, BULLET_DISTANCE, collisionIdentity);
				bullets.add(bullet);

				Bullet bullet2 = Bullet.createFromEntity(world, this, Constants.BULLET_SPEED, TRIPLE_OFFSET, BULLET_DISTANCE, collisionIdentity);
				bullets.add(bullet2);

				Bullet bullet3 = Bullet.createFromEntity(world, this, Constants.BULLET_SPEED, -TRIPLE_OFFSET, BULLET_DISTANCE, collisionIdentity);
				bullets.add(bullet3);
				break;
			case SHOT_GUN:
				for(int i = 0; i < SHOT_GUN_BULLETS; i++){
					float rotation = (MathUtils.random() * 2.0f * SHOT_GUN_RANGE_DEGREES) - SHOT_GUN_RANGE_DEGREES;
					Bullet shotBullet = Bullet.createFromEntity(world, this, Constants.SHOT_GUN_BULLET_SPEED, rotation,
							SHOT_GUN_DISTANCE + MathUtils.random(SHOT_GUN_RANDOM_EXTEND_RANGE), collisionIdentity);
					bullets.add(shotBullet);
				}
				break;
			case FULL:
				final float speed = Constants.BULLET_SPEED;
				final float SPACE_BETWEEN = 360.0f / FULL_BULLETS;
				float offset = (float) ((world.getTimeMillis() / 30.0) % 360.0);
				offset += SPACE_BETWEEN * MathUtils.random(FULL_BULLETS); // make it so the first bullet is random
				for(int i = 0; i < FULL_BULLETS; i++){
					float rotation = i * SPACE_BETWEEN;
					rotation += offset;
					rotation = MathUtil.mod(rotation, 360);
					Bullet fullBullet = new Bullet(world, this, this.getLocation(),
							new Vector2(speed * MathUtils.cosDeg(rotation), speed * MathUtils.sinDeg(rotation)),
							rotation, BULLET_DISTANCE, collisionIdentity);
					bullets.add(fullBullet);
				}

				break;
			default:
				Bullet straightBullet = Bullet.createFromEntity(world, this, Constants.BULLET_SPEED, 0, BULLET_DISTANCE, collisionIdentity);
				bullets.add(straightBullet);
				break;
		}
		for(Bullet bullet : bullets) {
			world.getLevel().addEntity(bullet);
		}
		List<List<Bullet>> shotsList = activeBulletsMap.get(shotType);
		if(shotsList == null){
			shotsList = new ArrayList<>();
			activeBulletsMap.put(shotType, shotsList);
		}
		shotsList.add(bullets);

		world.getMainSkin().get("bullet", Sound.class).play(1, 4, 0);
		getScoreObject().onShot(bullets.size());
		return bullets;
	}

	private boolean canShootBullet(Bullet.ShotType shotType) {
		Level level = world.getLevel();
		if(level.getMode() != LevelMode.NORMAL || level.isEndingSoon()){
			return false;
		}
		updateActive();
		int max;
		int amount = 0;
		switch(shotType){
			case SHOT_GUN: case FULL:
				max = 2;
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
	private void updateActive(){
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


	public enum Type {
		NORMAL("player"), SNIPER("sniper");

		private final String skinName;

		Type(String skinName){
			this.skinName = skinName;
		}
		public Drawable getDrawable(Skin skin){
			return skin.getDrawable(skinName);
		}
	}
}
