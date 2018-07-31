package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.retrodaredevil.controller.ControllerManager;
import me.retrodaredevil.controller.DefaultControllerManager;
import me.retrodaredevil.game.input.DefaultGameInput;
import me.retrodaredevil.game.input.GameInput;
import me.retrodaredevil.game.input.StandardUSBControllerInput;
import me.retrodaredevil.game.trackshooter.overlay.Overlay;

public class GameMain extends Game {

	private Batch batch; // used for initialization and begin() and end() methods are only used when used with RenderUtil.drawStage()
	private Overlay overlay; // not handled in this class, passed around to current screen

	private ControllerManager controllerManager;
	private List<GameInput> inputs = new ArrayList<>();


	@Override
	public void create () {
	    batch = new SpriteBatch();
		overlay = new Overlay(batch);
		controllerManager = new DefaultControllerManager();
		for(Controller controller : Controllers.getControllers()){
			StandardUSBControllerInput standardController = new StandardUSBControllerInput(controller);
			controllerManager.addController(standardController);

			GameInput controllerInput = new DefaultGameInput(standardController);
			inputs.add(controllerInput);
			controllerManager.addController(controllerInput);
		}
		if(inputs.size() == 0) {
			GameInput keyboardInput = new DefaultGameInput();
			inputs.add(keyboardInput);
			controllerManager.addController(keyboardInput);
		}

		Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.graphics.setTitle("Track Shooter");
		startScreen();
	}

	@Override
	public void render() {
		controllerManager.update();
		super.render();

		Screen screen = getScreen(); // TODO create our own screen interface instead of this ugly mess
		if(screen instanceof GameScreen){
			GameScreen game = (GameScreen) screen;
			if(game.isGameCompletelyOver()){
				startScreen();
			}
		} else if(screen instanceof StartScreen){
			StartScreen startScreen = (StartScreen) screen;
			if(startScreen.isReadyToStart()){
				gameScreen();
			}
		}
//		Gdx.graphics.setTitle("Track Shooter - FPS:" + Gdx.graphics.getFramesPerSecond());
	}
	private void startScreen(){
		setScreen(new StartScreen(inputs.get(0), overlay, batch)); // TODO all inputs
	}
	private void gameScreen(){
		setScreen(new GameScreen(inputs, overlay, batch));
	}

	@Override
	public void dispose() {
		super.dispose();
		overlay.dispose();
		batch.dispose();
		System.out.println("dispose() called on GameMain!");
	}
}
