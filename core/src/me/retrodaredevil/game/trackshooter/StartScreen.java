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
import me.retrodaredevil.game.trackshooter.account.AccountManager;
import me.retrodaredevil.game.trackshooter.account.Show;
import me.retrodaredevil.game.trackshooter.account.achievement.AchievementHandler;
import me.retrodaredevil.game.trackshooter.account.multiplayer.AccountMultiplayer;
import me.retrodaredevil.game.trackshooter.input.GameInput;
import me.retrodaredevil.game.trackshooter.multiplayer.DisconnectedMultiplayer;
import me.retrodaredevil.game.trackshooter.multiplayer.Multiplayer;
import me.retrodaredevil.game.trackshooter.render.*;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;
import me.retrodaredevil.game.trackshooter.render.selection.SelectionMenuRenderComponent;
import me.retrodaredevil.game.trackshooter.render.selection.SingleOption;
import me.retrodaredevil.game.trackshooter.render.selection.SingleOptionProvider;
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
	private final AccountObject accountObject;
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
	private final Button joinRoom;
	private final Button showInbox;
	private final Button showAchievements;
	private final Button showLeaderboards;
	private boolean optionsDown = false;
	private boolean signInDown = false;
	private boolean joinRoomDown = false;
	private boolean showInboxDown = false;
	private boolean showAchievementsDown = false;
	private boolean showLeaderboardsDown = false;
	private float idleTime = 0;

	public StartScreen(List<GameInput> gameInputs, RenderObject renderObject, RenderParts renderParts, AccountObject accountObject, VolumeControl volumeControl){
		this.gameInputs = Collections.unmodifiableList(new ArrayList<>(gameInputs)); // copy gameInputs for safe keeping
		this.gameInputPlayerIndex = 0;
		this.gameInput = gameInputs.get(gameInputPlayerIndex);
		this.renderObject = Objects.requireNonNull(renderObject);
		this.renderParts = Objects.requireNonNull(renderParts);
		this.accountObject = accountObject;
		this.volumeControl = volumeControl;
		this.uiStage = new Stage(new FitViewport(640, 640), renderObject.getBatch());

		final TextButton.TextButtonStyle style = renderObject.getUISkin().get(TextButton.TextButtonStyle.class);
		startButton = new TextButton("start", style); // do stuff with getStartButton.getStyle()
		optionsButton = new TextButton("options", style);
		creditsButton = new TextButton("info", style);
		if(accountObject.getAccountManager().isEverAbleToSignIn()){
			signInButton = new TextButton("sign in", style);
		} else {
			signInButton = null;
		}
		if(accountObject.getAccountMultiplayer().getShowRoomConfig().isEverAbleToShow()){
			joinRoom = new TextButton("join room", style);
		} else {
			joinRoom = null;
		}
		if(accountObject.getAccountMultiplayer().getShowInbox().isEverAbleToShow()){
			showInbox = new TextButton("invitations", style);
		} else {
			showInbox = null;
		}
		final AchievementHandler achievementHandler = accountObject.getAchievementHandler();
		if(achievementHandler.getShowAchievements().isEverAbleToShow()){
			showAchievements = new TextButton("achievements", style);
		} else {
			showAchievements = null;
		}
		if(achievementHandler.getShowLeaderboards().isEverAbleToShow()){
			showLeaderboards = new TextButton("leaderboards", style);
		} else {
			showLeaderboards = null;
		}

		List<SingleOptionProvider> providerList = new ArrayList<>();
		providerList.add(new MultiActorOptionProvider(Constants.START_SCREEN_BUTTON_SIZE, startButton));
		{
			SingleOption options = new PlainActorSingleOption(optionsButton, Constants.START_SCREEN_BUTTON_SIZE.withWidthPercent(.5f));
			SingleOption credits = new PlainActorSingleOption(creditsButton, Constants.START_SCREEN_BUTTON_SIZE.withWidthPercent(.5f));
			providerList.add(new BasicOptionProvider(
					new GroupedSelectionSingleOption(Constants.START_SCREEN_BUTTON_SIZE, true, Collections.singleton(new BasicOptionProvider(
							options,
							credits
					)))
			));
		}
		if(signInButton != null){
			providerList.add(new MultiActorOptionProvider(Constants.START_SCREEN_BUTTON_SIZE, signInButton));
		}
		if(showAchievements != null && showLeaderboards != null){ // both are available
			SingleOption achievements = new PlainActorSingleOption(showAchievements, Constants.START_SCREEN_BUTTON_SIZE.withWidthPercent(.5f));
			SingleOption leaderboards = new PlainActorSingleOption(showLeaderboards, Constants.START_SCREEN_BUTTON_SIZE.withWidthPercent(.5f));
			providerList.add(new BasicOptionProvider(
					new GroupedSelectionSingleOption(Constants.START_SCREEN_BUTTON_SIZE, true, Collections.singleton(new BasicOptionProvider(
							achievements, leaderboards
					)))
			));
		} else if(showAchievements != null || showLeaderboards != null){ // only one of these is available
			providerList.add(new MultiActorOptionProvider(
					Constants.START_SCREEN_BUTTON_SIZE,
					showAchievements != null ? showAchievements : showLeaderboards
			));
		}
		if(joinRoom != null && showInbox != null){ // both are available
			SingleOption join = new PlainActorSingleOption(joinRoom, Constants.START_SCREEN_BUTTON_SIZE.withWidthPercent(.5f));
			SingleOption inbox = new PlainActorSingleOption(showInbox, Constants.START_SCREEN_BUTTON_SIZE.withWidthPercent(.5f));
			providerList.add(new BasicOptionProvider(
					new GroupedSelectionSingleOption(Constants.START_SCREEN_BUTTON_SIZE, true, Collections.singleton(new BasicOptionProvider(
							join, inbox
					)))
			));
		} else if(joinRoom != null || showInbox != null){ // only one of these is available
			providerList.add(new MultiActorOptionProvider(
					Constants.START_SCREEN_BUTTON_SIZE,
					joinRoom != null ? joinRoom : showInbox
			));
		}

		// this is initialized after each button because it uses them
		this.menuRenderable = new ComponentRenderable(new SelectionMenuRenderComponent(
				renderObject,
                gameInputPlayerIndex,
				gameInput,
				new PlainTable(),
				providerList,
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
		final AccountMultiplayer accountMultiplayer = accountObject.getAccountMultiplayer();
		final boolean isMultiplayerConnected = accountMultiplayer.getConnectionState() != AccountMultiplayer.ConnectionState.DISCONNECTED;
		if(isMultiplayerConnected){
			idleTime = 0;
			if(accountMultiplayer.getConnectionState() == AccountMultiplayer.ConnectionState.CONNECTED) {
				System.out.println("Starting actual multiplayer game!");
				normalGame(accountMultiplayer.getMultiplayer());
			}
		} else {
			if (gameInput.getFireButton().isJustPressed() || gameInput.getEnterButton().isJustPressed()
					|| gameInput.getMainJoystick().getMagnitude() > .5 || Gdx.input.justTouched()
					|| renderParts.getOptionsMenu().isMenuOpen()) {
				idleTime = 0;
			}

			if (gameInput.getStartButton().isJustPressed() || startButton.isPressed()) {
				normalGame(new DisconnectedMultiplayer(gameInputs.size()));
				return;
			}
			if (!renderParts.getOptionsMenu().isMenuOpen() && (gameInput.getBackButton().isJustPressed() || idleTime > DEMO_GAME_INIT_IDLE)) {
				demoGame();
				return;
			}
			if (creditsButton.isPressed()) {
				nextScreen = new CreditsScreen(gameInputs, renderObject, renderParts, accountObject, volumeControl);
				return;
			}
			if (optionsDown && !optionsButton.isPressed()) { // just released options button
				renderParts.getOptionsMenu().setToController(gameInputPlayerIndex, gameInput, gameInputPlayerIndex, gameInput);
			}
			optionsDown = optionsButton.isPressed();

			final AccountManager accountManager = accountObject.getAccountManager();
			if (signInButton != null) {
				final boolean signedIn = accountManager.isSignedIn();
				if (signedIn) {
					signInButton.setText("sign out");
				} else {
					signInButton.setText("sign in");
				}
				if (signInDown && !signInButton.isPressed()) { // just released sign in
					if (signedIn) {
						accountManager.logout();
					} else {
						accountManager.signIn();
					}
				}
				signInDown = signInButton.isPressed();
			}
			if (joinRoom != null) {
				final Show roomShow = accountObject.getAccountMultiplayer().getShowRoomConfig();
				boolean canShow = roomShow.isCurrentlyAbleToShow();
				joinRoom.setVisible(canShow);
				if (canShow) {
					if (joinRoomDown && !joinRoom.isPressed()) {
						roomShow.show();
					}
					joinRoomDown = joinRoom.isPressed();
				}
			}
			if (showInbox != null) {
				final Show inboxShow = accountObject.getAccountMultiplayer().getShowInbox();
				boolean canShow = inboxShow.isCurrentlyAbleToShow();
				showInbox.setVisible(canShow);
				if (canShow) {
					if (showInboxDown && !showInbox.isPressed()) {
						inboxShow.show();
					}
					showInboxDown = showInbox.isPressed();
				}
			}
			if (showAchievements != null) {
				final Show achievementsShow = accountObject.getAchievementHandler().getShowAchievements();
				boolean canShow = achievementsShow.isCurrentlyAbleToShow();
				showAchievements.setVisible(canShow);
				if (canShow) {
					if (showAchievementsDown && !showAchievements.isPressed()) { // just released show achievements
						achievementsShow.show();
					}
					showAchievementsDown = showAchievements.isPressed();
				}
			}
			if (showLeaderboards != null) {
				final Show leaderboardsShow = accountObject.getAchievementHandler().getShowLeaderboards();
				boolean canShow = leaderboardsShow.isCurrentlyAbleToShow();
				showLeaderboards.setVisible(canShow);
				if (canShow) {
					if (showLeaderboardsDown && !showLeaderboards.isPressed()) { // just released show leaderboards
						leaderboardsShow.show();
					}
					showLeaderboardsDown = showLeaderboards.isPressed();
				}
			}
		}

		renderParts.getOverlay().setPauseVisible(false);
		new InputFocuser()
				.add(new InputFocuser(0).addParallel(renderParts.getTouchpadRenderer()).addParallel(uiStage))
				.add(renderParts.getOptionsMenu()) // may or may not get focus
				.giveFocus(uiStage, renderParts.getInputMultiplexer());


		createRenderer().render(delta);
	}
	private void normalGame(Multiplayer multiplayer){
		if(nextScreen == null) {
			nextScreen = new GameScreen(multiplayer, gameInputs, renderObject, renderParts, GameScreen.GameType.NORMAL, accountObject, volumeControl);
		}
	}
	private void demoGame(){
		if(nextScreen == null) {
			nextScreen = new GameScreen(new DisconnectedMultiplayer(1), gameInputs, renderObject, renderParts, GameScreen.GameType.DEMO_AI, accountObject, volumeControl);
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
			"Sign in for achievements and leaderboards!",
			"Blue sharks need 3 hits, pink need 2, and red needs one more!"
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
