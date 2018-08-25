package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.retrodaredevil.game.input.GameInput;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.entity.player.PlayerController;
import me.retrodaredevil.game.trackshooter.entity.player.Score;
import me.retrodaredevil.game.trackshooter.level.Level;
import me.retrodaredevil.game.trackshooter.level.LevelMode;
import me.retrodaredevil.game.trackshooter.overlay.Overlay;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;
import me.retrodaredevil.game.trackshooter.render.WorldViewport;
import me.retrodaredevil.game.trackshooter.util.Constants;
import me.retrodaredevil.game.trackshooter.util.RenderUtil;
import me.retrodaredevil.game.trackshooter.world.World;

public class GameScreen extends ScreenAdapter {

//	private final GameInput gameInput;
	private final List<Player> players = new ArrayList<>(); // elements may be removed
	private final Stage stage;
	private final World world;

	private final Overlay overlay;

	private final Batch batch;

	private boolean shouldExit = false;

	public GameScreen(List<GameInput> gameInputs, Overlay overlay, Batch batch, Skin skin){
//		this.gameInput = gameInputs.get(0);
		this.world = new World(new GameLevelGetter(players), 18, 18, skin);
		this.stage = new Stage(new WorldViewport(world), batch);
		this.overlay = overlay;
		this.batch = batch;


		for(GameInput gameInput : gameInputs){
			Player player = new Player(gameInput.getRumble(), skin);
			players.add(player);
			player.setEntityController(new PlayerController(player, gameInput));
			world.addEntity(player);
		}

		overlay.setGame(players, world);
	}

	@Override
	public void render(float delta) {
		if(delta > 1 / 30.0f){
			delta = 1 / 30.0f;
		}
		doUpdate(delta);
		doRender(delta);

	}
	private void doUpdate(float delta){
		world.update(delta, world);
//		stage.getViewport().apply(true);


		Level level = world.getLevel();
		LevelMode mode = level.getMode();

		for(Iterator<Player> it = players.listIterator(); it.hasNext(); ){
			Player player = it.next();
			Score score = player.getScoreObject();

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
				long time = level.getModeTime();
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
				if (level.getModeTime() > 4000) {
					shouldExit = true;
					System.out.println("Exiting game.");
					for(Player player : players){
						Score score = player.getScoreObject();
						score.printOut();
					}
				}
			}
		}
	}
	private void doRender(float delta){
		RenderUtil.clearScreen(Constants.BACKGROUND_COLOR);

		RenderComponent worldRender = world.getRenderComponent();
		if(worldRender != null){
			worldRender.render(delta, stage);
		}

		stage.act(delta);

		RenderComponent overlayRender = overlay.getRenderComponent();
		if(overlayRender != null){
			Stage textStage = overlay.getStage();
			overlayRender.render(delta, textStage);

			textStage.act(delta);
			assert batch == stage.getBatch() : "stage's batch isn't our batch!";
			assert batch == textStage.getBatch() : "Overlay's batch isn't our batch!";
			// We use this instead of Stage#draw() because we only have to begin() batch one time
			batch.begin();
			RenderUtil.drawStage(batch, stage);
			RenderUtil.drawStage(batch, textStage);
			batch.end();
		} else {
			System.err.println("overlayRender is null!. If this is intended, please remove this debug statement. Not doing some assertion checks.");
			stage.draw();
		}
	}

	@Override
	public void resize(int width, int height) {
		Gdx.gl.glViewport(0, 0, width, height);
		stage             .getViewport().update(width, height,true);
		overlay.getStage().getViewport().update(width, height,true);
	}

	@Override
	public void dispose() {
		world.disposeRenderComponent();
		stage.dispose();

	}
	public boolean isGameCompletelyOver(){
		return shouldExit;
	}
}
