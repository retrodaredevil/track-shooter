package me.retrodaredevil.input;

public abstract class ButtonDPad extends JoystickPart {

	protected ButtonDPad(){
		super(JoystickType.POV);
	}

	public abstract InputPart up();
	public abstract InputPart down();
	public abstract InputPart left();
	public abstract InputPart right();

	@Override
	public double getX() {
		boolean left = left().isDown();
		boolean right = right().isDown();
		return right == left ? 0 : (right ? 1 : -1);
	}

	@Override
	public double getY() {
		boolean up = up().isDown();
		boolean down = down().isDown();
		return up == down ? 0 : (up ? 1 : -1);
	}

	@Override
	public boolean isXDeadzone() {
		return getX() == 0;
	}

	@Override
	public boolean isYDeadzone() {
		return getY() == 0;
	}

}
