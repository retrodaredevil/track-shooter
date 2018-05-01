package me.retrodaredevil.game.trackshooter.entity.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.Hittable;
import me.retrodaredevil.game.trackshooter.entity.SimpleEntity;
import me.retrodaredevil.game.trackshooter.render.ImageRenderComponent;
import me.retrodaredevil.game.trackshooter.world.World;

public class Shark extends SimpleEntity implements Hittable {


	public Shark(){
		setRenderComponent(new ImageRenderComponent(new Image(new Texture("shark.png")), this, 1.0f, 1.0f));

		getHitbox().setSize(.8f, .8f);
	}

	@Override
	public void onHit(World world, Entity other) {
		System.out.println("I've been hit!!");
	}
}
