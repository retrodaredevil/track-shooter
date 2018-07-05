package me.retrodaredevil.game.input;

import com.badlogic.gdx.controllers.Controller;

import me.retrodaredevil.controller.output.ControllerRumble;

public class GdxControllerRumble extends ControllerRumble {

	private final Controller controller;

	public GdxControllerRumble(Controller controller){
		this.controller = controller;
	}

	@Override
	public void rumble(float amount) {
		this.rumble(amount, amount);
	}

	@Override
	public void rumble(float left, float right) {
		throw new UnsupportedOperationException("The rumble is not connected. Make sure to check isConnected(). Rumble is not supported yet.");
	}

	@Override
	public boolean isConnected() {
		return false;
	}
}
