package me.retrodaredevil.game.trackshooter.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import me.retrodaredevil.game.trackshooter.entity.DisplayEntity;
import me.retrodaredevil.game.trackshooter.render.components.ImageRenderComponent;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;
import me.retrodaredevil.game.trackshooter.world.World;

public final class EntityUtil {
	private static final float DEFAULT_WIDTH = .8f;
	private static final float DEFAULT_TIME = .75f;
	private EntityUtil(){
	}

	public static void displayScore(World world, Vector2 location, Drawable drawable, float width, float time){
		DisplayEntity entity = new DisplayEntity(world, time, location);
		Image image = new Image(drawable);
		RenderComponent renderComponent = new ImageRenderComponent(world.getMainStage(), image, entity, width, width * image.getHeight() / image.getWidth()).setFacingDirection(0);
		entity.setRenderComponent(renderComponent);

		world.addEntity(entity);
	}
	public static void displayScore(World world, Vector2 location, Drawable drawable){
		displayScore(world, location, drawable, DEFAULT_WIDTH, DEFAULT_TIME);
	}

}
