package me.retrodaredevil.game.trackshooter;

import me.retrodaredevil.game.trackshooter.world.World;

public class OnTrackMoveComponent extends MoveComponent {
	private float distance = 0; // total distance

	private float velocity = 0; // per second

	private Entity entity;

	public OnTrackMoveComponent(Entity entity){
		this.entity = entity;
	}

	@Override
	public void update(float delta, World world) {
		this.distance += delta * this.velocity;

		entity.setLocation(world.getTrack().getDesiredLocation(distance));
	}
	public void setDistance(float distance, boolean zeroVelocity){
		if(zeroVelocity){
			velocity = 0;
		}
		this.distance = distance;
	}
	public float getDistance(){
		return distance;
	}
	public void setVelocity(float velocity){
		this.velocity = velocity;
	}
	public float getVelocity(){
		return velocity;
	}
}
