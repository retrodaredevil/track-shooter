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
import me.retrodaredevil.game.trackshooter.render.Renderer;
import me.retrodaredevil.game.trackshooter.render.viewports.WorldViewport;
import me.retrodaredevil.game.trackshooter.world.World;

public class GameScreen extends ScreenAdapter {

//	private final GameInput gameInput;
	private final List<Player> players = new ArrayList<>(); // elements may be removed
	private final Stage stage;
	private final World world;

	private final RenderObject renderObject;
	private final RenderParts renderParts;

	private boolean shouldExit = false;

	public GameScreen(List<GameInput> gameInputs, RenderObject renderObject, RenderParts renderParts){
//		this.gameInput = gameInputs.get(0);
		this.world = new World(new GameLevelGetter(players), 18, 18, renderObject);
		this.stage = new Stage(new WorldViewport(world), renderObject.getBatch());
		this.renderParts = renderParts;
		this.renderObject = renderObject;


		int i = -1;
		for(GameInput gameInput : gameInputs){
			i++;
			Player player = new Player(gameInput.getRumble(), i % 2 == 0 ? Player.Type.NORMAL : Player.Type.SNIPER);
			players.add(player);
			player.setEntityController(new PlayerController(player, gameInput));
			world.addEntity(player);
		}

		renderParts.getOverlay().setGame(players, world);
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
		renderParts.getOptionsMenu().setToController(null); // stop displaying options menu
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

		Renderer renderer = new Renderer(renderObject.getBatch(), stage);
		renderer.addRenderable(renderParts.getBackground());
		renderer.addRenderable(world);
		renderer.addMainStage(); // world should have added this anyway
		renderer.addRenderable(renderParts.getOptionsMenu());
		renderer.addRenderable(renderParts.getOverlay());

		renderer.render(delta);

	}

	@Override
	public void resize(int width, int height) {
		Gdx.gl.glViewport(0, 0, width, height);
		stage.getViewport().update(width, height,true);
		renderParts.resize(width, height);
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
