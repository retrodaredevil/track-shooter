package me.retrodaredevil.game.trackshooter.util;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import me.retrodaredevil.game.trackshooter.render.RenderObject;

public interface Points {
	int getWorth();
	Drawable getDrawable(RenderObject renderObject);
}
