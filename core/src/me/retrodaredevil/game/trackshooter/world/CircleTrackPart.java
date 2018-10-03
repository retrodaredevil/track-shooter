package me.retrodaredevil.game.trackshooter.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.render.components.CircleRenderComponent;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;
import me.retrodaredevil.game.trackshooter.util.MathUtil;

public class CircleTrackPart extends TrackPart {
	private final float radius;
	private final Vector2 center = new Vector2();
	private final float zeroAngle;
	private final RenderComponent renderComponent;

	/**
	 *
	 * @param radius The radius of the circle
	 * @param center The center point
	 * @param zeroAngle The angle where the distance on the track is 0 (in degrees)
	 * @param color The color of the circle
	 */
	public CircleTrackPart(float radius, Vector2 center, float zeroAngle, Color color) {
		super(new Vector2(radius, 0), new Vector2(radius, 0));
		this.radius = radius;
		this.center.set(center);
		this.zeroAngle = zeroAngle;
		this.renderComponent = new CircleRenderComponent(radius, 3, center, color, 100);
	}

	@Override
	public float getMovePercent(float angleDegrees, float distance) {
		return MathUtil.getAngleAlikeRatio(getForwardDirection(distance), angleDegrees);
	}

	@Override
	public float getForwardDirection(float distance) {
		return distance * 360.0f / getDistance() + 90 + zeroAngle;
	}

	@Override
	public float getDistance() {
		return radius * MathUtils.PI2;
	}

	@Override
	public Vector2 getDesiredPosition(float distanceGone) {
		final float radians = distanceGone * MathUtils.PI2 / getDistance() + zeroAngle * MathUtils.degreesToRadians;

		return new Vector2(MathUtils.cos(radians), MathUtils.sin(radians)).scl(radius).add(center);
	}

	@Override
	public RenderComponent getRenderComponent() {
		return renderComponent;
	}

}
