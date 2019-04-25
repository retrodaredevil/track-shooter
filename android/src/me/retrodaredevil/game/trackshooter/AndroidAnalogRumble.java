package me.retrodaredevil.game.trackshooter;

import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import me.retrodaredevil.game.trackshooter.input.RumbleAnalogControl;

import static java.lang.Math.min;
import static java.util.Objects.requireNonNull;

public class AndroidAnalogRumble implements RumbleAnalogControl {
	private final Vibrator vibrator;

	AndroidAnalogRumble(Vibrator vibrator){
		this.vibrator = requireNonNull(vibrator);
	}
	@Override
	public boolean isSupported() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			return vibrator.hasAmplitudeControl() && vibrator.hasVibrator();
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public void setControl(double intensity, long time) {
		if(time < 0){
			throw new IllegalArgumentException("time must be > 0");
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			if(!isSupported()){
				throw new UnsupportedOperationException("This isn't supported!");
			}
			vibrator.vibrate(VibrationEffect.createOneShot(min(time, Integer.MAX_VALUE), (int) Math.round(255 * intensity)));
		} else {
			throw new UnsupportedOperationException();
		}
	}
}
