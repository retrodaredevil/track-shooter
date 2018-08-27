package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import me.retrodaredevil.game.trackshooter.render.RenderComponent;
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
	public void addMainStage(){
		addStage(mainStage);
	}
	public void addStage(Stage stage){
		stages.add(stage);
	}

	public void render(float delta){
		System.out.println();
		for(Renderable r : renderables){
//			RenderComponent renderComponent = r.getRenderComponent();
//			if(renderComponent != null){
//				Stage stage = r.getPreferredStage();
//				if(stage == null){
//					stage = mainStage;
//				}
//				renderComponent.render(delta, stage);
//			}
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
}
