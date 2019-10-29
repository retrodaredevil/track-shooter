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
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import me.retrodaredevil.controller.MutableControlConfig;
import me.retrodaredevil.controller.PartUpdater;
import me.retrodaredevil.controller.gdx.ControllerProviders;
import me.retrodaredevil.controller.gdx.GdxControllerPartCreator;
import me.retrodaredevil.controller.implementations.BaseExtremeFlightJoystickControllerInput;
import me.retrodaredevil.controller.implementations.BaseLogitechAttack3JoystickControllerInput;
import me.retrodaredevil.controller.implementations.BaseStandardControllerInput;
import me.retrodaredevil.controller.implementations.ControllerPartCreator;
import me.retrodaredevil.controller.implementations.mappings.DefaultExtremeFlightJoystickInputCreator;
import me.retrodaredevil.controller.implementations.mappings.DefaultLogitechAttack3JoystickInputCreator;
import me.retrodaredevil.controller.implementations.mappings.DefaultStandardControllerInputCreator;
import me.retrodaredevil.controller.implementations.mappings.PS4StandardControllerInputCreator;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionTracker;
import me.retrodaredevil.controller.options.OptionValue;
import me.retrodaredevil.controller.options.OptionValues;
import me.retrodaredevil.game.trackshooter.account.AccountManager;
import me.retrodaredevil.game.trackshooter.account.achievement.AchievementHandler;
import me.retrodaredevil.game.trackshooter.account.multiplayer.AccountMultiplayer;
import me.retrodaredevil.game.trackshooter.input.ChangeableGameInput;
import me.retrodaredevil.game.trackshooter.input.ControllerGameInput;
import me.retrodaredevil.game.trackshooter.input.GameInput;
import me.retrodaredevil.game.trackshooter.input.GameInputs;
import me.retrodaredevil.game.trackshooter.input.RumbleAnalogControl;
import me.retrodaredevil.game.trackshooter.input.UsableGameInput;
import me.retrodaredevil.game.trackshooter.render.RenderObject;
import me.retrodaredevil.game.trackshooter.render.RenderParts;
import me.retrodaredevil.game.trackshooter.render.parts.ArrowRenderer;
import me.retrodaredevil.game.trackshooter.render.parts.Background;
import me.retrodaredevil.game.trackshooter.render.parts.OptionMenu;
import me.retrodaredevil.game.trackshooter.render.parts.Overlay;
import me.retrodaredevil.game.trackshooter.render.parts.TouchpadRenderer;
import me.retrodaredevil.game.trackshooter.save.SaveObject;
import me.retrodaredevil.game.trackshooter.sound.OptionValueVolumeControl;
import me.retrodaredevil.game.trackshooter.sound.VolumeControl;
import me.retrodaredevil.game.trackshooter.util.GdxControllerTester;
import me.retrodaredevil.game.trackshooter.util.ImmutableConfigurableObject;
import me.retrodaredevil.game.trackshooter.util.PreferencesGetter;
import me.retrodaredevil.game.trackshooter.util.Resources;

import static java.util.Objects.requireNonNull;

public class GameMain extends Game {
	/** Should only be used during initialization. Not for static access*/
	public static final PreferencesGetter SCORE_PREFERENCSE_GETTER = () -> Gdx.app.getPreferences("score");

	private final PreferencesGetter scorePreferencesGetter;
	private final RumbleAnalogControl rumbleAnalogControl;
	private final AccountObject accountObject;

	private RenderObject renderObject;
	private SaveObject saveObject;
	private VolumeControl volumeControl;
	private RenderParts renderParts;

	private final PartUpdater controllerUpdater = new PartUpdater();
	private final MutableControlConfig controllerConfig = new MutableControlConfig();
	private List<GameInput> inputs = new ArrayList<>();

	public GameMain(PreferencesGetter scorePreferencesGetter, RumbleAnalogControl rumbleAnalogControl, AccountObject accountObject){
		this.scorePreferencesGetter = scorePreferencesGetter;
		this.rumbleAnalogControl = requireNonNull(rumbleAnalogControl);
		this.accountObject = requireNonNull(accountObject);
	}

	public GameMain(PreferencesGetter scorePreferencesGetter){
		this(
				scorePreferencesGetter, RumbleAnalogControl.Defaults.UNSUPPORTED_ANALOG,
				new AccountObject(AccountManager.Defaults.NO_MANAGER, AchievementHandler.Defaults.UNSUPPORTED_HANDLER, AccountMultiplayer.Defaults.NOT_SUPPORTED)
		);
	}


