package me.retrodaredevil.game.trackshooter.overlay;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import me.retrodaredevil.game.trackshooter.Renderable;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;

public class Overlay implements Renderable, Disposable {

    private final Stage stage;
	private final RenderComponent component = new OverlayRenderer(this);
	private Player player = null;

	public Overlay(Batch batch){
		stage = new Stage(new ScreenViewport(), batch);
	}
	public int getCurrentScore(){
		if(player == null){
			return 0;
		}
		return player.getScoreObject().getScore();
	}
	public int getHighScore(){
		int score = getCurrentScore();
		if(score > 10000){
			return score;
		}
		return 10000;
	}
	public void setPlayer(Player player){
	    this.player = player;
	}
	public Stage getStage(){
		return stage;
	}
	@Override
	public RenderComponent getRenderComponent() {
		return component;
	}

	@Override
	public void disposeRenderComponent() {
        component.dispose();
	}

	@Override
	public void dispose() {
		disposeRenderComponent();
		stage.dispose();
	}
}
