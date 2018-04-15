package me.retrodaredevil.game.trackshooter.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.render.LineRenderComponent;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;

public class LineTrackPart extends TrackPart {
	private static final ShapeRenderer renderer = new ShapeRenderer();
	private final float distance;
	protected RenderComponent renderComponent;

	public LineTrackPart(Vector2 start, Vector2 end){
		super(start, end);
		distance = start.dst(end);
		renderComponent = new LineRenderComponent(start, end, Color.GRAY, 3, renderer);
	}
	@Override
	public float getDistance() {
		return distance;
	}

	@Override
	public Vector2 getDesiredPosition(float distanceGone) {
		return start.lerp(end, distanceGone / distance);
	}

	@Override
	public RenderComponent getRenderComponent() {
		return renderComponent;
	}
}
