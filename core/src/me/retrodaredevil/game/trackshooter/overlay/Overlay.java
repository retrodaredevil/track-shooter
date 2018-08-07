package me.retrodaredevil.game.trackshooter.overlay;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import me.retrodaredevil.game.trackshooter.Renderable;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.item.DisplayedItem;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;
import me.retrodaredevil.game.trackshooter.render.UIViewport;
import me.retrodaredevil.game.trackshooter.util.Constants;
import me.retrodaredevil.game.trackshooter.world.World;

public class Overlay implements Renderable, Disposable {
	// quick and dirty high score implementation // this only updates when an optional method is called so it's even worse
	private static int highScore = 10000; // TODO git rid of this terrible static variable

	private final Stage stage;
	private final RenderComponent component = new OverlayRenderer(this);
	private Player[] players = null;
	private World world = null;
//	private final List<Player> players = new ArrayList<>(4);

	public Overlay(Batch batch){
		stage = new Stage(new UIViewport(640), batch);
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
	public int getShipsToDraw(int playerIndex){
		if(players == null || playerIndex >= players.length){
			return 0;
		}
		Player player = players[playerIndex];
		int lives = player.getScoreObject().getLives();
		// we have the player.shouldRemove() call so the lives doesn't flicker in a single frame when the life is lost.
		if(player.isRemoved() || player.shouldRemove(world)){ // if the player is removed or is about to be
			return lives;
		}
		return lives - 1;
	}
	/**
	 *
	 * @param playerIndex The index of the player
	 * @return A list that should not be modified representing the images of the items the player of playerIndex has
	 */
	public Collection<Image> getItemImages(int playerIndex) {
		if(players == null || playerIndex >= players.length){
			return Collections.emptyList();
		}
		Player player = players[playerIndex];
		Collection<DisplayedItem> items = player.getItems(DisplayedItem.class);
		if(items == null){
			return Collections.emptyList();
		}

		List<Image> r = new ArrayList<>();
		for(DisplayedItem item : items){
			Image image = item.getImage();
			r.add(image);
		}
		return r;
	}

	/**
	 * Sets the list of players and copies it
	 * @param players The list of players to be copied
	 * @param world The world of the game
	 */
	public void setGame(List<Player> players, World world){
		this.players = players.toArray(new Player[players.size()]);
		this.world = world;
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
