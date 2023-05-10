package me.retrodaredevil.game.trackshooter.input.implementations;

import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

import java.util.Objects;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.implementations.JoystickAxisFollowerPart;
import me.retrodaredevil.controller.input.JoystickType;
import me.retrodaredevil.controller.input.implementations.SimpleJoystickPart;

public class GdxTouchpadJoystick extends SimpleJoystickPart {

	private final InputPart xAxis = new JoystickAxisFollowerPart(this, partUpdater, false);
	private final InputPart yAxis = new JoystickAxisFollowerPart(this, partUpdater, true);

	private final Touchpad touchpad;

	public GdxTouchpadJoystick(Touchpad touchpad){
		super(new JoystickType(true, false, false, true), false, false, false);
		this.touchpad = Objects.requireNonNull(touchpad);
	}

	@Override
	public InputPart getXAxis() { return xAxis; }
	@Override
	public InputPart getYAxis() { return yAxis; }

	@Override
	public double getX() {
		return touchpad.getKnobPercentX();
	}

	@Override
	public double getY() {
		return touchpad.getKnobPercentY();
	}

	@Override
	public boolean isXDeadzone() {
		return getX() == 0;
	}

	@Override
	public boolean isYDeadzone() {
		return getY() == 0;
	}

	@Override
	public boolean isConnected() {
		return touchpad.isVisible() && touchpad.isTouchable() && touchpad.getParent() != null;
	}
}
