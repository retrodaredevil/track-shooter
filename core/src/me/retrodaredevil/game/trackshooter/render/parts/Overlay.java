package me.retrodaredevil.game.trackshooter.render.parts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import me.retrodaredevil.game.trackshooter.RenderObject;
import me.retrodaredevil.game.trackshooter.Updateable;
import me.retrodaredevil.game.trackshooter.render.Renderable;
import me.retrodaredevil.game.trackshooter.entity.player.Player;
import me.retrodaredevil.game.trackshooter.item.DisplayedItem;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;
import me.retrodaredevil.game.trackshooter.render.viewports.UIViewport;
import me.retrodaredevil.game.trackshooter.world.World;

public class Overlay implements Renderable, Updateable, Disposable {

	private final Stage stage;
	private final RenderObject renderObject;
	private final RenderComponent component;
	private final Preferences scorePreferences;
	private Player[] players = null;
	private World world = null;

	public Overlay(RenderObject renderObject){
		this.renderObject = renderObject;
		stage = new Stage(new UIViewport(640), renderObject.getBatch());
		component = new OverlayRenderer(this, renderObject);
		scorePreferences = Gdx.app.getPreferences("score");
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
//		for(int i = 0; i < getNumberPlayers(); i++){
//			int score = getCurrentScore(i);
//			if(score > highScore){
//				highScore = score;
//			}
//		}
//		return highScore;
		return scorePreferences.getInteger("high_score", 10000);
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
	public Player.Type getPlayerType(int playerIndex){
		if(players == null || playerIndex >= players.length){
			return Player.Type.NORMAL;
		}
		Player player = players[playerIndex];
		return player.getPlayerType();
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
	 * @param players The list of players to be copied. (If mutated outside of this class, will have no effect on this class)
	 * @param world The world of the game
	 */
	public void setGame(List<Player> players, World world){
		this.players = players.toArray(new Player[players.size()]);
		this.world = world;
	}
	@Override
	public Stage getPreferredStage(){
		return stage;
	}
	@Override
	public RenderComponent getRenderComponent() {
		return component;
	}

	@Override
	public void update(float delta, World world) {
		for(Player player : players){
			int score = player.getScoreObject().getScore();
			if(score > getHighScore()){
				scorePreferences.putInteger("high_score", score);
				scorePreferences.flush();
			}
		}
	}

	@Override
	public void dispose() {
		disposeRenderComponent();
		stage.dispose();
	}

}
