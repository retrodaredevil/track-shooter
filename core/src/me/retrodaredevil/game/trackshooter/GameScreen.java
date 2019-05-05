package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.utils.viewport.Viewport;
import me.retrodaredevil.game.trackshooter.achievement.AchievementHandler;
import me.retrodaredevil.game.trackshooter.achievement.implementations.DefaultGameEvent;
import me.retrodaredevil.game.trackshooter.entity.player.PlayerAIController;
import me.retrodaredevil.game.trackshooter.input.GameInput;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.entity.player.PlayerController;
import me.retrodaredevil.game.trackshooter.entity.player.Score;
import me.retrodaredevil.game.trackshooter.level.Level;
import me.retrodaredevil.game.trackshooter.level.LevelMode;
import me.retrodaredevil.game.trackshooter.render.RenderObject;
import me.retrodaredevil.game.trackshooter.render.RenderParts;
import me.retrodaredevil.game.trackshooter.render.Renderer;
import me.retrodaredevil.game.trackshooter.render.parts.PauseMenu;
import me.retrodaredevil.game.trackshooter.render.viewports.WorldViewport;
import me.retrodaredevil.game.trackshooter.sound.VolumeControl;
import me.retrodaredevil.game.trackshooter.world.World;

public class GameScreen implements UsableScreen {

//	private final GameInput gameInput;
	private final List<GameInput> gameInputs;
	private final GameType gameType;
	private final List<Player> players = new ArrayList<>(); // elements may be removed // initialized in constructor
	private final World world;

	private final RenderObject renderObject;
	private final RenderParts renderParts;
	/** The pause menu or null */
	private final PauseMenu pauseMenu;
	private final AchievementHandler achievementHandler;
	private final VolumeControl volumeControl;

	private final Stage stage;

	private boolean shouldExit = false;

	public GameScreen(List<GameInput> gameInputs, RenderObject renderObject, RenderParts renderParts, GameType gameType, AchievementHandler achievementHandler, VolumeControl volumeControl){
		this.gameInputs = gameInputs;
		this.gameType = gameType;
		this.renderObject = renderObject;
		this.renderParts = renderParts;
		this.achievementHandler = achievementHandler;
		this.volumeControl = volumeControl;

		final AchievementHandler passedHandler = gameType == GameType.NORMAL ? achievementHandler : AchievementHandler.Defaults.UNSUPPORTED_HANDLER;
		world = new World(new GameLevelGetter(players, passedHandler), 18, 18, renderObject, new StageCoordinatesConverter());
		stage = new Stage(new WorldViewport(world), renderObject.getBatch());

		if(gameType == GameType.NORMAL){
			int i = 0;
			for (GameInput gameInput : gameInputs) {
				Player player = new Player(world, gameInput::getRumble, passedHandler, i % 2 == 0 ? Player.Type.NORMAL : Player.Type.SNIPER, volumeControl);
				players.add(player);
				player.setEntityController(new PlayerController(world, player, gameInput));
				world.addEntity(player);
				i++;
			}
			pauseMenu = new PauseMenu(gameInputs, renderObject, renderParts, () -> setToExit(false));
		} else { // assume DEMO_AI
			Player player = new Player(world, () -> null, passedHandler, Player.Type.NORMAL, VolumeControl.Defaults.MUTED);
			players.add(player);
			player.setEntityController(new PlayerAIController(world, player));
			world.addEntity(player);
			pauseMenu = null;
		}

		renderParts.getOverlay().setGame(players, world);
	}

