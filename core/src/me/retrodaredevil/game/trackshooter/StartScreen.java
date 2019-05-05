package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import me.retrodaredevil.game.trackshooter.achievement.AchievementHandler;
import me.retrodaredevil.game.trackshooter.input.GameInput;
import me.retrodaredevil.game.trackshooter.render.*;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;
import me.retrodaredevil.game.trackshooter.render.selection.SelectionMenuRenderComponent;
import me.retrodaredevil.game.trackshooter.render.selection.SingleOption;
import me.retrodaredevil.game.trackshooter.render.selection.options.GroupedSelectionSingleOption;
import me.retrodaredevil.game.trackshooter.render.selection.options.PlainActorSingleOption;
import me.retrodaredevil.game.trackshooter.render.selection.options.providers.BasicOptionProvider;
import me.retrodaredevil.game.trackshooter.render.selection.options.providers.MultiActorOptionProvider;
import me.retrodaredevil.game.trackshooter.render.selection.tables.PlainTable;
import me.retrodaredevil.game.trackshooter.sound.VolumeControl;
import me.retrodaredevil.game.trackshooter.util.Constants;

import java.util.*;

public class StartScreen extends ScreenAdapter implements UsableScreen{
	private static final float DEMO_GAME_INIT_IDLE = 45;

	private final List<GameInput> gameInputs;
	private final GameInput gameInput;
	private final int gameInputPlayerIndex;
	private final RenderObject renderObject;
	private final RenderParts renderParts;
	private final AchievementHandler achievementHandler;
	private final VolumeControl volumeControl;
	private UsableScreen nextScreen = null;

	private final Stage uiStage;
	private final Renderable menuRenderable;
	private final Renderable tipsRenderable;

	private final Button startButton;
	private final Button optionsButton;
	private final Button creditsButton;
	/** The sign in button or null*/
	private final TextButton signInButton;
	private final Button showAchievements;
	private final Button showLeaderboards;
	private boolean optionsDown = false;
	private boolean signInDown = false;
	private boolean showAchievementsDown = false;
	private boolean showLeaderboardsDown = false;
	private float idleTime = 0;

