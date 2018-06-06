package me.retrodaredevil.game.trackshooter.entity.player;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import me.retrodaredevil.game.trackshooter.CollisionIdentity;
import me.retrodaredevil.game.trackshooter.entity.*;
import me.retrodaredevil.game.trackshooter.entity.movement.OnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.powerup.PowerupEntity;
import me.retrodaredevil.game.trackshooter.level.LevelMode;
import me.retrodaredevil.game.trackshooter.render.ImageRenderComponent;
import me.retrodaredevil.game.trackshooter.util.CannotHitException;
import me.retrodaredevil.game.trackshooter.util.Constants;
import me.retrodaredevil.game.trackshooter.util.MathUtil;
import me.retrodaredevil.game.trackshooter.util.Resources;
import me.retrodaredevil.game.trackshooter.world.World;

import java.util.ArrayList;
import java.util.List;

public class Player extends SimpleEntity implements BulletShooter, Entity {
	private static final int MAX_BULLETS = 3;
	private static final float TRIPLE_OFFSET = 15;  // degrees
	private static final float SHOT_GUN_RANGE_DEGREES = 40; // degrees
	private static final int SHOT_GUN_BULLETS = 5;
	private static final int FULL_BULLETS = 10;

	private static final float BULLET_DISTANCE = 20;
	private static final float SHOT_GUN_DISTANCE = 7;
	private static final float SHOT_GUN_RANDOM_EXTEND_RANGE = 5; // can now go a max of this + SHOT_GUN_DISTANCE

	private List<Bullet> activeBullets = new ArrayList<>(); // you must update using World.updateEntityList(activeBullets);
	private Score score;

	private boolean hit = false;

	private Bullet.ShotType shotType = Bullet.ShotType.STRAIGHT;

	public Player(){
		setMoveComponent(new OnTrackMoveComponent(this));
		setHitboxSize(.7f, .7f);
		score = new PlayerScore(this);
		canRespawn = true;
		collisionIdentity = CollisionIdentity.FRIENDLY;
	}

	@Override
	public void beforeSpawn(World world) {
		super.beforeSpawn(world);
		hit = false;
		setRenderComponent(new ImageRenderComponent(new Image(Resources.PLAYER_TEXTURE), this, .8f, .8f));
	}

	public Score getScoreObject(){
		return score;
	}

	@Override
	public void update(float delta, World world) {
		super.update(delta, world);
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
	public void onHit(World world, Entity other) throws CannotHitException {
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
	public boolean shouldRemove(World world) {
		return super.shouldRemove(world) || hit || score.getLives() <= 0;
	}

	@Override
	public List<Bullet> shootBullet(World world) {
		final CollisionIdentity collisionIdentity = CollisionIdentity.FRIENDLY_PROJECTILE;
		List<Bullet> bullets = new ArrayList<>();
		switch(getShotType()){
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
		activeBullets.add(bullets.get(0));
		Resources.BULLET_SOUND.play(1, 4, 0);
		return bullets;
	}

	@Override
	public boolean canShootBullet(World world) {
		if(world.getLevel().getMode() != LevelMode.NORMAL){
			return false;
		}
		float max = MAX_BULLETS;
		if(this.shotType == Bullet.ShotType.SHOT_GUN || this.shotType == Bullet.ShotType.FULL){
			max = 1;
		}
		World.updateEntityList(activeBullets);
		return activeBullets.size() < max;
	}

	@Override
	public void setShotType(Bullet.ShotType shotType) {
		this.shotType = shotType;
	}

	@Override
	public Bullet.ShotType getShotType() {
		return shotType;
	}
}
