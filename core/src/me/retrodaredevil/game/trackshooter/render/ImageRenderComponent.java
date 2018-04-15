package me.retrodaredevil.game.trackshooter.render;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import me.retrodaredevil.game.trackshooter.Entity;

public class ImageRenderComponent implements RenderComponent{

	private Image image;
	private Entity entity;

	public ImageRenderComponent(Image image, Entity entity){
		this.image = image;
		this.entity = entity;
	}
	@Override
	public void render(float delta, Stage stage) {
		if(image.getStage() != stage){
			stage.addActor(image);
		}
		Vector2 location = entity.getLocation();
		image.setPosition(location.x, location.y); // TODO make centered
	}

	@Override
	public void dispose() {
	}
}
