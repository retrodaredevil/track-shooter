package me.retrodaredevil.game.trackshooter.util;

public final class MathUtil {

	/**
	 *
	 * @param a Number on the left of the MOD
	 * @param b Number on the right of the MOD
	 * @return The modulus of a MOD b where the return value is not negative
	 */
	public static float mod(float a, float b){
		float r = a % b;
		if(r < 0){
			r += b;
		}
		return r;
	}

	/**
	 * returns a - b or, when |a - b| > wrap / 2, it finds a quicker way
	 * <p>
	 * minChange(1, 5, 4) == 0
	 * minChange(1, 5, 5) == 1
	 * minChange(5, 1, 5) == -1
	 * <p>
	 * minChange(30, 300, 360) == 90
	 * minChange(180, 0, 360) == 180
	 * minChange(181, 0, 360) == -179
	 * @param a Usually the desired variable to get to
	 * @param b Usually the current variable to change
	 * @param wrap The number that it "wraps" at. Ex: if wrap is 10, 2 is the same as 12
	 * @return A positive or negative number that if added to b is the smallest change to get to a.
	 */
	public static float minChange(float a, float b, float wrap){
		a = mod(a, wrap);
		b = mod(b, wrap);
		float change = a - b;
		if(Math.abs(change) > wrap / 2.0f){
			if(change < 0){
				change += wrap;
			} else {
				change -= wrap;
			}
		}
		return change;
	}

	/**
	 * @return return Math.abs(minChange(a, b, wrap));
	 */
	public static float minDistance(float a, float b, float wrap){
		return Math.abs(minChange(a, b, wrap));
	}
}
