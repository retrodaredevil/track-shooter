package me.retrodaredevil.game.trackshooter.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import me.retrodaredevil.game.trackshooter.Bullet;
import me.retrodaredevil.game.trackshooter.BulletShooter;
import me.retrodaredevil.game.trackshooter.OnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.SimpleEntity;
import me.retrodaredevil.game.trackshooter.render.ImageRenderComponent;
import me.retrodaredevil.game.trackshooter.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Player extends SimpleEntity implements BulletShooter {
	private static final int MAX_BULLETS = 3;

	private List<Bullet> activeBullets = new ArrayList<>();

	public Player(){
		setMoveComponent(new OnTrackMoveComponent(this));
		setRenderComponent(new ImageRenderComponent(new Image(new Texture("player.png")), this, 1.0f, 1.0f));
	}

	@Override
	public void update(float delta, World world) {
		super.update(delta, world);
		if(!activeBullets.isEmpty()){
			for(Iterator<Bullet> it = activeBullets.iterator(); it.hasNext();){
				Bullet bullet = it.next();
				if(bullet.isRemoved()){
					it.remove();
				}
			}
		}
	}

	@Override
	public Bullet shootBullet(World world) {
		if(activeBullets.size() >= MAX_BULLETS){
			return null;
		}
		Bullet bullet = Bullet.createFromEntity(this);
		world.addEntity(bullet);
		activeBullets.add(bullet);
		return bullet;
	}
}
