package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import me.retrodaredevil.controller.ControllerManager;
import me.retrodaredevil.controller.SimpleControllerManager;
import me.retrodaredevil.game.input.DefaultGameInput;
import me.retrodaredevil.game.input.GameInput;
import me.retrodaredevil.game.input.StandardUSBControllerInput;
import me.retrodaredevil.game.trackshooter.overlay.Overlay;

public class GameMain extends Game {

	private Batch batch;
	private Overlay overlay;

	private ControllerManager controllerManager;
	private GameInput gameInput;


	@Override
	public void create () {
	    batch = new SpriteBatch();
		controllerManager = new SimpleControllerManager();
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		if(Controllers.getControllers().size > 0){
			StandardUSBControllerInput controller = new StandardUSBControllerInput(Controllers.getControllers().first());
			controllerManager.addController(controller);
			gameInput = new DefaultGameInput(controller);
		} else {
			gameInput = new DefaultGameInput();
		}
		controllerManager.addController(gameInput);

		overlay = new Overlay(batch);
		setScreen(new StartScreen(gameInput, overlay));
        Gdx.graphics.setTitle("Track Shooter");
	}

	@Override
	public void render() {
		controllerManager.update();

		super.render();
		Screen screen = getScreen();
		if(screen instanceof GameScreen){
			GameScreen game = (GameScreen) screen;
			if(game.isGameCompletelyOver()){
				setScreen(new StartScreen(gameInput, overlay));
			}
		} else if(screen instanceof StartScreen){
			StartScreen startScreen = (StartScreen) screen;
			if(startScreen.isReadyToStart()){
				setScreen(new GameScreen(gameInput, overlay, batch));
			}
		}
//		Gdx.graphics.setTitle("Track Shooter - FPS:" + Gdx.graphics.getFramesPerSecond());
	}

	@Override
	public void dispose() {
		super.dispose();
		overlay.dispose();
		batch.dispose();
		System.out.println("dispose() called on GameMain!");
	}
}
