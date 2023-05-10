package me.retrodaredevil.game.trackshooter.util;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

public final class GdxUtil {
	private GdxUtil(){}

	public static boolean isMobilePhone(){
		Application.ApplicationType type = Gdx.app.getType();
		return type == Application.ApplicationType.Android
				|| type == Application.ApplicationType.iOS;
	}
}
