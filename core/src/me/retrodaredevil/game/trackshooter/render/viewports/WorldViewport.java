package me.retrodaredevil.game.trackshooter.render.viewports;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import me.retrodaredevil.game.trackshooter.world.World;

import static java.util.Objects.requireNonNull;

public class WorldViewport extends ExtendViewport {

	public WorldViewport(World world, OrthographicCamera camera){
		super(world.getBounds().getWidth(), world.getBounds().getHeight(), requireNonNull(camera));
	}
	public WorldViewport(World world){
		this(world, new OrthographicCamera());
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
