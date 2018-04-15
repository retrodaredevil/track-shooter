package me.retrodaredevil.game.trackshooter.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import me.retrodaredevil.game.trackshooter.OnTrackMoveComponent;
import me.retrodaredevil.game.trackshooter.SimpleEntity;
import me.retrodaredevil.game.trackshooter.render.ImageRenderComponent;

public class Player extends SimpleEntity {

	public Player(){
		setMoveComponent(new OnTrackMoveComponent(this));
		setRenderComponent(new ImageRenderComponent(new Image(new Texture("player.png")), this, 1.2f, 1.2f));
	}
}
