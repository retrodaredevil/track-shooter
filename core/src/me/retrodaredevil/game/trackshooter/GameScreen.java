package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import me.retrodaredevil.game.input.StandardUSBControllerInput;
import me.retrodaredevil.game.trackshooter.player.Player;
import me.retrodaredevil.game.trackshooter.player.PlayerController;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;
import me.retrodaredevil.game.trackshooter.render.WorldViewport;
import me.retrodaredevil.game.trackshooter.world.LineTrackPart;
import me.retrodaredevil.game.trackshooter.world.Track;
import me.retrodaredevil.game.trackshooter.world.TrackPart;
import me.retrodaredevil.game.trackshooter.world.World;
import me.retrodaredevil.input.ControllerManager;

import java.util.Collection;

public class GameScreen extends ScreenAdapter {

	private Stage stage;
	private World world;

	private ControllerManager controllerManager = new ControllerManager();

	public GameScreen(){
		Vector2 bRight = new Vector2(7, -7), bLeft = new Vector2(-7, -7),
				uRight = new Vector2(7, 7), uLeft = new Vector2(-7, 7);
		Collection<? extends TrackPart> parts = new LineTrackPart.LineTrackPartBuilder(bLeft)
				.connect(new Vector2(5, -7))
				.connect(new Vector2(5, -5))
				.connect(new Vector2(7, -5))
//				.connect(bRight)
				.connect(uRight)
				.connect(uLeft)
				.build(true);
		this.world = new World(new Track(parts), 16, 16);
		this.stage = new Stage(new WorldViewport(world));

		StandardUSBControllerInput controller = new StandardUSBControllerInput(Controllers.getControllers().get(0));
		controllerManager.addController(controller);

		Player player = new Player();
		player.setEntityController(new PlayerController(player, controller));
		world.addEntity(player);
	}

	@Override
	public void render(float delta) {
		doUpdate(delta);
		doRender(delta);

	}
	private void doUpdate(float delta){
		controllerManager.update();
		world.update(delta, world);

	}
	private void doRender(float delta){
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		RenderComponent worldRender = world.getRenderComponent();
		if(worldRender != null){
			worldRender.render(delta, stage);
		}

		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		Gdx.gl.glViewport(0, 0, width, height);
		stage.getViewport().update(width, height,true);

//		OrthographicCamera camera = (OrthographicCamera) stage.getCamera();
//		camera.setToOrtho(false, 20, 20);
//		camera.position.set(0, 0, 0);
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
