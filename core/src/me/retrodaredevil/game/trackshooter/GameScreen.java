package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;
import me.retrodaredevil.game.trackshooter.world.Track;
import me.retrodaredevil.game.trackshooter.world.World;

public class GameScreen extends ScreenAdapter {

	private Stage stage;
	private World world;

	public GameScreen(ScreenViewport viewport){
		this.stage = new Stage(viewport);


//		world = new World(track);
	}

	@Override
	public void render(float delta) {
		RenderComponent worldRender = world.getRenderComponent();
		if(worldRender != null){
			worldRender.render(delta, stage);
		}
	}

	@Override
	public void dispose() {
		RenderComponent worldRender = world.getRenderComponent();
		if(worldRender != null){
			worldRender.dispose();
		}
		stage.dispose();

	}
}
