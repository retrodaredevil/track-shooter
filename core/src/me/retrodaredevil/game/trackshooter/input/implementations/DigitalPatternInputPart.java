package me.retrodaredevil.game.trackshooter.input.implementations;

import com.badlogic.gdx.Gdx;

import java.util.Arrays;
import java.util.Objects;

import me.retrodaredevil.controller.input.AutoCachingInputPart;
import me.retrodaredevil.controller.input.AxisType;
import me.retrodaredevil.game.trackshooter.util.MathUtil;

public class DigitalPatternInputPart extends AutoCachingInputPart {

	private final long[] pattern;
	private final long totalTime;

	/**
	 *
	 * @param pattern The pattern where each even index is how long it should stay off, and each odd index is how long it should on
	 */
	public DigitalPatternInputPart(long... pattern){
		super(AxisType.DIGITAL, false);
		this.pattern = Objects.requireNonNull(pattern);
		totalTime = MathUtil.sum(pattern);
	}

	@Override
	protected double calculatePosition() {
		final long time = System.currentTimeMillis() % totalTime; // time is in range [0, totalTime)

		long current = 0;
		for(int i = 0; i < pattern.length; ++i){
			final long period = pattern[i];
			current += period;
			if(time <= current){
				return (i % 2 == 1) ? 1 : 0; // if the index is odd, 1, otherwise 0
			}
		}
		Gdx.app.error("unable to calculate position", "time: " + time + " current: " + current + " pattern: " + Arrays.toString(pattern));
		return 0;
	}

	@Override
	public boolean isConnected() {
		return true;
	}
}
