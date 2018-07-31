package me.retrodaredevil.game.trackshooter.overlay;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.retrodaredevil.game.trackshooter.Renderable;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;
import me.retrodaredevil.game.trackshooter.util.Constants;

public class Overlay implements Renderable, Disposable {
	// quick and dirty high score implementation // this only updates when an optional method is called so it's even worse
	private static int highScore = 10000; // TODO git rid of this terrible static variable

    private final Stage stage;
	private final RenderComponent component = new OverlayRenderer(this);
	private Player[] players = null;
//	private final List<Player> players = new ArrayList<>(4);

	public Overlay(Batch batch){
		stage = new Stage(new ScreenViewport(), batch);
	}
	public void handleRender(float delta){
		component.render(delta, stage);

		stage.act(delta);
		stage.draw();
	}
	public int getNumberPlayers(){
		return players == null ? 0 : players.length;
	}
	public int getCurrentScore(int playerIndex){
		if(players == null || playerIndex >= players.length){
			return 0;
		}
		Player player = players[playerIndex];
		if(player == null){
			throw new NullPointerException("An item in the players array is null");
		}
		return player.getScoreObject().getScore();
	}
	public int getHighScore(){
		for(int i = 0; i < getNumberPlayers(); i++){
			int score = getCurrentScore(i);
			if(score > highScore){
				highScore = score;
			}
		}
		return highScore;
	}

	/**
	 * Sets the list of players and copies it
	 * @param players The list of players to be copied
	 */
	public void setPlayers(List<Player> players){
		this.players = players.toArray(new Player[players.size()]);
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
