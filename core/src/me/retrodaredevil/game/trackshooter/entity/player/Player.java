package me.retrodaredevil.game.trackshooter.entity.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import me.retrodaredevil.game.trackshooter.entity.*;
import me.retrodaredevil.game.trackshooter.entity.movement.OnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.render.ImageRenderComponent;
import me.retrodaredevil.game.trackshooter.util.Constants;
import me.retrodaredevil.game.trackshooter.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Player extends SimpleEntity implements BulletShooter, Hittable {
	private static final int MAX_BULLETS = 2;

	private List<Bullet> activeBullets = new ArrayList<>(); // you must update using World.updateEntityList(activeBullets);

	public Player(){
		setMoveComponent(new OnTrackMoveComponent(this));
		setRenderComponent(new ImageRenderComponent(new Image(new Texture("player.png")), this, .8f, .8f));
	}

	@Override
	public void update(float delta, World world) {
		super.update(delta, world);
	}

	@Override
	public void onHit(World world, Entity other) {
		System.out.println("player is hit");
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
		return bullet;
	}

}
