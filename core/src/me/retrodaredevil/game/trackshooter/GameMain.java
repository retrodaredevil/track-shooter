package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import me.retrodaredevil.game.input.StandardUSBControllerInput;
import me.retrodaredevil.input.ControlConfig;
import me.retrodaredevil.input.ControllerInput;
import me.retrodaredevil.input.JoystickInput;
import me.retrodaredevil.input.StandardControllerInput;

import java.awt.geom.Point2D;

public class GameMain extends Game {

	private Stage stage;

	private Image image;

	private StandardControllerInput controller;
	private ControlConfig controlConfig = new ControlConfig();

	private long last;

	@Override
	public void create () {
		stage = new Stage(new ScreenViewport());
		image = new Image(new Texture("badlogic.jpg"));

		stage.addActor(image);
		controller = new StandardUSBControllerInput(Controllers.getControllers().get(0));


	}

	@Override
	public void render () {
		final long time = System.currentTimeMillis();
		long delta = time - last;
		if(delta > 500){
			delta = 500;
		}
		last = time;

		controller.update(controlConfig);
		JoystickInput leftJoy = controller.rightJoy();
		System.out.println("x: " + leftJoy.getX() + " y: " + leftJoy.getY());
		System.out.println("R Trigger: " + controller.rightTrigger().getPosition());

		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
//		System.out.println(controller.getAxis(0) + " : " + controller.getAxis(1));
	}
	
	@Override
	public void dispose () {
		stage.dispose();
	}
}
