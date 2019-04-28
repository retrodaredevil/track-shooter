package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * A {@link Touchpad} that is hacked to allow for the {@link #getPointer()} method
 */
public class PointerTouchpad extends Touchpad {
	private int pointer;
	public PointerTouchpad(float deadzoneRadius, Skin skin) {
		super(deadzoneRadius, skin);
		getListeners().clear();
		addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (touched) return false;
				touched = true;
				calculatePositionAndValue(x, y, false);
				PointerTouchpad.this.pointer = pointer;
				return true;
			}

			@Override
			public void touchDragged (InputEvent event, float x, float y, int pointer) {
				calculatePositionAndValue(x, y, false);
			}

			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				touched = false;
				calculatePositionAndValue(x, y, resetOnTouchUp);
				PointerTouchpad.this.pointer = -1;
			}
		});
	}
	public int getPointer(){
		return pointer;
	}
}
