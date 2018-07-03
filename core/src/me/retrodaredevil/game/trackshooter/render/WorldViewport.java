package me.retrodaredevil.game.trackshooter.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import me.retrodaredevil.game.trackshooter.entity.Entity;
import me.retrodaredevil.game.trackshooter.world.World;

public class WorldViewport extends FitViewport {
//	private static final Vector2 temp = new Vector2();

	private World world;
	private Entity follow;


	public WorldViewport(World world, OrthographicCamera camera, Entity follow){
		super(world.getBounds().getWidth(), world.getBounds().getHeight(), camera);
		this.world = world;
		this.follow = follow;
		Gdx.app.debug("up", camera.up.toString());
		Gdx.app.debug("direction", camera.direction.toString());
	}
	public WorldViewport(World world, Entity follow){
		this(world, new OrthographicCamera(), follow);
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
			OrthographicCamera camera = (OrthographicCamera) getCamera();
			if(follow != null){
				final float forwardAmount = 7;
				float angle = follow.getRotation();
				camera.position.set(follow.getLocation().add(MathUtils.cosDeg(angle) * forwardAmount, MathUtils.sinDeg(angle) * forwardAmount), camera.position.z);
				camera.up.set(0, 1, 0);
				camera.direction.set(0, 0, -1);
				camera.rotate(-angle + 90);
			} else {
				camera.position.set(0, 0, 0);
			}
		}
		super.apply(false);
	}
}
