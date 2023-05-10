package me.retrodaredevil.game.trackshooter.util;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * This is a util class for changing Rectangles to correct positions and sizes.
 * <p>
 * You can use Rectangle#setCenter to set the position, but for the size, you should use hitboxSetSize
 */
public final class HitboxUtil {
	private static Vector2 temp = new Vector2();

	public static Rectangle createHitbox(float x, float y, float width, float height){
		return hitboxSet(new Rectangle(), x, y, width, height);
	}

//	public static float getHitboxCenterX(Rectangle hitbox){
//		return hitbox.getX() + (hitbox.getWidth() / 2f);
//	}
//	public static float getHitboxCenterY(Rectangle hitbox){
//		return hitbox.getY() + (hitbox.getHeight() / 2f);
//	}
//	public static Vector2 getHitboxCenter(Rectangle hitbox){
//		return new Vector2(getHitboxCenterX(hitbox), getHitboxCenterY(hitbox));
//	}

	/**
	 * @param hitbox The Rectangle to change its data for
	 * @param x The x value for the center of the rectangle
	 * @param y The y value for the center of the rectangle
	 * @param width The width of the rectangle
	 * @param height The height of the rectangle
	 * @return passed hitbox for chaining. its position will be (x-width/2,y-height/2). (Its center will be (x, y))
	 */
	public static Rectangle hitboxSet(Rectangle hitbox, float x, float y, float width, float height){
		hitbox.set(x - (width / 2f), y - (height / 2f), width, height);
		return hitbox;
	}

	/**
	 * @param hitbox The hitbox to change the size of without altering the center
	 * @param width The width
	 * @param height The height
	 * @return The passed hitbox for chaining
	 */
	public static Rectangle hitboxSetSize(Rectangle hitbox, float width, float height){
		hitbox.getCenter(temp);
		return hitboxSet(hitbox, temp.x, temp.y, width, height);
	}
//	public static Rectangle hitboxSetPosition(Rectangle hitbox, float x, float y){
//		return hitboxSet(hitbox, x, y, hitbox.getWidth(), hitbox.getHeight());
//	}
//	public static Rectangle hitboxSetPosition(Rectangle hitbox, Vector2 position){
//		return hitboxSetPosition(hitbox, position.x, position.y);
//	}

}
