package me.retrodaredevil.game.trackshooter.render.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.world.World;

public class WorldRenderComponent implements RenderComponent {

	private final World world;
	private boolean renderHitboxes = false;
	private ShapeRenderer renderer = null; // initialized when hitbox debugging turns on

	public WorldRenderComponent(World world){
		this.world = world;
	}

	@Override
	public void render(float delta, Stage stage) {
		world.getTrack().autoRender(delta, stage, false);
		if(Gdx.input.isKeyJustPressed(Input.Keys.H) && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)){ // hell yeah hard coding
			renderHitboxes = !renderHitboxes;
		}
		if(renderHitboxes){
			if(renderer == null){
				renderer = new ShapeRenderer();
			}
			Camera camera = stage.getCamera();
			camera.update();
			renderer.setProjectionMatrix(camera.combined);
			renderer.begin(ShapeRenderer.ShapeType.Line);
		}
		for(Entity entity : world.getEntities()){
			entity.autoRender(delta, stage, false);
			if(renderHitboxes){
				Rectangle hitbox = entity.getHitbox();
				renderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
			}
		}
		if(renderHitboxes){
			renderer.end();
		}
	}

	@Override
	public void dispose() {
		if(renderer != null) {
			renderer.dispose();
		}
	}
}
