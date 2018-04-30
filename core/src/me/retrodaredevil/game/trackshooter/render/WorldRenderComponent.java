package me.retrodaredevil.game.trackshooter.render;

import com.badlogic.gdx.scenes.scene2d.Stage;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.world.World;

public class WorldRenderComponent implements RenderComponent {

	private World world;

	public WorldRenderComponent(World world){
		this.world = world;
	}

	@Override
	public void render(float delta, Stage stage) {
		RenderComponent trackRender = world.getTrack().getRenderComponent();
		if(trackRender != null){
			trackRender.render(delta, stage);
		}
		for(Entity entity : world.getEntities()){
			RenderComponent entityRender = entity.getRenderComponent();
			if(entityRender != null){
				entityRender.render(delta, stage);
			}
		}
	}

	@Override
	public void dispose() {

	}
}
