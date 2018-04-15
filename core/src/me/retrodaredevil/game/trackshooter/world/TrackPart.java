package me.retrodaredevil.game.trackshooter.world;

import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.Renderable;

public abstract class TrackPart implements Renderable {

	protected final Vector2 start;
	protected final Vector2 end;

	protected TrackPart(Vector2 start, Vector2 end){
		this.start = start;
		this.end = end;
	}

	public abstract float getDistance();

	/**
	 * When distanceGone is 0, the return value should be
	 *
	 * @param distanceGone must be in range [0, getDistance())
	 * @return The desired coordinate position.
	 */
	public abstract Vector2 getDesiredPosition(float distanceGone);
}
