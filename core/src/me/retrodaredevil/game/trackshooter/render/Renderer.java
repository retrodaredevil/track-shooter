package me.retrodaredevil.game.trackshooter.render;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import me.retrodaredevil.game.trackshooter.util.RenderUtil;

/**
 * A short lived class that helps with the rendering of {@link Renderable}s and {@link Stage}s.
 * <p>
 * Renderables are rendered first, in order, and then, stages are rendered, in order.
 */
public class Renderer {
	private final Batch batch;
	private final Stage mainStage;
	private final Collection<Renderable> renderables = new LinkedHashSet<>();
	private final Set<Stage> stages = new LinkedHashSet<>();

	public Renderer(Batch batch, Stage mainStage){
		this.batch = batch;
		this.mainStage = mainStage;
	}

	/**
	 * Adds the renderable and adds its preferred stage or the main stage if there's no preferred stage
	 * @param renderable
	 */
	public void addRenderable(Renderable renderable){
		if(renderable == null){
			return;
		}
		renderables.add(renderable);
		Stage stage = renderable.getPreferredStage();
		if(stage == null){
			stage = mainStage;
		}
		stages.add(stage);
	}

	/**
	 * Adds the main stage if it hasn't been added yet
	 */
	public void addMainStage(){
		addStage(mainStage);
	}

	/**
	 * Adds a stage to the stage
	 * @param stage
	 */
	public void addStage(Stage stage){
		stages.add(stage);
	}

	@SuppressWarnings("LibGDXFlushInsideLoop") // drawStage doesn't flush
	public void render(float delta){
		for(Renderable r : renderables){
			r.autoRender(delta, mainStage, true);
		}
		for(Stage stage : stages){
			stage.act();
		}
		batch.begin();
		for(Stage stage : stages){
			RenderUtil.drawStage(batch, stage);
		}
		batch.end();
	}

	/**
	 * This resizes everything added to this Renderable. Sometimes, you should not use this because
	 * it would be easier to just resize whatever you're using individually.
	 * @param width
	 * @param height
	 */
	public void resize(int width, int height){
		for(Stage stage : stages){
			stage.getViewport().update(width, height, true);
		}
	}
}
