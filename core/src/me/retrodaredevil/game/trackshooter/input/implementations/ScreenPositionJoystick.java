package me.retrodaredevil.game.trackshooter.input.implementations;

import com.badlogic.gdx.Gdx;
import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickAxisFollowerPart;
import me.retrodaredevil.controller.input.JoystickType;
import me.retrodaredevil.controller.input.SimpleJoystickPart;

public class ScreenPositionJoystick extends SimpleJoystickPart {

	private final InputPart xAxis = new JoystickAxisFollowerPart(this, false);
	private final InputPart yAxis = new JoystickAxisFollowerPart(this, true);

	private final ShouldIgnorePointer shouldIgnorePointer;

	private Integer x, y;

	public ScreenPositionJoystick(ShouldIgnorePointer shouldIgnorePointer) {
		super(new JoystickType(false, true, false, false),
				false, false, true);
		this.shouldIgnorePointer = shouldIgnorePointer;
	}

	@Override
	protected void onUpdate() {
		super.onUpdate();
		x = null;
		y = null;
		for(int i = GdxScreenTouchButton.NUM_TOUCHES - 1; i >= 0; i--){
			if(Gdx.input.isTouched(i) && !shouldIgnorePointer.shouldIgnorePointer(i)){
				x = Gdx.input.getX(i);
				y = Gdx.input.getY(i);
			}
		}
	}

	@Override
	public double getX() {
		Integer r = x;
		return r == null ? 0 : r;
	}

	@Override
	public double getY() {
		Integer r = y;
		return r == null ? 0 : r;
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
	public boolean isXDeadzone() {
		return x == null;
	}

	@Override
	public boolean isYDeadzone() {
		return y == null;
	}

	@Override
	public boolean isConnected() {
		return true;
	}
}
