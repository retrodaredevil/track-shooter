package me.retrodaredevil.game.trackshooter.entity.movement;

import me.retrodaredevil.game.trackshooter.world.World;

public class TimedMoveComponent implements ChainMoveComponent {

	private MoveComponent nextComponent = null;
	private Long startTime;
	private long time;

	public TimedMoveComponent(long time){
		this.time = time;
	}

	@Override
	public MoveComponent getNextComponent() {
		if(startTime == null || startTime + time > System.currentTimeMillis()){
			return this;
		}
		return nextComponent != null ? nextComponent : this;
	}

	@Override
	public <T extends MoveComponent> T setNextComponent(T nextComponent) {
		this.nextComponent = nextComponent;
		return nextComponent;
	}

	@Override
	public void update(float delta, World world) {
		if(startTime == null){
			startTime = System.currentTimeMillis();
		}
	}
}
