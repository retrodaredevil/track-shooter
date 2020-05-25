package me.retrodaredevil.game.trackshooter;

import android.view.MotionEvent;
import android.view.WindowManager;

import me.retrodaredevil.controller.input.AxisType;
import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.implementations.AutoCachingInputPart;
import me.retrodaredevil.game.trackshooter.input.InputConfig;
import me.retrodaredevil.game.trackshooter.input.InputQuirk;

public class WearAndroidLauncher extends GoogleAndroidLauncher {
	private final InputPart rotateAxis;
	private float delta = 0;
	{
		rotateAxis = new AutoCachingInputPart(new AxisType(true, true, true, true)) {
			@Override
			protected double calculatePosition() {
				float r = delta;
				delta = 0;
				return r * 10;
			}

			@Override
			public boolean isConnected() {
				return true;
			}
		};
	}

	@Override
	protected InputConfig getInputConfig() {
		return new InputConfig(InputQuirk.WEAR, rotateAxis);
	}

	@Override
	protected void postInitialize() {
		super.postInitialize();
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		graphics.getView().setOnGenericMotionListener((v, ev) -> {
			System.out.println(ev.getAction());
			if (ev.getAction() == MotionEvent.ACTION_SCROLL) {
				float delta = ev.getAxisValue(MotionEvent.AXIS_SCROLL);
				this.delta += delta;
				return true;
			}
			return false;
		});
	}
}
