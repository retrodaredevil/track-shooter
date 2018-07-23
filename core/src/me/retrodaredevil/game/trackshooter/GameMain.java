package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

import me.retrodaredevil.controller.ControllerManager;
import me.retrodaredevil.controller.SimpleControllerManager;
import me.retrodaredevil.game.input.DefaultGameInput;
import me.retrodaredevil.game.input.GameInput;
import me.retrodaredevil.game.input.StandardUSBControllerInput;

public class GameMain extends Game {

	private ControllerManager controllerManager;
	private GameInput gameInput;

	@Override
	public void create () {
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
		setScreen(new StartScreen(gameInput));
	}

	@Override
	public void render() {
		controllerManager.update();

		super.render();
		Screen screen = getScreen();
		if(screen instanceof GameScreen){
			GameScreen game = (GameScreen) screen;
			if(game.isGameCompletelyOver()){
				setScreen(new StartScreen(gameInput));
			}
		} else if(screen instanceof StartScreen){
			StartScreen startScreen = (StartScreen) screen;
			if(startScreen.isReadyToStart()){
				setScreen(new GameScreen(gameInput));
			}
		}
		Gdx.graphics.setTitle("Track Shooter - FPS:" + Gdx.graphics.getFramesPerSecond());
	}
}
