package me.retrodaredevil.game.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;

import java.util.Collections;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickAxisFollowerPart;
import me.retrodaredevil.controller.input.JoystickType;
import me.retrodaredevil.controller.input.SimpleJoystickPart;
import me.retrodaredevil.game.trackshooter.util.Util;

public class ControllerPovJoystick extends SimpleJoystickPart{
	private static final double SQRT2 = 1.0 / Math.sqrt(2.0);

	private final InputPart xAxis = new JoystickAxisFollowerPart(this, false);
	private final InputPart yAxis = new JoystickAxisFollowerPart(this, true);

	private final Controller controller;
	private final int povCode;

	private double x, y;

	public ControllerPovJoystick(Controller controller, int povCode){
		super(new JoystickType(false, false, false, true), false,false);
		this.controller = controller;
		this.povCode = povCode;
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
	protected void onUpdate() {
		super.onUpdate();
		PovDirection pov = controller.getPov(povCode);
		switch(pov){
			case center: x = 0; y = 0; break;
			case north: x = 0; y = 1; break;
			case northEast: x = SQRT2; y = SQRT2; break;
			case east: x = 1; y = 0; break;
			case southEast: x = SQRT2; y = -SQRT2; break;
			case south: x = 0; y = -1; break;
			case southWest: x = -SQRT2; y = -SQRT2; break;
			case west: x = -1; y = 0; break;
			case northWest: x = -SQRT2; y = SQRT2; break;
			default: throw new UnsupportedOperationException("Unknown pov: " + pov);
		}
//		System.out.println("magnitude will be: " + Math.hypot(x, y));
	}

	@Override
	protected double calculateMagnitude(double x, double y) {
		assert x == this.x && y == this.y : "Our x or y values got changed";
		return Math.round(super.calculateMagnitude(x, y));
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
	public boolean isXDeadzone() {
		return x == 0;
	}

	@Override
	public boolean isYDeadzone() {
		return y == 0;
	}

	@Override
	public boolean isConnected() {
		return Util.isControllerConnected(controller);
	}
}
