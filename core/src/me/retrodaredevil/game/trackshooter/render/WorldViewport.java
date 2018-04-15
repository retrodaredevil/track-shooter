package me.retrodaredevil.game.trackshooter.render;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import me.retrodaredevil.game.trackshooter.world.World;

public class WorldViewport extends FitViewport {

	private World world;


	public WorldViewport(World world, OrthographicCamera camera){
		super(world.getBounds().getWidth(), world.getBounds().getHeight(), camera);
		this.world = world;
	}
	public WorldViewport(World world){
		this(world, new OrthographicCamera());
	}

	@Override
	public void update(int screenWidth, int screenHeight, boolean centerCamera) {
		super.update(screenWidth, screenHeight, centerCamera);
//		setScreenSize(screenWidth, screenHeight);
//		float ratio = screenWidth / screenHeight; //
//		setWorldSize(world.getBounds().getWidth(), world.getBounds().getHeight());
//
		apply(centerCamera);
	}

	@Override
	public void apply(boolean centerCamera) {
		if(centerCamera) {
			Camera camera = getCamera();
			camera.position.set(0, 0, 0);
		}
		super.apply(false);
	}
}
