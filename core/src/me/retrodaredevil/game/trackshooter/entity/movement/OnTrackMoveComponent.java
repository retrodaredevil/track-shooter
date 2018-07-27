package me.retrodaredevil.game.trackshooter.entity.movement;

public interface OnTrackMoveComponent extends MoveComponent {
	float getDistanceOnTrack();
	void setDistanceOnTrack(float distance);
}
