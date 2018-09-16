package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import me.retrodaredevil.controller.ControllerManager;
import me.retrodaredevil.controller.DefaultControllerManager;
import me.retrodaredevil.controller.SimpleControllerPart;
import me.retrodaredevil.controller.types.StandardControllerInput;
import me.retrodaredevil.game.input.ChangeableGameInput;
import me.retrodaredevil.game.input.GameInputs;
import me.retrodaredevil.game.input.ControllerGameInput;
import me.retrodaredevil.game.input.GameInput;
import me.retrodaredevil.game.input.StandardUSBControllerInput;
import me.retrodaredevil.game.input.UsableGameInput;
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
//		Skin uiSkin = new Skin(Gdx.files.internal("skins/ui/uiskin.json"));
		Skin uiSkin = new Skin(Gdx.files.internal("skins/sgx/sgx-ui.json"));
		renderObject = new RenderObject(batch, skin, uiSkin);
		renderParts = new RenderParts(new Background(renderObject), new OptionMenu(renderObject), new Overlay(renderObject), new InputMultiplexer());
		controllerManager = new DefaultControllerManager();
		for(Iterator<Controller> it = new Array.ArrayIterator<>(Controllers.getControllers()); it.hasNext();){
			Controller controller = it.next();

			UsableGameInput controllerInput = new ControllerGameInput(new StandardUSBControllerInput(controller));
//			inputs.add(controllerInput);
			// TODO Make using ChangeableGameInput here useful
			GameInput realGameInput = new ChangeableGameInput(Arrays.asList(controllerInput));
			inputs.add(realGameInput);
			controllerManager.addController(controllerInput);
			controllerManager.addController(realGameInput);

		}
		if(inputs.isEmpty()) { // use keyboard and mouse as a last resort
			List<UsableGameInput> gameInputs = new ArrayList<>();
			if(Gdx.app.getType() == Application.ApplicationType.Android){
				if(Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope)) {
					gameInputs.add(GameInputs.createTouchGyroInput());
				}
				gameInputs.add(GameInputs.createHiddenJoystickInput());
			}
			gameInputs.add(GameInputs.createKeyboardInput());
//			inputs.add(keyboardInput);
			GameInput realGameInput = new ChangeableGameInput(gameInputs);
			inputs.add(realGameInput);
			for(UsableGameInput input : gameInputs){
				controllerManager.addController(input);
			}
			controllerManager.addController(realGameInput);

		}
		for(GameInput input : inputs) {
			renderParts.getOptionsMenu().loadControllerConfiguration(input);
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
