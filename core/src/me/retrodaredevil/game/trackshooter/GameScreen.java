package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;

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

	private final Player player;
	private final Stage stage;
	private final World world;

	private final Overlay overlay;

	private final Batch batch;

	private boolean shouldExit = false;

	public GameScreen(GameInput gameInput, Overlay overlay, Batch batch){
		this.player = new Player();
		this.world = new World(new GameLevelGetter(player), 18, 18);
		this.stage = new Stage(new WorldViewport(world), batch);
		this.overlay = overlay;
		this.batch = batch;

		overlay.setPlayer(player);


		player.setEntityController(new PlayerController(player, gameInput));
		world.addEntity(player);
	}

	@Override
	public void render(float delta) {
		if(delta > 1){
			System.out.println("Delta is high: " + delta);
			delta = Math.min(delta, .1f); //
			float virtualDelta = 1f / 30f; // seems like it's 30 fps now
			for(int i = 0; i < delta / virtualDelta; i++){
				doUpdate(virtualDelta);
			}
		} else {
			doUpdate(delta);
		}
		doRender(delta);

	}
	private void doUpdate(float delta){
		world.update(delta, world);
//		stage.getViewport().apply(true);


		Level level = world.getLevel();
		LevelMode mode = level.getMode();

		Score score = player.getScoreObject();
		if(score.getLives() <= 0){
			if(mode == LevelMode.NORMAL) {
				level.setMode(LevelMode.RESET);
			}
			if(mode == LevelMode.STANDBY){ // all enemies have returned to start
				if(level.getModeTime() > 4000) {
					shouldExit = true;
					System.out.println("Exiting game.");
					Gdx.app.log("final score", "" + score.getScore());
					Gdx.app.log("Shots", "" + score.getNumberShots());
                    Gdx.app.log("Total Shots", "" + score.getTotalNumberShots());
                    Gdx.app.log("Hits", "" + score.getNumberShotsHit());
                    float hitMiss = Math.round(score.getNumberShotsHit() * 1000 / score.getNumberShots()) / 10;
                    Gdx.app.log("hit/miss ratio", hitMiss + "%");
				}
			}
			return;
		}
		if(player.isRemoved() && mode == LevelMode.NORMAL){
			level.setMode(LevelMode.RESET);
		}
		if(mode == LevelMode.STANDBY){
			long time = level.getModeTime();
			if(player.isRemoved()){
				if(time > 750){
					world.addEntity(player);
				}
			} else if(time > 1500){
				level.setMode(LevelMode.NORMAL);
			}
		}

	}
	private void doRender(float delta){
        RenderUtil.clearScreen(Constants.BACKGROUND_COLOR);

		RenderComponent worldRender = world.getRenderComponent();
		if(worldRender != null){
			worldRender.render(delta, stage);
		}

		if(Constants.SHOULD_ACT) {
			stage.act(delta);
		}


		RenderComponent overlayRender = overlay.getRenderComponent();
		if(overlayRender != null){
		    Stage textStage = overlay.getStage();
			overlayRender.render(delta, textStage);

			if(Constants.SHOULD_ACT) {
				textStage.act(delta);
			}
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
		stage    .getViewport().update(width, height,true);
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
