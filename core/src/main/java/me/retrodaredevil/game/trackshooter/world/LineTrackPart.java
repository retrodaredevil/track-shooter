package me.retrodaredevil.game.trackshooter.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.render.components.LineRenderComponent;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;
import me.retrodaredevil.game.trackshooter.util.MathUtil;

import java.util.ArrayList;
import java.util.List;

public class LineTrackPart extends TrackPart {
	private static final ShapeRenderer renderer = new ShapeRenderer();
	private final float distance;
	private final float angle;
	protected RenderComponent renderComponent;

	public LineTrackPart(Vector2 start, Vector2 end, Color color){
		super(start, end);
		distance = start.dst(end);
		angle = MathUtil.angle(start, end);
		renderComponent = new LineRenderComponent(start, end, color, 3, renderer);
	}

	@Override
	public float getMovePercent(float angleDegrees, float distance) {
		return MathUtil.getAngleAlikeRatio(angle, angleDegrees);
	}

	@Override
	public float getForwardDirection(float distance) {
		return this.angle;
	}

	@Override
	public float getDistance() {
		return distance;
	}

	@Override
	public Vector2 getDesiredPosition(float distanceGone) {
		return start.cpy().lerp(end, distanceGone / distance);
	}

	@Override
	public RenderComponent getRenderComponent() {
		return renderComponent;
	}


	public static class LineTrackPartBuilder{
		private final Vector2 first;
		private Vector2 previous;
		private List<LineTrackPart> parts = new ArrayList<>();
		private Color color;
		public LineTrackPartBuilder(Color color, Vector2 start){
			this.color = color;
			this.first = start;
			this.previous = start;
		}
		public LineTrackPartBuilder connect(Vector2 point){
			parts.add(new LineTrackPart(previous, point, color));
			previous = point;
			return this;
		}
		public LineTrackPartBuilder connect(float x, float y){
			return this.connect(new Vector2(x, y));
		}
		public List<LineTrackPart> build(boolean connectToFirst){
			if(connectToFirst && !first.equals(previous)){
				parts.add(new LineTrackPart(previous, first, color));
			}
			return parts;
		}
	}
}
