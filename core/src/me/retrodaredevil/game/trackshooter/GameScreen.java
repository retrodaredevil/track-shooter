package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import me.retrodaredevil.game.input.StandardUSBControllerInput;
import me.retrodaredevil.game.trackshooter.entity.enemies.Shark;
import me.retrodaredevil.game.trackshooter.entity.enemies.SharkAIController;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.entity.player.PlayerController;
import me.retrodaredevil.game.trackshooter.entity.powerup.Cherry;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;
import me.retrodaredevil.game.trackshooter.render.WorldViewport;
import me.retrodaredevil.game.trackshooter.util.Resources;
import me.retrodaredevil.game.trackshooter.world.*;
import me.retrodaredevil.input.ControllerManager;

public class GameScreen extends ScreenAdapter {

	private Player player;
	private Stage stage;
	private World world;

	private ControllerManager controllerManager = new ControllerManager();

	public GameScreen(){
		this.world = new World(Tracks.newKingdomTrack(), 18, 18);
		this.stage = new Stage(new WorldViewport(world));

		StandardUSBControllerInput controller = new StandardUSBControllerInput(Controllers.getControllers().get(0));
		controllerManager.addController(controller);

		player = new Player();
		player.setEntityController(new PlayerController(player, controller));
		world.addEntity(player);

		final Vector2[] positions = new Vector2[] { new Vector2(1, 1), new Vector2(-1, 1), new Vector2(-1, -1), new Vector2(1, -1)};
		final int amount = 4;
		final float spacing = world.getTrack().getTotalDistance() / amount;
		for(int i = 0; i < amount; i++){
			int sign = ((i % 2) * 2) - 1;
			float trackDistanceAway = sign * ((i / 2) * spacing);
			Shark shark = new Shark(i * 800, new Vector2(), 0);
			shark.setEntityController(new SharkAIController(shark, player, trackDistanceAway, sign));
			Vector2 location = positions[i];
			shark.setLocation(location);
			shark.setRotation(location.angle());
			world.addEntity(shark);

		}

		Cherry cherry = new Cherry(world.getTrack().getTotalDistance() * .5f);
		world.addEntity(cherry);

//		Resources.INTRO.play();
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
