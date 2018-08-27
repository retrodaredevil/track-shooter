package me.retrodaredevil.game.trackshooter.render.viewports;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.Objects;

import me.retrodaredevil.game.trackshooter.world.World;

public class WorldViewport extends ExtendViewport {
//	private static final Vector2 temp = new Vector2();

	public WorldViewport(World world, OrthographicCamera camera){
		super(world.getBounds().getWidth(), world.getBounds().getHeight(), Objects.requireNonNull(camera));
	}
	public WorldViewport(World world){
		this(world, new OrthographicCamera());
	}

	@Override
	public void update(int screenWidth, int screenHeight, boolean centerCamera) {
		super.update(screenWidth, screenHeight, centerCamera);
	}

	@Override
	public void apply(boolean centerCamera) {
		if(centerCamera) {
			OrthographicCamera camera = (OrthographicCamera) getCamera();
			camera.position.set(0, 0, 0);
		}
		super.apply(false);
	}
}