	public StartScreen(List<GameInput> gameInputs, RenderObject renderObject, RenderParts renderParts, AchievementHandler achievementHandler, VolumeControl volumeControl){
		this.gameInputs = Collections.unmodifiableList(new ArrayList<>(gameInputs)); // copy gameInputs for safe keeping
		this.gameInputPlayerIndex = 0;
		this.gameInput = gameInputs.get(gameInputPlayerIndex);
		this.renderObject = Objects.requireNonNull(renderObject);
		this.renderParts = Objects.requireNonNull(renderParts);
		this.achievementHandler = achievementHandler;
		this.volumeControl = volumeControl;
		this.uiStage = new Stage(new FitViewport(640, 640), renderObject.getBatch());

		final TextButton.TextButtonStyle style = renderObject.getUISkin().get(TextButton.TextButtonStyle.class);
		startButton = new TextButton("start", style); // do stuff with getStartButton.getStyle()
		optionsButton = new TextButton("options", style);
		creditsButton = new TextButton("info", style);
		final List<Button> buttons = new ArrayList<>(Arrays.asList(startButton, optionsButton, creditsButton));
		if(achievementHandler.isNeedsSignIn()){
			signInButton = new TextButton("sign in", style);
			buttons.add(signInButton);
		} else {
			signInButton = null;
		}
		final List<SingleOption> horizontalOptions = new ArrayList<>();
		if(achievementHandler.isEverAbleToShowAchievements()){
			showAchievements = new TextButton("achievements", style);
//			buttons.add(showAchievements);
			horizontalOptions.add(new PlainActorSingleOption(showAchievements, Constants.START_SCREEN_BUTTON_SIZE.withWidthPercent(.5f)));
		} else {
			showAchievements = null;
		}
		if(achievementHandler.isEverAbleToShowLeaderboards()){
			showLeaderboards = new TextButton("leaderboards", style);
//			buttons.add(showLeaderboards);
			horizontalOptions.add(new PlainActorSingleOption(showLeaderboards, Constants.START_SCREEN_BUTTON_SIZE.withWidthPercent(.5f)));
		} else {
			showLeaderboards = null;
		}

		// this is initialized after each button because it uses them
		this.menuRenderable = new ComponentRenderable(new SelectionMenuRenderComponent(
				renderObject,
                gameInputPlayerIndex,
				gameInput,
				new PlainTable(),
				Arrays.asList(
						new MultiActorOptionProvider(Constants.START_SCREEN_BUTTON_SIZE, buttons.toArray(new Button[0])),
						new BasicOptionProvider(
								new GroupedSelectionSingleOption(Constants.START_SCREEN_BUTTON_SIZE, true, Collections.singleton(new BasicOptionProvider(horizontalOptions)))
						)
				),
				() -> {} // do nothing on back button
		));
		this.tipsRenderable = new ComponentRenderable(new TipsRenderComponent());

	}
	private Renderer createRenderer(){
		return new Renderer(renderObject.getBatch(), uiStage)
				.addRenderable(renderParts.getBackground())
				.addMainStage()
				.addRenderable(tipsRenderable)
				.addRenderable(renderParts.getOptionsMenu().isMenuOpen() ? renderParts.getOptionsMenu() : menuRenderable)
				.addRenderable(renderParts.getOverlay());
	}
	@Override
	public void render(float delta) {
		idleTime += delta;
		if(gameInput.getFireButton().isPressed() || gameInput.getEnterButton().isPressed()
				|| gameInput.getMainJoystick().getMagnitude() > .5 || Gdx.input.justTouched()
				|| renderParts.getOptionsMenu().isMenuOpen()){
			idleTime = 0;
		}

		if(gameInput.getStartButton().isPressed() || startButton.isPressed()){
			normalGame();
			return;
		}
		if(!renderParts.getOptionsMenu().isMenuOpen() && (gameInput.getBackButton().isPressed() || idleTime > DEMO_GAME_INIT_IDLE)){
			demoGame();
			return;
		}
		if(creditsButton.isPressed()){
			nextScreen = new CreditsScreen(gameInputs, renderObject, renderParts, achievementHandler, volumeControl);
			return;
		}
		if(optionsDown && !optionsButton.isPressed()){ // just released options button
			renderParts.getOptionsMenu().setToController(gameInputPlayerIndex, gameInput, gameInputPlayerIndex, gameInput);
		}
		optionsDown = optionsButton.isPressed();

		if(signInButton != null && achievementHandler.isNeedsSignIn()){
			final boolean signedIn = achievementHandler.isSignedIn();
			if(signedIn){
				signInButton.setText("sign out");
			} else {
				signInButton.setText("sign in");
			}
			if(signInDown && !signInButton.isPressed()){ // just released sign in
				if(signedIn){
					achievementHandler.logout();
				} else {
					achievementHandler.signIn();
				}
			}
			signInDown = signInButton.isPressed();
		}
		if(showAchievements != null){
			boolean canShow = achievementHandler.isCurrentlyAbleToShowAchievements();
			showAchievements.setVisible(canShow);
			if(canShow){
				if (showAchievementsDown && !showAchievements.isPressed()) { // just released show achievements
					achievementHandler.showAchievements();
				}
				showAchievementsDown = showAchievements.isPressed();
			}
		}
		if(showLeaderboards != null){
			boolean canShow = achievementHandler.isCurrentlyAbleToShowLeaderboards();
			showLeaderboards.setVisible(canShow);
			if(canShow){
				if(showLeaderboardsDown && !showLeaderboards.isPressed()){ // just released show leaderboards
					achievementHandler.showLeaderboards();
				}
				showLeaderboardsDown = showLeaderboards.isPressed();
			}
		}

		renderParts.getOverlay().setPauseVisible(false);
		new InputFocuser()
				.add(new InputFocuser(0).addParallel(renderParts.getTouchpadRenderer()).addParallel(uiStage))
				.add(renderParts.getOptionsMenu()) // may or may not get focus
				.giveFocus(uiStage, renderParts.getInputMultiplexer());


		createRenderer().render(delta);
	}
	private void normalGame(){
		if(nextScreen == null) {
			nextScreen = new GameScreen(gameInputs, renderObject, renderParts, GameScreen.GameType.NORMAL, achievementHandler, volumeControl);
		}
	}
	private void demoGame(){
		if(nextScreen == null) {
			nextScreen = new GameScreen(gameInputs, renderObject, renderParts, GameScreen.GameType.DEMO_AI, achievementHandler, volumeControl);
		}
	}

	@Override
	public void resize(int width, int height) {
		uiStage.getViewport().update(width, height, true);
		renderParts.resize(width, height);
	}

	@Override
	public void dispose() {
		super.dispose();
		uiStage.dispose(); // we created uiStage so dispose it
	}

	@Override
	public boolean isScreenDone() {
		return nextScreen != null && !startButton.isPressed() && !creditsButton.isPressed();
	}

	@Override
	public UsableScreen createNextScreen() {
		return nextScreen;
	}


	private static final String[] TIPS = new String[] {
			"Change your controls in options!",
			"Extra life at 10,000 and every 30,000!",
			"Only 4 shots at a time!",
			"If you save Mr. Spaceship from fire, you'll get bonus points!",
			"If you wait long enough, the AI will start playing the game!",
			"Don't turn your back on the snake!",
			"Shoot the snake's head!",
			"Don't let the snake eat your fruit or power-ups!",
			"Don't shoot the sharks while they're blocking your escape route!",
			"Keep shooting at the Sharks to make them spin longer!",
			"Shoot the starfish once to change its direction!",
			"Open Source!",
			"Made by Joshua Shannon!",
			"Sign in for achievements and leaderboards!"
	};
	private class TipsRenderComponent implements RenderComponent {
		private final Group group = new Table(){{setFillParent(true);}};

		private TipsRenderComponent() {
			Table table = new Table(renderObject.getMainSkin());
			table.setFillParent(true);
			final String tip = TIPS[(int) (Math.random() * TIPS.length)];
			final Label label = new Label(tip, renderObject.getMainSkin(), "game_label", "tips");
			label.setFontScale(Math.min(34.0f / tip.length(), 1)); // scale can only be <= 1
			table.add(label);
			table.center().bottom().padBottom(95);

			group.addActor(table);
		}

		@Override
		public void render(float delta, Stage stage) {
			stage.addActor(group);
		}

		@Override
		public void dispose() {
			group.remove();
		}
	}
}
