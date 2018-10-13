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
import me.retrodaredevil.game.trackshooter.input.ChangeableGameInput;
import me.retrodaredevil.game.trackshooter.input.GameInputs;
import me.retrodaredevil.game.trackshooter.input.ControllerGameInput;
import me.retrodaredevil.game.trackshooter.input.GameInput;
import me.retrodaredevil.game.trackshooter.input.StandardUSBControllerInput;
import me.retrodaredevil.game.trackshooter.input.UsableGameInput;
import me.retrodaredevil.game.trackshooter.render.RenderObject;
import me.retrodaredevil.game.trackshooter.render.RenderParts;
import me.retrodaredevil.game.trackshooter.render.parts.Background;
import me.retrodaredevil.game.trackshooter.render.parts.Overlay;
import me.retrodaredevil.game.trackshooter.render.parts.OptionMenu;
import me.retrodaredevil.game.trackshooter.render.parts.TouchpadRenderer;
import me.retrodaredevil.game.trackshooter.save.SaveObject;
import me.retrodaredevil.game.trackshooter.util.Resources;

public class GameMain extends Game {

	private RenderObject renderObject;
	private SaveObject saveObject;
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
		Skin arcadeSkin = new Skin(Gdx.files.internal("skins/arcade/arcade-ui.json"));
		renderObject = new RenderObject(batch, skin, uiSkin, arcadeSkin);
		saveObject = new SaveObject();
		renderParts = new RenderParts(new Background(renderObject), new OptionMenu(renderObject, saveObject),
				new Overlay(renderObject), new TouchpadRenderer(renderObject), new InputMultiplexer());
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
				gameInputs.add(GameInputs.createVirtualJoystickInput(renderParts));
				if(Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope)) {
					gameInputs.add(GameInputs.createTouchGyroInput());
				}
			}
			gameInputs.add(GameInputs.createKeyboardInput());
			for(UsableGameInput input : gameInputs){
				controllerManager.addController(input);
			}

			GameInput realGameInput = new ChangeableGameInput(gameInputs);
			inputs.add(realGameInput);
			controllerManager.addController(realGameInput);

		}
		for(GameInput input : inputs) {
			saveObject.getOptionSaver().loadControllerConfiguration(input);
		}

//		Gdx.app.setLogLevel(Application.LOG_ERROR);
		Gdx.graphics.setTitle("Track Shooter");
		startScreen();
	}

	@Override
	public UsableScreen getScreen() {
		return (UsableScreen) super.getScreen();
	}

	@Override
	public void setScreen(Screen screen) {
		if (!(screen instanceof UsableScreen)) {
			throw new IllegalArgumentException("The screen must be a UsableScreen! got: " + screen);
		}
		UsableScreen old = getScreen();
		if(old != null) old.dispose();
		super.setScreen(screen);
	}

	@Override
	public void resize(int width, int height) {
		Gdx.gl.glViewport(0, 0, width, height);
		super.resize(width, height);
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

		UsableScreen screen = getScreen();
		if(screen.isScreenDone()){
			setScreen(screen.createNextScreen());
		}
	}
	private void startScreen(){
		setScreen(new StartScreen(inputs, renderObject, renderParts));
	}

	@Override
	public void dispose() {
		super.dispose();
		renderObject.dispose();
		renderParts.dispose();
		System.out.println("dispose() called on GameMain!");
	}
}
