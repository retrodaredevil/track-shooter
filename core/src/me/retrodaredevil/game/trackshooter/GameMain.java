package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.retrodaredevil.controller.ControllerManager;
import me.retrodaredevil.controller.DefaultControllerManager;
import me.retrodaredevil.controller.SimpleControllerPart;
import me.retrodaredevil.controller.types.StandardControllerInput;
import me.retrodaredevil.game.input.DefaultGameInput;
import me.retrodaredevil.game.input.GameInput;
import me.retrodaredevil.game.input.StandardUSBControllerInput;
import me.retrodaredevil.game.trackshooter.render.parts.Background;
import me.retrodaredevil.game.trackshooter.render.parts.Overlay;
import me.retrodaredevil.game.trackshooter.render.parts.options.OptionMenu;
import me.retrodaredevil.game.trackshooter.util.Resources;

public class GameMain extends Game {

	private RenderObject renderObject;
	private RenderParts renderParts;

	private ControllerManager controllerManager;
	private List<GameInput> inputs = new ArrayList<>();


	@Override
	public void create () {
		SimpleControllerPart.setDebugChangeInParent(true);

		Batch batch = new SpriteBatch();
		Skin skin = new Skin(Gdx.files.internal("skins/main/skin.json"));
		Resources.loadToSkin(skin);
		Skin uiSkin = new Skin(Gdx.files.internal("skins/ui/uiskin.json"));
		renderObject = new RenderObject(batch, skin, uiSkin);
		renderParts = new RenderParts(new Background(renderObject), new OptionMenu(renderObject), new Overlay(renderObject));
		controllerManager = new DefaultControllerManager();
		for(Iterator<Controller> it = new Array.ArrayIterator<>(Controllers.getControllers()); it.hasNext();){
			Controller controller = it.next();
			StandardControllerInput standardController = new StandardUSBControllerInput(controller);
//			controllerManager.addController(standardController);

			GameInput controllerInput = new DefaultGameInput(standardController);
			inputs.add(controllerInput);
			controllerManager.addController(controllerInput);
		}
		if(inputs.isEmpty()) { // use keyboard and mouse as a last resort
			GameInput keyboardInput = new DefaultGameInput();
			inputs.add(keyboardInput);
			controllerManager.addController(keyboardInput);
		}

//		Gdx.app.setLogLevel(Application.LOG_ERROR);
		Gdx.graphics.setTitle("Track Shooter");
		startScreen();
	}

	@Override
	public void render() {
		controllerManager.update();
		super.render(); // renders current screen
//		for(GameInput input : inputs){
//			if(!input.isConnected()){
//				System.out.println("input: " + input + " is disconnected!");
//			}
//		}

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
		setScreen(new StartScreen(inputs.get(0), renderObject, renderParts)); // TODO all inputs
	}
	private void gameScreen(){
		setScreen(new GameScreen(inputs, renderObject, renderParts));
	}

	@Override
	public void dispose() {
		super.dispose();
		renderObject.dispose();
		renderParts.dispose();
		System.out.println("dispose() called on GameMain!");
	}
}
