package me.retrodaredevil.game.trackshooter.render;

import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * A short lived class that helps with the rendering of {@link Renderable}s and {@link Stage}s.
 * <p>
 * Renderables are rendered first, in order, and then, stages are rendered, in order.
 * <p>
 * Because most Renderables utilize stages, renderables or stages added last will be shown on top.
 * Some Renderables don't use stages meaning they will be drawn under everything.
 */
public class Renderer {
	private final Collection<Renderable> renderables = new LinkedHashSet<>();

	/**
	 * Adds the renderable and adds its preferred stage or the main stage if there's no preferred stage
	 * @param renderable The {@link Renderable} to add or null to do nothing.
	 */
	public Renderer addRenderable(Renderable renderable){
		if(renderable == null){
			return this;
		}
		renderables.add(renderable);
		return this;
	}


	@SuppressWarnings("LibGDXFlushInsideLoop") // drawStage doesn't flush
	public void render(float delta){
		for(Renderable r : renderables){
			r.render(delta);
		}
	}

	/**
	 * This resizes everything added to this Renderer. Sometimes, you should not use this because
	 * it would be easier to just resize whatever you're using individually.
	 * @param width
	 * @param height
	 */
	public void resize(int width, int height){
		for(Renderable r : renderables){
			r.resize(width, height);
		}
	}
}
