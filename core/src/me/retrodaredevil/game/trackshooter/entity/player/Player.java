package me.retrodaredevil.game.trackshooter.entity.player;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import me.retrodaredevil.game.trackshooter.entity.*;
import me.retrodaredevil.game.trackshooter.entity.movement.OnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.entity.powerup.Powerup;
import me.retrodaredevil.game.trackshooter.render.ImageRenderComponent;
import me.retrodaredevil.game.trackshooter.util.CannotHitException;
import me.retrodaredevil.game.trackshooter.util.Constants;
import me.retrodaredevil.game.trackshooter.util.Resources;
import me.retrodaredevil.game.trackshooter.world.World;

import java.util.ArrayList;
import java.util.List;

public class Player extends SimpleEntity implements BulletShooter, Hittable {
	private static final int MAX_BULLETS = 2;

	private List<Bullet> activeBullets = new ArrayList<>(); // you must update using World.updateEntityList(activeBullets);
	private Score score;

	private boolean hit = false;

	public Player(){
		setMoveComponent(new OnTrackMoveComponent(this));
		setRenderComponent(new ImageRenderComponent(new Image(Resources.PLAYER_TEXTURE), this, .8f, .8f));
		setHitboxSize(.7f, .7f);
		score = new PlayerScore(this);
	}

	public Score getScoreObject(){
		return score;
	}

	@Override
	public void update(float delta, World world) {
		super.update(delta, world);
	}

	@Override
	public void onHit(World world, Entity other) throws CannotHitException {
		if(other.getShooter() == this){
			throw new CannotHitException(other, this);
		}
		if(other instanceof Powerup){
			return;
		}
		System.out.println("player is hit");
		hit = true;
	}

	@Override
	public boolean shouldRemove(World world) {
		return super.shouldRemove(world) || hit;
	}

	@Override
	public Bullet shootBullet(World world) {
		World.updateEntityList(activeBullets);
		if(activeBullets.size() >= MAX_BULLETS){
			return null;
		}
		Bullet bullet = Bullet.createFromEntity(this, Constants.BULLET_SPEED);
		world.addEntity(bullet);
		activeBullets.add(bullet);
		Resources.BULLET_SOUND.play(1, 4, 0);
		return bullet;
	}

}
