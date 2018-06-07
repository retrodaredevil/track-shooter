package me.retrodaredevil.game.trackshooter.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import me.retrodaredevil.game.trackshooter.entity.DisplayEntity;
import me.retrodaredevil.game.trackshooter.render.ImageRenderComponent;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;
import me.retrodaredevil.game.trackshooter.world.World;

public final class EntityUtil {

	public static void displayScore(World world, Vector2 location, Drawable regionDrawable){
		DisplayEntity entity = new DisplayEntity(750, location);
		RenderComponent renderComponent = new ImageRenderComponent(new Image(regionDrawable), entity, .8f, .8f / 2f).setFacingDirection(0);
		entity.setRenderComponent(renderComponent);

		world.addEntity(entity);
	}

}
