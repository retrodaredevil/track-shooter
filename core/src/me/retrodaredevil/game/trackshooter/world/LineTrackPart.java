package me.retrodaredevil.game.trackshooter.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.render.LineRenderComponent;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;

import java.util.ArrayList;
import java.util.Collection;

public class LineTrackPart extends TrackPart {
	private static final ShapeRenderer renderer = new ShapeRenderer();
	private final float distance;
	protected RenderComponent renderComponent;

	public LineTrackPart(Vector2 start, Vector2 end, Color color){
		super(start, end);
		distance = start.dst(end);
		renderComponent = new LineRenderComponent(start, end, color, 3, renderer);
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
		private Collection<LineTrackPart> parts = new ArrayList<LineTrackPart>();
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
		public Collection<LineTrackPart> build(boolean connectToFirst){
			if(connectToFirst){
				parts.add(new LineTrackPart(previous, first, color));
			}
			return parts;
		}
	}
}
