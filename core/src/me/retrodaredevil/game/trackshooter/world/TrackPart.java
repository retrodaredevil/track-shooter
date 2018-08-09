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

	/**
	 *
	 * @param angleDegrees The desired direction to travel
	 * @param distance The distance/location of whatever is on this track part
	 * @return A value between -1 and 1 where a value > 0 means the direction corresponds to an increasing track distance
	 */
	public abstract float getMovePercent(float angleDegrees, float distance);

	/**
	 *
	 * @param distance The distance/location on this track part
	 * @return The direction in degrees that is forward on the track
	 */
	public abstract float getForwardDirection(float distance);

	public abstract float getDistance();

	/**
	 * @param distanceGone must be in range [0, getDistance())
	 * @return The desired coordinate position. This variable is allowed to be modified.
	 */
	public abstract Vector2 getDesiredPosition(float distanceGone);
}
