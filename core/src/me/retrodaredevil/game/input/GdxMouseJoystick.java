package me.retrodaredevil.game.input;

import com.badlogic.gdx.Gdx;

import me.retrodaredevil.controller.ControlConfig;
import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickAxisFollowerPart;
import me.retrodaredevil.controller.input.MouseJoystick;

public class GdxMouseJoystick extends MouseJoystick {
	private Double lastX = null;
	private Double lastY = null;

	private double x;
	private double y;

	private final InputPart xAxis = new JoystickAxisFollowerPart(this, false);
	private final InputPart yAxis = new JoystickAxisFollowerPart(this, true);

	@Override
	public void update(ControlConfig config) {
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
		xAxis.update(config);
		yAxis.update(config);
		super.update(config);
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
	public InputPart getXAxis() {
		return xAxis;
	}

	@Override
	public InputPart getYAxis() {
		return yAxis;
	}

	@Override
	public boolean isConnected() {
		return true;
	}
}
