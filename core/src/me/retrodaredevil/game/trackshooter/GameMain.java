package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import me.retrodaredevil.game.input.StandardUSBControllerInput;
import me.retrodaredevil.input.ControlConfig;
import me.retrodaredevil.input.JoystickPart;
import me.retrodaredevil.input.StandardControllerInput;

public class GameMain extends Game {

	private Stage stage;

	private Image image;

	private StandardControllerInput controller;
	private ControlConfig controlConfig = new ControlConfig();


	@Override
	public void create () {
		stage = new Stage(new ScreenViewport());
		image = new Image(new Texture("badlogic.jpg"));

		stage.addActor(image);
		controller = new StandardUSBControllerInput(Controllers.getControllers().get(0));
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

	}

	@Override
	public void render () {

		controller.update(controlConfig);
		JoystickPart joy = controller.rightJoy();
		Gdx.app.debug("x","" + joy.getX());
		Gdx.app.debug("y", "" + joy.getY());
		Gdx.app.debug("R Trigger", "" + controller.rightTrigger().getPosition());
		Gdx.app.debug("abstract delta", "" + Gdx.graphics.getDeltaTime());


		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
//		System.out.println(controller.getAxis(0) + " : " + controller.getAxis(1));
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}

	@Override
	public void dispose () {
		stage.dispose();
	}
}
