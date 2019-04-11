package me.retrodaredevil.game.trackshooter.input.implementations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

import me.retrodaredevil.controller.input.AutoCachingInputPart;
import me.retrodaredevil.controller.input.AxisType;
import me.retrodaredevil.game.trackshooter.util.Util;

public class GdxScreenTouchButton extends AutoCachingInputPart {
	private static final int NUM_TOUCHES = 20;
	private final boolean needsTouchScreenForConnection;
	private final ScreenArea screenArea;
	public GdxScreenTouchButton(boolean needsTouchScreenForConnection, ScreenArea screenArea) {
		super(new AxisType(false, false, false, true), false);
		this.needsTouchScreenForConnection = needsTouchScreenForConnection;
		this.screenArea = screenArea;
	}
	public GdxScreenTouchButton(ScreenArea screenArea){
		this(true, screenArea);
	}

	@Override
	protected double calculatePosition() {
		if(Gdx.input.isTouched()){
			float width = Gdx.graphics.getWidth();
			float height = Gdx.graphics.getHeight();
			for(int i = 0; i < NUM_TOUCHES; i++){
				try {
					if (Gdx.input.isTouched(i)) {
						float x = Gdx.input.getX(i) / width;
						float y = 1 - (Gdx.input.getY(i) / height);
						if (screenArea.containsPoint(x, y)) {
							return 1;
						}
					}
				} catch(IndexOutOfBoundsException ex){
					ex.printStackTrace();
					System.err.println("index: " + i + " is too big to be a pointer.");
					break;
				}
			}
		}
		return 0;
	}

	@Override
	public boolean isConnected() {
		return !needsTouchScreenForConnection || Gdx.input.isPeripheralAvailable(Input.Peripheral.MultitouchScreen);
	}
}
