package me.retrodaredevil.game.trackshooter.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public final class Resources {
	// Constants
	// Textures
	private static final Texture SHARK_TEXTURE = new Texture("skins/main/textures/shark.png");
	private static final Texture SHARK_HIT = new Texture("skins/main/textures/shark_hit.png");
	private static final Texture SHARK_WORN = new Texture("skins/main/textures/shark_worn.png");
	/**The frames for a shark [0] is right [1] is straight [2] is left */
	public static final TextureRegionDrawable[] SHARK_REGIONS = createRegions(SHARK_TEXTURE.getWidth() <= SHARK_TEXTURE.getHeight(), 3, SHARK_TEXTURE);
	public static final TextureRegionDrawable[] SHARK_REGIONS_HIT = createRegions(SHARK_HIT.getWidth() <= SHARK_HIT.getHeight(), 3, SHARK_HIT);
	public static final TextureRegionDrawable[] SHARK_REGIONS_WORN = createRegions(SHARK_WORN.getWidth() <= SHARK_WORN.getHeight(), 3, SHARK_WORN);

	private static final Texture EVEN_BONUS = new Texture("skins/main/textures/even_bonus.png");
	/** 200, 400, 800, 1600*/
	private static final TextureRegionDrawable[] EVEN_BONUS_REGIONS = createRegions(false, 4, EVEN_BONUS);
	private static final Texture ODD_BONUS = new Texture("skins/main/textures/odd_bonus.png");
	/** 100, 300, 500, 700, 1000*/
	private static final TextureRegionDrawable[] ODD_BONUS_REGIONS = createRegions(false, 5, ODD_BONUS);



	// Sounds
	public static void loadToSkin(Skin skin){

		// thanks http://www.freesfx.co.uk/soundeffects/lasers_weapons/
		skin.add("bullet", Gdx.audio.newSound(Gdx.files.internal("skins/main/sounds/bullet_sound.mp3")), Sound.class);

		// TODO shark animations, bonuses


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
	public enum Points {
		P100(100, ODD_BONUS_REGIONS[0]),
		P300(300, ODD_BONUS_REGIONS[1]),
		P500(500, ODD_BONUS_REGIONS[2]),
		P700(700, ODD_BONUS_REGIONS[3]),
		P1000(1000, ODD_BONUS_REGIONS[4]),

		P200(200, EVEN_BONUS_REGIONS[0]),
		P400(400, EVEN_BONUS_REGIONS[1]),
		P800(800, EVEN_BONUS_REGIONS[2]),
		P1600(1600, EVEN_BONUS_REGIONS[3]),
		;
		private final int worth;
		private final TextureRegionDrawable regionDrawable;

		Points(int worth, TextureRegionDrawable regionDrawable){
			this.worth = worth;
			this.regionDrawable = regionDrawable;
		}
		public int getWorth(){
			return worth;
		}
		public Drawable getDrawable(){
			return regionDrawable;
		}
	}
}
