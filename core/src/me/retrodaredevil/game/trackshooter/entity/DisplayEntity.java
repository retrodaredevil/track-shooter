package me.retrodaredevil.game.trackshooter.entity;

import com.badlogic.gdx.math.Vector2;
import me.retrodaredevil.game.trackshooter.render.RenderComponent;

public class DisplayEntity extends TimedEntity {

	public DisplayEntity(long time, Vector2 position) {
		super(time);
		setHitboxSize(0, 0);
		setLocation(position);
		canRespawn = false;
		canLevelEndWithEntityActive = false;
	}

	@Override
	public void setRenderComponent(RenderComponent renderComponent) {
		super.setRenderComponent(renderComponent);
	}
}
