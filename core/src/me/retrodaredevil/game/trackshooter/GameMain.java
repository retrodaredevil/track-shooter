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
import me.retrodaredevil.input.JoystickInput;

import java.awt.geom.Point2D;

public class GameMain extends Game {

	private Stage stage;

	private Image image;
	private Controller controller;

	private double x = 0, y = 0;

	private long last;

	@Override
	public void create () {
		stage = new Stage(new ScreenViewport());
		image = new Image(new Texture("badlogic.jpg"));

		stage.addActor(image);
		controller = Controllers.getControllers().get(0);

	}

	@Override
	public void render () {
		final long time = System.currentTimeMillis();
		long delta = time - last;
		if(delta > 500){
			delta = 500;
		}
		last = time;
		Point2D joy = JoystickInput.getScaled(
				Math.pow(controller.getAxis(0), 3),
				-Math.pow(controller.getAxis(1), 3),
				null);
//		Point2D joy = new Point2D.Double(controller.getAxis(0), -controller.getAxis(1));
		double joyX = joy.getX();
		double joyY = joy.getY();
		if(Math.abs(joyX) > .001 || Math.abs(joyY) > .001) {
			double addX = joyX * delta * (200.0 / 1000.0);
			double addY = joyY * delta * (200.0 / 1000.0);
			x += addX;
			y += addY;
		}
		System.out.println("joy: " + joy.toString());
		System.out.println("x: " + x + " y: " + y);

		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		image.setPosition((int) x,(int) y);
		stage.act();
		stage.draw();
//		System.out.println(controller.getAxis(0) + " : " + controller.getAxis(1));
	}
	
	@Override
	public void dispose () {
		stage.dispose();
	}
}