	@Override
	public void create () {
		GdxControllerTester.initialize();
		Batch batch = new SpriteBatch();
		Skin skin = new Skin(Gdx.files.internal("skins/main/skin.json"));
		Resources.loadToSkin(skin);
		Skin uiSkin = new Skin(Gdx.files.internal("skins/sgx/sgx-ui.json"));
		Skin arcadeSkin = new Skin(Gdx.files.internal("skins/arcade/arcade-ui.json"));
		renderObject = new RenderObject(batch, skin, uiSkin, arcadeSkin);
		saveObject = new SaveObject();
		OptionValue volumeOption = OptionValues.createAnalogRangedOptionValue(0, 1, 1);
		volumeControl = new OptionValueVolumeControl(volumeOption);
		OptionMenu optionMenu = new OptionMenu(renderObject, saveObject, Collections.singletonList(
				new ImmutableConfigurableObject(new ControlOption("Volume", "The volume percentage", "controls.main.options.volume", volumeOption))
		));
		renderParts = new RenderParts(new Background(renderObject), optionMenu,
				new Overlay(renderObject), new TouchpadRenderer(renderObject), new ArrowRenderer(renderObject), new InputMultiplexer());
		{
			boolean firstRun = true;
			for (Iterator<Controller> it = new Array.ArrayIterator<>(Controllers.getControllers()); it.hasNext(); ) {
				Controller controller = it.next();
				String controllerName = controller.getName().toLowerCase();

				// ====== Controller =====
				final ControllerPartCreator controllerPartCreator = new GdxControllerPartCreator(ControllerProviders.wrap(controller), true);
				final UsableGameInput controllerInput;
				if(controllerName.contains("extreme") && controllerName.contains("logitech")){
					controllerInput = new ControllerGameInput(new BaseExtremeFlightJoystickControllerInput(
							new DefaultExtremeFlightJoystickInputCreator(),
							controllerPartCreator
					));
				} else if(controllerName.contains("attack") && controllerName.contains("logitech")) {
					controllerInput = new ControllerGameInput(new BaseLogitechAttack3JoystickControllerInput(
							new DefaultLogitechAttack3JoystickInputCreator(),
							controllerPartCreator
					));
				} else if(controllerName.contains("ps4")){
					controllerInput = new ControllerGameInput(new BaseStandardControllerInput(
							new PS4StandardControllerInputCreator(),
							controllerPartCreator,
							OptionValues.createImmutableBooleanOptionValue(false),
							OptionValues.createImmutableBooleanOptionValue(false)
					), new OptionTracker());
				} else {
					OptionValue physicalLocationsSwapped = OptionValues.createBooleanOptionValue(false);
					OptionTracker tracker = new OptionTracker();
					tracker.add(new ControlOption(
							"Are physical face button locations inverted",
							"Should be checked for some nintendo controllers where the select button is the right face button",
							"controller.main.controller." + controller.getName() + ".layout.physical_face_buttons_inverted",
							physicalLocationsSwapped
					));
					controllerInput = new ControllerGameInput(new BaseStandardControllerInput(
							new DefaultStandardControllerInputCreator(),
							controllerPartCreator,
							physicalLocationsSwapped,
							OptionValues.createImmutableBooleanOptionValue(false)
					), tracker);
				}
				controllerUpdater.addPartAssertNotPresent(controllerInput);

				// ====== Physical Inputs (Keyboards, on screen) (Only add if we haven't already)
				final Collection<? extends UsableGameInput> addBefore = firstRun ? getPhysicalInputs(rumbleAnalogControl) : Collections.emptySet();

				// ==== Inputs to go into our ChangeableGameInput
				final List<UsableGameInput> usableInputs = new ArrayList<>(addBefore);
				usableInputs.add(controllerInput);

				// ==== Create our ChangeableGameInput and add it to our official inputs
				GameInput realGameInput = new ChangeableGameInput(usableInputs);
				controllerUpdater.addPartAssertNotPresent(realGameInput);
				inputs.add(realGameInput);

				firstRun = false;
			}
		}
		if(inputs.isEmpty()) { // if there were no controllers, add inputs from getPhysicalInputs()
			List<UsableGameInput> gameInputs = getPhysicalInputs(rumbleAnalogControl);
			GameInput realGameInput = new ChangeableGameInput(gameInputs);
			controllerUpdater.addPartAssertNotPresent(realGameInput);
			inputs.add(realGameInput);
		}

		// Load all saved player control configurations
		{
			int i = 0;
			for (GameInput input : inputs) {
				saveObject.getOptionSaver().loadControllerConfiguration(i, input);
				i++;
			}
		}
		// Load global configuration, not player or control specific
		optionMenu.loadGlobalConfiguration();

		Gdx.graphics.setTitle("Track Shooter");
		startScreen();
	}
	private List<UsableGameInput> getPhysicalInputs(RumbleAnalogControl rumbleAnalogControl){

		List<UsableGameInput> gameInputs = new ArrayList<>();
		if(Gdx.app.getType() == Application.ApplicationType.Android){
			gameInputs.add(GameInputs.createVirtualJoystickInput(renderParts, rumbleAnalogControl));
			if(Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope)) {
				gameInputs.add(GameInputs.createTouchGyroInput(renderParts, rumbleAnalogControl));
			}
		}
		gameInputs.add(GameInputs.createMouseAndKeyboardInput());
		return gameInputs;
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
		controllerUpdater.updateParts(controllerConfig);
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
		setScreen(new StartScreen(inputs, renderObject, renderParts, accountObject, volumeControl));
	}

	@Override
	public void dispose() {
		super.dispose();
		renderObject.dispose();
		renderParts.dispose();
		System.out.println("dispose() called on GameMain!");
	}
}
