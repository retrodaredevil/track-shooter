package me.retrodaredevil.game.trackshooter.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.render.CircleRenderComponent;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;

public class CircleTrackPart extends TrackPart {
	private float radius;
	private RenderComponent renderComponent;

	public CircleTrackPart(float radius, Color color) {
		super(new Vector2(radius, 0), new Vector2(radius, 0));
		this.radius = radius;
		this.renderComponent = new CircleRenderComponent(radius, Vector2.Zero, color);
	}

	@Override
	public float getDistance() {
		return radius * MathUtils.PI2;
	}

	@Override
	public Vector2 getDesiredPosition(float distanceGone) {
		float radians = distanceGone / getDistance();
		radians *= MathUtils.PI2;

		return new Vector2(MathUtils.cos(radians), MathUtils.sin(radians)).scl(radius);
	}

	@Override
	public RenderComponent getRenderComponent() {
		return renderComponent;
	}
}
