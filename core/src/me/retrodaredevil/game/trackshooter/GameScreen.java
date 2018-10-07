package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.retrodaredevil.game.input.GameInput;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.entity.player.PlayerController;
import me.retrodaredevil.game.trackshooter.entity.player.Score;
import me.retrodaredevil.game.trackshooter.level.Level;
import me.retrodaredevil.game.trackshooter.level.LevelMode;
import me.retrodaredevil.game.trackshooter.render.RenderObject;
import me.retrodaredevil.game.trackshooter.render.RenderParts;
import me.retrodaredevil.game.trackshooter.render.Renderer;
import me.retrodaredevil.game.trackshooter.render.viewports.WorldViewport;
import me.retrodaredevil.game.trackshooter.world.World;

public class GameScreen implements UsableScreen {

//	private final GameInput gameInput;
	private final List<Player> players = new ArrayList<>(); // elements may be removed // initialized in constructor
	private final List<GameInput> gameInputs;
	private final Stage stage;
	private final World world;

	private final RenderObject renderObject;
	private final RenderParts renderParts;

	private boolean shouldExit = false;
	private boolean paused = false;

	public GameScreen(List<GameInput> gameInputs, RenderObject renderObject, RenderParts renderParts){
		this.gameInputs = gameInputs;
		this.world = new World(new GameLevelGetter(players), 18, 18, renderObject);
		this.stage = new Stage(new WorldViewport(world), renderObject.getBatch());
		this.renderParts = renderParts;
		this.renderObject = renderObject;

		{
			int i = 0;
			for (GameInput gameInput : gameInputs) {
				Player player = new Player(gameInput.getRumble(), i % 2 == 0 ? Player.Type.NORMAL : Player.Type.SNIPER);
				players.add(player);
				player.setEntityController(new PlayerController(player, gameInput));
				world.addEntity(player);
				i++;
			}
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
		for(GameInput input : gameInputs){
			if(input.getPauseButton().isPressed()){
				paused = !paused;
				break;
			}
		}
		renderParts.getOptionsMenu().closeMenu(); // stop displaying options menu
		renderParts.getOverlay().update(delta, world);
		if(paused){
			return;
		}
		world.update(delta, world);


		Level level = world.getLevel();
		LevelMode mode = level.getMode();

		for(Iterator<Player> it = players.listIterator(); it.hasNext(); ){
			Player player = it.next();
			Score score = player.getScoreObject();

			// TODO There's a rare bug where the player dies, lives goes to 0, then gets an extra...
			// life from a previously shot bullet. Because of this, the player is removed before
			// we have a chance to see if the player gets that extra life. We may fix this in the
			// future or we may leave it the way it is because it might create more bugs with multiplayer.
			if(score.getLives() > 0){
				if(player.isRemoved() && mode == LevelMode.NORMAL){
					level.setMode(LevelMode.RESET);
				}
			} else {
				it.remove();
			}
		}
		// now players only has players that will appear on screen in the future

		if(!players.isEmpty()){
			if(mode == LevelMode.STANDBY){
				long time = level.getModeTimeMillis();
				if(time > 750){
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
					setToExit();
				}
			}
		}
	}
	private void doRender(float delta){

		Renderer renderer = new Renderer(renderObject.getBatch(), stage);
		renderer.addRenderable(renderParts.getBackground());
		renderer.addRenderable(world);
		renderer.addMainStage(); // world should have added this anyway
		renderer.addRenderable(renderParts.getTouchpadRenderer());
		renderer.addRenderable(renderParts.getOptionsMenu());
		renderer.addRenderable(renderParts.getOverlay());

		renderer.render(delta);

		InputFocuser inputFocuser = new InputFocuser();
		{
			InputFocuser inGameFocuser = new InputFocuser(0);

			inGameFocuser.addParallel(renderParts.getTouchpadRenderer());

			inputFocuser.add(inGameFocuser);
		}
		if(paused){

		}
		inputFocuser.giveFocus(stage, renderParts.getInputMultiplexer());
	}
	public void setToExit(){
		shouldExit = true;
		System.out.println("Exiting game.");
		for(Player player : players){
			Score score = player.getScoreObject();
			score.printOut();
		}
	}

	@Override
	public void pause() {
		// called when exiting app when still open
		paused = true;
	}

	@Override
	public void resume() {
		// Called when reentering opened app
	}

	@Override
	public void hide() { // called usually before dispose
	}

	@Override
	public void show() {
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height,true);
		renderParts.resize(width, height);
	}

	@Override
	public void dispose() {
		world.disposeRenderComponent();
		stage.dispose();
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
		return new StartScreen(gameInputs, renderObject, renderParts);
	}
}
