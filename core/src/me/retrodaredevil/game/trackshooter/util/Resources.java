package me.retrodaredevil.game.trackshooter.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public final class Resources {
	public static final Texture PLAYER_TEXTURE = new Texture("player.png");
	public static final Texture SHARK_TEXTURE = new Texture("shark.png");
	public static final Texture BULLET_TEXTURE = new Texture("bullet.png");
	public static final Texture CHERRY_TEXTURE = new Texture("cherry.png");

	public static final Sound INTRO = Gdx.audio.newSound(Gdx.files.internal("intro.ogg"));
}
