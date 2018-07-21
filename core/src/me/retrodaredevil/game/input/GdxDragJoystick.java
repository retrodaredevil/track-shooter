package me.retrodaredevil.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickAxisFollowerPart;
import me.retrodaredevil.controller.input.MouseJoystick;
import me.retrodaredevil.game.trackshooter.util.Util;

@Deprecated
public class GdxDragJoystick extends MouseJoystick {
	private static final int NUM_TOUCHES = 20;
	private final boolean needsTouchScreenForConnection;
	private final Rectangle screenArea = new Rectangle();
	private final float multiplier;

	private final InputPart xAxis = new JoystickAxisFollowerPart(this, false);
	private final InputPart yAxis = new JoystickAxisFollowerPart(this, true);

	private float x, y;

	/**
	 *
	 * @param needsTouchScreenForConnection
	 * @param screenArea A proportional rectangle. Ex: (0, 0, 1, 1) is the entire screen (.5, 0, .5, 1) is the right half
	 */
	public GdxDragJoystick(boolean needsTouchScreenForConnection, Rectangle screenArea, float multiplier) {
		super();
		this.needsTouchScreenForConnection = needsTouchScreenForConnection;
		this.screenArea.set(screenArea);
		this.multiplier = multiplier;
	}
	public GdxDragJoystick(Rectangle screenArea){
		this(true, screenArea, 1);
	}
	public GdxDragJoystick(Rectangle screenArea, float multiplier){
		this(true, screenArea, multiplier);
	}

	@Override
	protected void onUpdate() {
		super.onUpdate();
		Rectangle area = Util.proportionalRectangleToScreenArea(screenArea);
		x = 0;
		y = 0;
		if(Gdx.input.isTouched()){
			for(int i = 0; i < NUM_TOUCHES; i++){
				try {
					if (Gdx.input.isTouched(i)) {
						if (area.contains(Gdx.input.getX(i), Gdx.input.getY(i))) {
							x = Gdx.input.getDeltaX(i) * multiplier;
							y = Gdx.input.getDeltaY(i) * multiplier;
						}
					}
				} catch(IndexOutOfBoundsException ex){
					ex.printStackTrace();
					System.err.println("index: " + i + " is too big to be a pointer.");
				}
			}
		}
	}

	@Override
	public boolean isConnected() {
		return !needsTouchScreenForConnection || Gdx.input.isPeripheralAvailable(Input.Peripheral.MultitouchScreen);
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
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

}
