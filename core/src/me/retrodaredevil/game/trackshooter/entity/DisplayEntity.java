package me.retrodaredevil.game.trackshooter.entity;

import com.badlogic.gdx.math.Vector2;

import me.retrodaredevil.game.trackshooter.level.LevelEndState;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;
import me.retrodaredevil.game.trackshooter.world.World;

public class DisplayEntity extends TimedEntity {

	public DisplayEntity(World world, float time, Vector2 position) {
		super(world, time);
//		setHitboxSize(0); doesn't matter since it can't collide
		setLocation(position);
		canRespawn = false;
//		canLevelEndWithEntityActive = false;
		levelEndStateWhenActive = LevelEndState.CAN_END_SOON;
	}

	@Override
	public void setRenderComponent(RenderComponent renderComponent) {
		super.setRenderComponent(renderComponent);
	}
}
