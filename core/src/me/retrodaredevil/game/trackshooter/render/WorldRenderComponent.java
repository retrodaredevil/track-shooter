package me.retrodaredevil.game.trackshooter.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.world.World;

public class WorldRenderComponent implements RenderComponent {

	private World world;
	private boolean renderHitboxes = false;
	private ShapeRenderer renderer = null;

	public WorldRenderComponent(World world){
		this.world = world;
	}

	@Override
	public void render(float delta, Stage stage) {
		RenderComponent trackRender = world.getTrack().getRenderComponent();
		if(trackRender != null){
			trackRender.render(delta, stage);
		}
		if(renderHitboxes && renderer == null){
			renderer = new ShapeRenderer();
		}
		for(Entity entity : world.getEntities()){
			RenderComponent entityRender = entity.getRenderComponent();
			if(entityRender != null){
				entityRender.render(delta, stage);
			}
			if(renderHitboxes){
				Rectangle hitbox = entity.getHitbox();

				Camera camera = stage.getCamera();
				camera.update();
				renderer.setProjectionMatrix(camera.combined);
				renderer.begin(ShapeRenderer.ShapeType.Line);
//				renderer.setColor(this.color);
				renderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
				renderer.end();
			}
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.H) && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)){
			renderHitboxes = !renderHitboxes;
		}
	}

	@Override
	public void dispose() {
		if(renderer != null) {
			renderer.dispose();
		}
	}
}
