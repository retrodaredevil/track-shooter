package me.retrodaredevil.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import me.retrodaredevil.controller.ControlConfig;
import me.retrodaredevil.controller.ControllerManager;
import me.retrodaredevil.controller.JoystickPart;
import me.retrodaredevil.game.trackshooter.util.MathUtil;

public class GdxGyroJoystick extends JoystickPart {
	private static final float MAX_DEGREES = 20;
	private float x, y, z;
	private Long last = null;
	public GdxGyroJoystick() {
		super(JoystickType.NORMAL);
	}


	@Override
	public void update(ControlConfig config) {
		super.update(config);
		if(last == null){
			last = System.currentTimeMillis();
		}
		float delta = Gdx.graphics.getDeltaTime();
		x += Gdx.input.getGyroscopeX() * delta; // up and down
		y += Gdx.input.getGyroscopeY() * delta; // side to side
		z += Gdx.input.getGyroscopeZ() * delta;
//		System.out.println(String.format("x: %s, y: %s, z: %s", x, y, z));
	}
	private static double degreesToFullAnalog(float degrees){
		float r = MathUtil.minChange(degrees, 0, 360);
		if(r > 180){
			r -= 360;
		}
		if(r > MAX_DEGREES){
			r = MAX_DEGREES;
		} else if(r < -MAX_DEGREES){
			r = -MAX_DEGREES;
		}
		return r / MAX_DEGREES;
	}

	@Override
	public double getX() {
		double r = degreesToFullAnalog((float) Math.toDegrees(x));
		System.out.println("x: " + r);
		return r;
	}

	@Override
	public double getY() {
		double r = degreesToFullAnalog((float) Math.toDegrees(y));
		System.out.println("y: " + r);
		return r;
	}

	@Override
	public boolean isXDeadzone() {
		return false;
	}

	@Override
	public boolean isYDeadzone() {
		return false;
	}

	@Override
	public boolean isConnected(ControllerManager manager) {
		return Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope);
	}
}
