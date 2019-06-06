package me.retrodaredevil.game.trackshooter.input.implementations;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickType;
import me.retrodaredevil.controller.input.implementations.JoystickAxisFollowerPart;
import me.retrodaredevil.controller.input.implementations.SimpleJoystickPart;
import me.retrodaredevil.game.trackshooter.util.Util;

public class ControllerPovJoystick extends SimpleJoystickPart {
	private static final double INV_SQRT2 = 1.0 / Math.sqrt(2.0);

	private final InputPart xAxis = new JoystickAxisFollowerPart(this, partUpdater, false);
	private final InputPart yAxis = new JoystickAxisFollowerPart(this, partUpdater, true);

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
			case northEast: x = INV_SQRT2; y = INV_SQRT2; break;
			case east: x = 1; y = 0; break;
			case southEast: x = INV_SQRT2; y = -INV_SQRT2; break;
			case south: x = 0; y = -1; break;
			case southWest: x = -INV_SQRT2; y = -INV_SQRT2; break;
			case west: x = -1; y = 0; break;
			case northWest: x = -INV_SQRT2; y = INV_SQRT2; break;
			default: throw new UnsupportedOperationException("Unknown pov: " + pov);
		}
	}

	@Override
	protected double calculateMagnitude(double x, double y) {
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
