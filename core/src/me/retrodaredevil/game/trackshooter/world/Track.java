package me.retrodaredevil.game.trackshooter.world;

import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.Renderable;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;
import me.retrodaredevil.game.trackshooter.render.TrackRenderComponent;
import me.retrodaredevil.game.trackshooter.util.MathUtil;

import java.util.List;

public class Track implements Renderable {

	private final List<? extends TrackPart> parts;
	private float totalDistance;

	protected RenderComponent renderComponent;

	public Track(List<? extends TrackPart> parts){
		this.parts = parts;
		this.totalDistance = calculateTotalDistance();

		this.renderComponent = new TrackRenderComponent(this);
	}
	protected float calculateTotalDistance(){
		float r = 0;
		for(TrackPart part : parts){
			r += part.getDistance();
		}
		return r;
	}
	public List<? extends TrackPart> getParts(){
		return parts;
	}

	@Override
	public RenderComponent getRenderComponent() {
		return renderComponent;
	}

	public float getTotalDistance(){
		return totalDistance;
	}
	public Vector2 getDesiredLocation(float distanceGone){
		distanceGone = MathUtil.mod(distanceGone, totalDistance);

		float current = 0;

		TrackPart currentPart = null;
		for(TrackPart part : parts){
			float partDistance = part.getDistance();
			float newCurrent = current + partDistance;
			if(distanceGone < newCurrent){
				currentPart = part;
				break;
			}
			current = newCurrent;
		}
		assert currentPart != null : "currentPart is null. Something must be wrong with the distances";
		return currentPart.getDesiredPosition(distanceGone - current);
	}
	public float getMovePercent(float angleDegrees, float distanceGone){
		distanceGone = MathUtil.mod(distanceGone, totalDistance);

		float current = 0;
		TrackPart first = null; // the first part in the last
		TrackPart before = null; // the part before current part
		TrackPart after = null; // the part after current part
		TrackPart currentPart = null; // the part distanceGone corresponds to
		for (TrackPart part : parts) {
			if(currentPart != null){
				after = part;
				break;
			}
			if(current == 0){
				first = part;
			}
			float partDistance = part.getDistance();
			float newCurrent = current + partDistance;
			if (distanceGone < newCurrent) {
				currentPart = part;
				continue; // go to first if
			}
			current = newCurrent;
			before = part;
		}
		assert currentPart != null : "currentPart is null. Something must be wrong with the distances";
		if(after == null){
			after = first;
		}
		if(before == null){
//			assert currentPart == first;
			for(TrackPart part : parts){
				before = part;
			}
			assert before != null;
		}
		float currentPartDistance = distanceGone - current;
		float percent = currentPartDistance / currentPart.getDistance(); // if NPE, asserts not turned on
		float r = currentPart.getMovePercent(angleDegrees, currentPartDistance);
		if(percent < .5f){
			// before
			if(before != currentPart) {
				float r2 = before.getMovePercent(angleDegrees, before.getDistance());
				if (Math.abs(r2) > Math.abs(r)) {
					return r2;
				}
			}
		} else {
			// after
			if(after != currentPart) {
				float r2 = after.getMovePercent(angleDegrees, 0);
				if (Math.abs(r2) > Math.abs(r)) {
					return r2;
				}
			}
		}
		return r;
	}

	public float getForwardDirection(float distanceGone){
		distanceGone = MathUtil.mod(distanceGone, totalDistance);
		float current = 0;
		for(TrackPart part : parts){
			float partDistance = part.getDistance();
			float newCurrent = current + partDistance;
			if (distanceGone < newCurrent) {
				float currentPartDistance = distanceGone - current;
				return part.getForwardDirection(currentPartDistance);
			}
			current = newCurrent;
		}
		throw new AssertionError("You had the correct arguments. But the code didn't return!. distanceGone: " + distanceGone + " total: " + totalDistance);
	}
}
