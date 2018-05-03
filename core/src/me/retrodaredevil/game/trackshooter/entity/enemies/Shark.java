package me.retrodaredevil.game.trackshooter.entity.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.entity.Hittable;
import me.retrodaredevil.game.trackshooter.entity.SimpleEntity;
import me.retrodaredevil.game.trackshooter.render.ImageRenderComponent;
import me.retrodaredevil.game.trackshooter.world.World;

public class Shark extends SimpleEntity implements Hittable {

	private int lives = 3;

	public Shark(){
		setRenderComponent(new ImageRenderComponent(new Image(new Texture("shark.png")), this, 1.0f, 1.0f));

		getHitbox().setSize(.7f, .7f);
	}

	@Override
	public void onHit(World world, Entity other) {
		Gdx.app.debug("hit by", other.toString() + " at " + Gdx.app.getGraphics().getFrameId());
		lives--;
	}

	@Override
	public boolean shouldRemove(World world) {
		return super.shouldRemove(world) || lives <= 0;
	}
}
