package me.retrodaredevil.game.trackshooter.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public final class Resources {
	// Constants
	// Textures
	public static final Texture PLAYER_TEXTURE = new Texture("player.png");
	public static final Texture SHARK_TEXTURE = new Texture("shark.png");
	/**The frames for a shark [0] is right [1] is straight [2] is left */
	public static final TextureRegionDrawable[] SHARK_REGIONS = createRegions(SHARK_TEXTURE.getWidth() <= SHARK_TEXTURE.getHeight(), 3, SHARK_TEXTURE);
	public static final Texture BULLET_TEXTURE = new Texture("bullet.png");
	public static final Texture CHERRY_TEXTURE = new Texture("cherry.png");
	public static final Texture EVEN_BONUS = new Texture("even_bonus.png");
	/** 200, 400, 800, 1600*/
	public static final TextureRegionDrawable[] EVEN_BONUS_REGIONS = createRegions(false, 4, EVEN_BONUS);
	public static final Texture ODD_BONUS = new Texture("odd_bonus.png");
	/** 100, 300, 500, 700, 1000*/
	public static final TextureRegionDrawable[] ODD_BONUS_REGIONS = createRegions(false, 5, ODD_BONUS);



	// Sounds
	public static final Sound INTRO = Gdx.audio.newSound(Gdx.files.internal("intro.ogg"));


	// initializer
	static {
	}


	// Methods
	private static TextureRegionDrawable[] createRegions(boolean vertical, int amount, Texture texture){
		int width;
		int height;
		if(vertical){
			width = texture.getWidth();
			height = texture.getHeight() / amount;
		} else {
			width = texture.getWidth() / amount;
			height = texture.getHeight();
		}
		TextureRegion[][] regions2d = TextureRegion.split(texture, width, height);
		TextureRegionDrawable[] regions = new TextureRegionDrawable[regions2d.length * regions2d[0].length];
		int index = 0;
		for(TextureRegion[] nestRegion : regions2d) for(TextureRegion region : nestRegion){
			regions[index] = new TextureRegionDrawable(region);
			index++;
		}
		return regions;
	}
}
