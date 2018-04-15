package me.retrodaredevil.game.trackshooter.world;

import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.Renderable;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;
import me.retrodaredevil.game.trackshooter.render.TrackRenderComponent;

import java.util.Collection;

public class Track implements Renderable {

	private final Collection<? extends TrackPart> parts;
	private float totalDistance;

	protected RenderComponent renderComponent;

	public Track(Collection<? extends TrackPart> parts){
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
	public Collection<? extends TrackPart> getParts(){
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
		distanceGone %= totalDistance;
		distanceGone = distanceGone < 0 ? distanceGone + totalDistance : distanceGone;

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

}
