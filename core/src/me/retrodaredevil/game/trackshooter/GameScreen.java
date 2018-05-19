package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import me.retrodaredevil.game.input.StandardUSBControllerInput;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.entity.player.PlayerController;
import me.retrodaredevil.game.trackshooter.entity.powerup.Cherry;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;
import me.retrodaredevil.game.trackshooter.render.WorldViewport;
import me.retrodaredevil.game.trackshooter.world.World;
import me.retrodaredevil.input.ControllerManager;

public class GameScreen extends ScreenAdapter {

	private Player player;
	private Stage stage;
	private World world;

	private ControllerManager controllerManager = new ControllerManager();

	public GameScreen(){
		this.player = new Player();
		this.world = new World(new GameLevelGetter(player), 18, 18);
		this.stage = new Stage(new WorldViewport(world));

		StandardUSBControllerInput controller = new StandardUSBControllerInput(Controllers.getControllers().get(0));
		controllerManager.addController(controller);

		player.setEntityController(new PlayerController(player, controller));
		world.addEntity(player);


		Cherry cherry = new Cherry(world.getTrack().getTotalDistance() * .5f);
		world.addEntity(cherry);
//		System.out.println("initial entities: " + world.getEntities());

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
