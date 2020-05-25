package me.retrodaredevil.game.trackshooter;

import android.view.WindowManager;

import me.retrodaredevil.game.trackshooter.input.InputQuirk;

public class WearAndroidLauncher extends GoogleAndroidLauncher {
	@Override
	protected InputQuirk getInputQuirk() {
		return InputQuirk.WEAR;
	}

	@Override
	protected void postInitialize() {
		super.postInitialize();
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
}
