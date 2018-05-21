package me.retrodaredevil.game.input;

import com.badlogic.gdx.Gdx;
import me.retrodaredevil.input.ControlConfig;
import me.retrodaredevil.input.ControllerManager;
import me.retrodaredevil.input.MouseJoystick;

public class GdxMouseJoystick extends MouseJoystick {
	private Double lastX = null;
	private Double lastY = null;

	private double x;
	private double y;

	@Override
	public void update(ControlConfig config) {
		super.update(config);

		double xPosition = Gdx.input.getX();
		double yPosition = -Gdx.input.getY() + 1 + Gdx.graphics.getHeight();

		if(lastX != null && lastY != null){
			x = xPosition - lastX;
			y = yPosition - lastY;
		} else {
			Gdx.input.setCursorCatched(true);
			x = 0;
			y = 0;
		}
//		System.out.println("position: " + xPosition + ", " + yPosition);

		lastX = xPosition;
		lastY = yPosition;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public boolean isConnected(ControllerManager manager) {
		return true;
	}
}
