package me.retrodaredevil.game.input.implementations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Objects;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickAxisFollowerPart;
import me.retrodaredevil.controller.input.JoystickType;
import me.retrodaredevil.controller.input.SimpleJoystickPart;
import me.retrodaredevil.controller.options.OptionValue;
import me.retrodaredevil.controller.options.OptionValues;
import me.retrodaredevil.game.trackshooter.util.Util;

public class GdxHiddenTouchJoystick extends SimpleJoystickPart {

	private final InputPart xAxis = new JoystickAxisFollowerPart(this, false);
	private final InputPart yAxis = new JoystickAxisFollowerPart(this, true);

	private final Rectangle proportionalScreenArea = new Rectangle();
	private final OptionValue minimumProportionalDiameter;

	private float x, y;

	public GdxHiddenTouchJoystick(Rectangle proportionalScreenArea, OptionValue minimumProportionalDiameter){
		super(new JoystickType(true, true, false, true), false, false);
		this.proportionalScreenArea.set(Objects.requireNonNull(proportionalScreenArea));
		this.minimumProportionalDiameter = Objects.requireNonNull(minimumProportionalDiameter);
	}
	public GdxHiddenTouchJoystick(Rectangle proportionalScreenArea){
		this(proportionalScreenArea, OptionValues.createAnalogRangedOptionValue(.5, 1, .8));
	}

	public OptionValue getMinimumProportionalDiameterOptionValue(){
		return minimumProportionalDiameter;
	}


	@Override
	protected void onUpdate() {
		super.onUpdate();
		Rectangle screenArea = Util.proportionalRectangleToScreenArea(proportionalScreenArea);
		final Vector2 center = screenArea.getCenter(new Vector2());
		final float radius = (float) minimumProportionalDiameter.getOptionValue() * Math.min(screenArea.getWidth(), screenArea.getHeight()) / 2f;

		Vector2 maxPosition = new Vector2();
		for(int i = 0; i < GdxMouseAxis.MAX_POINTERS; i++){
			try {
				if(!Gdx.input.isTouched(i)){
					continue;
				}
				int xPosition = Gdx.input.getX(i);
				int yPosition = Gdx.input.getY(i);
				if(screenArea.contains(xPosition, yPosition)){
					Vector2 position = new Vector2(xPosition - center.x, yPosition - center.y);
					if(position.len2() > maxPosition.len2()){
						maxPosition = position;
					}
				}
			} catch(IndexOutOfBoundsException ex){
				ex.printStackTrace();
				System.err.println("index: " + i + " is too big to be a pointer.");
			}
		}
		this.x = maxPosition.x / radius;
		this.y = maxPosition.y / -radius;
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
		return Gdx.input.isPeripheralAvailable(Input.Peripheral.MultitouchScreen);
	}
}
