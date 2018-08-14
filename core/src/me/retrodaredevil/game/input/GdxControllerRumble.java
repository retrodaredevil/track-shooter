package me.retrodaredevil.game.input;

import com.badlogic.gdx.controllers.Controller;

import me.retrodaredevil.controller.SimpleControllerPart;
import me.retrodaredevil.controller.output.ControllerRumble;

/**
 * Currently, rumble is not supported by libgdx but in the future they may be so
 * even though this class doesn't work right now, you can use it as a place holder for a gyro
 * for controllers.
 */
public class GdxControllerRumble extends SimpleControllerPart implements ControllerRumble {

	private final Controller controller;

	public GdxControllerRumble(Controller controller){
		this.controller = controller;
	}

	@Override
	public void rumble(double amount) {
		throwException();
	}

	@Override
	public void rumble(double left, double right) {
		throwException();
	}

	@Override
	public void rumble(long millis, double left, double right) {
		throwException();
	}

	@Override
	public void rumble(long millis, double amount) {
		throwException();
	}

	private void throwException(){
		throw new UnsupportedOperationException("The rumble is not connected. Make sure to check isConnected(). Rumble is not supported yet.");
	}

	@Override
	public boolean isLeftAndRightSupported() {
		return false;
	}

	@Override
	public boolean isTimingNativelyImplemented() {
		return false;
	}

	@Override
	public boolean isAnalogRumbleSupported() {
		return false;
	}

	@Override
	public boolean isAnalogRumbleNativelySupported() {
		return false;
	}

	@Override
	public boolean isConnected() {
		return false;
	}
}
