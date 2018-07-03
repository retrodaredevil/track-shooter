package me.retrodaredevil.game.trackshooter.util;

import com.badlogic.gdx.math.Vector2;

public final class MathUtil {
	private static final Vector2 temp = new Vector2();

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
	 * <br/> <br/>
	 * minChange(1, 5, 4) == 0 <br/>
	 * minChange(1, 5, 5) == 1 <br/>
	 * minChange(5, 1, 5) == -1 <br/>
	 * <br/>
	 * minChange(30, 300, 360) == 90 <br/>
	 * minChange(180, 0, 360) == 180 <br/>
	 * minChange(181, 0, 360) == -179 <br/>
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

	public static float angle(Vector2 start, Vector2 end){
		return temp.set(end).sub(start).angle();
	}

	/**
	 * NOTE: It doesn't matter what order the passed parameters are in
	 * <br/>
	 * getAngleAlikeRatio(90, 90) == 1 <br/>
	 * getAngleAlikeRatio(90, -90) == -1 <br/>
	 * getAngleAlikeRatio(90, 0) == 0 <br/>
	 *
	 * @param anglePart An angle to test the alikeness of
	 * @param desiredAngle An angle to test the alikeness of
	 * @return A number between -1 and 1 where 1 is 100% alike and -1 is totally opposite
	 */
	public static float getAngleAlikeRatio(float anglePart, float desiredAngle){
		float positive = minDistance(anglePart, desiredAngle, 360);
		float negative = minDistance(anglePart, desiredAngle + 180, 360);
		int sign = positive >= negative ? 1 : -1;
		float bigger = Math.max(positive, negative);
		return sign * (90 - bigger) / 90.0f;
	}
}