	@Override
	public void render(float delta) {
		delta = Math.min(delta, 1 / 30f);
		doUpdate(delta);
		doRender(delta);

	}
	private void doUpdate(float delta){
		renderParts.getOverlay().update(delta);
		if(pauseMenu != null) {
			pauseMenu.update(delta);
		}
		if(gameType == GameType.DEMO_AI){
			for(GameInput input : gameInputs){
				if(input.getBackButton().isPressed() || input.getFireButton().isPressed() || input.getStartButton().isPressed()){
					setToExit(false);
					return;
				}
			}
			if(Gdx.input.justTouched()){
				setToExit(false);
				return;
			}
		}
		if(isPaused()){
			return;
		}
		world.update(delta);


		Level level = world.getLevel();
		LevelMode mode = level.getMode();

		for(Iterator<Player> it = players.listIterator(); it.hasNext(); ){
			Player player = it.next();
			Score score = player.getScoreObject();

			/* NOTTODO There's a rare bug where the player dies, lives goes to 0, then gets an extra...
			life from a previously shot bullet. Because of this, the player is removed before
			we have a chance to see if the player gets that extra life. We may fix this in the
			future or we may leave it the way it is because it might create more bugs with multiplayer. */
			// Actually, as of 2019.4.27, this should be fixed. I fixed this by ignoring new score changes in PlayerScore
			// after onGameEnd() is called. This was not my initial plan to fix it this way, but I think it's the simplest and most elegant

			if(score.getLives() > 0){
				if(player.isRemoved() && mode == LevelMode.NORMAL){
					level.setMode(LevelMode.RESET);
				}
			} else {
				it.remove();
				score.onGameEnd();
				score.printOut();
			}
		}
		// now players only has players that will appear on screen in the future

		if(!players.isEmpty()){
			if(mode == LevelMode.STANDBY){
				long time = level.getModeTimeMillis();
				if(time > 600){
					for(Player player : players){
						if(player.isRemoved()){
							world.addEntity(player);
						}
					}
				}
				if(time > 1500){
					level.setMode(LevelMode.NORMAL);
				}
			}
		} else {
			if(mode == LevelMode.NORMAL){
				level.setMode(LevelMode.RESET);
			} else if (mode == LevelMode.STANDBY) { // all enemies have returned to start
				if (level.getModeTimeMillis() > 4000) {
					setToExit(true);
				}
			}
		}
	}
	private Renderer createRenderer(){
		return new Renderer(renderObject.getBatch(), stage)
				.addRenderable(renderParts.getBackground())
				.addRenderable(world)
				.addMainStage() // world should have added this anyway
				.addRenderable(renderParts.getTouchpadRenderer())
				.addRenderable(renderParts.getArrowRenderer())
				.addRenderable(renderParts.getOptionsMenu())
				.addRenderable(pauseMenu) // may be null
				.addRenderable(renderParts.getOverlay());
	}
	private void doRender(float delta){
		createRenderer().render(delta);

		new InputFocuser()
				.addParallel(renderParts.getTouchpadRenderer())
				.addParallel(renderParts.getOverlay())
				.addParallel(pauseMenu)
				.addParallel(renderParts.getOptionsMenu())
				.giveFocus(stage, renderParts.getInputMultiplexer());
	}
	private void setToExit(boolean wasFullGame){
		if(shouldExit){
			System.out.println("Was already set to exit!");
			return;
		}
		shouldExit = true;
		System.out.println("Exiting game.");
		for(Player player : players){
			Score score = player.getScoreObject();
			score.printOut();
			score.onGameEnd();
		}
		if(wasFullGame && gameType != GameType.DEMO_AI){
			achievementHandler.incrementIfSupported(DefaultGameEvent.GAMES_COMPLETED, 1);
		}
	}

	public boolean isPaused(){
		return pauseMenu != null && pauseMenu.isMenuOpen();
	}

	/** @deprecated should not be used to pause the game*/
	@Deprecated
	@Override
	public void pause() {
		// called when exiting app when the app is still open
		if(pauseMenu != null && !pauseMenu.isMenuOpen()){
			pauseMenu.setControllerAndOpen(0, gameInputs.get(0));
		}
		if(gameType == GameType.DEMO_AI){
			setToExit(false);
		}
	}

	/** @deprecated should not be used to resume the game */
	@Deprecated
	@Override
	public void resume() {
		// Called when reentering opened app
		if(gameType == GameType.DEMO_AI){
			setToExit(false);
		}
	}

	@Deprecated
	@Override
	public void hide() { // called usually before dispose
	}

	@Deprecated
	@Override
	public void show() {
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height,true);
		renderParts.resize(width, height);
		world.resize(width, height);
		if(pauseMenu != null) {
			pauseMenu.resize(width, height);
		}
	}

	@Override
	public void dispose() {
		world.disposeRenderComponent();
		stage.dispose();
		if(pauseMenu != null) {
			pauseMenu.disposeRenderComponent();
		}
	}

	@Override
	public boolean isScreenDone() {
		return shouldExit;
	}

	@Override
	public UsableScreen createNextScreen() {
		if(!shouldExit){
			throw new IllegalStateException("Cannot create a StartScreen if we aren't done!");
		}
		return new StartScreen(gameInputs, renderObject, renderParts, achievementHandler, volumeControl);
	}

	public enum GameType {
		/** Represents a normal game controlled by player(s)*/
		NORMAL,
		/** Represents a game controlled by an ai*/
		DEMO_AI
	}
	private class StageCoordinatesConverter implements World.WorldCoordinatesGetter {
		@Override
		public void getWorldCoordinates(int screenX, int screenY, Vector2 result) {
			Viewport viewport = stage.getViewport();
			viewport.unproject(result.set(screenX, screenY));
		}
	}
}
