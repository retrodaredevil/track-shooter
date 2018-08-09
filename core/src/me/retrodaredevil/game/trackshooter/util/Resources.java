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
	public static final Texture SPACE_STATION_TEXTURE = new Texture("space_station.png");
	public static final Texture SNIPER_TEXTURE = new Texture("sniper.png");
	public static final Texture SNAKE_PART_TEXTURE = new Texture("snake_part.png");
	public static final Texture CARGO_SHIP = new Texture("cargo_ship.png");

	private static final Texture SHARK_TEXTURE = new Texture("shark.png");
	private static final Texture SHARK_HIT = new Texture("shark_hit.png");
	private static final Texture SHARK_WORN = new Texture("shark_worn.png");
	/**The frames for a shark [0] is right [1] is straight [2] is left */
	public static final TextureRegionDrawable[] SHARK_REGIONS = createRegions(SHARK_TEXTURE.getWidth() <= SHARK_TEXTURE.getHeight(), 3, SHARK_TEXTURE);
	public static final TextureRegionDrawable[] SHARK_REGIONS_HIT = createRegions(SHARK_HIT.getWidth() <= SHARK_HIT.getHeight(), 3, SHARK_HIT);
	public static final TextureRegionDrawable[] SHARK_REGIONS_WORN = createRegions(SHARK_WORN.getWidth() <= SHARK_WORN.getHeight(), 3, SHARK_WORN);

	public static final Texture BULLET_TEXTURE = new Texture("bullet.png");
	public static final Texture CHERRY_TEXTURE = new Texture("cherry.png");
	public static final Texture POWERUP_TEXTURE = new Texture("powerup.png");
	private static final Texture EVEN_BONUS = new Texture("even_bonus.png");
	/** 200, 400, 800, 1600*/
	public static final TextureRegionDrawable[] EVEN_BONUS_REGIONS = createRegions(false, 4, EVEN_BONUS);
	private static final Texture ODD_BONUS = new Texture("odd_bonus.png");
	/** 100, 300, 500, 700, 1000*/
	public static final TextureRegionDrawable[] ODD_BONUS_REGIONS = createRegions(false, 5, ODD_BONUS);



	// Sounds
//	public static final Sound INTRO = Gdx.audio.newSound(Gdx.files.internal("intro.ogg"));
	// thanks http://www.freesfx.co.uk/soundeffects/lasers_weapons/
	public static final Sound BULLET_SOUND = Gdx.audio.newSound(Gdx.files.internal("bullet_sound.mp3"));


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
	public enum Points {
		P100(100, ODD_BONUS_REGIONS[0]),
		P300(300, ODD_BONUS_REGIONS[1]),
		P500(500, ODD_BONUS_REGIONS[2]),
		P700(700, ODD_BONUS_REGIONS[3]),
		P1000(1000, ODD_BONUS_REGIONS[4]),

		P200(200, ODD_BONUS_REGIONS[0]),
		P400(400, ODD_BONUS_REGIONS[1]),
		P800(800, ODD_BONUS_REGIONS[2]),
		P1600(1600, ODD_BONUS_REGIONS[3]),
		;
		private int worth;
		private TextureRegionDrawable regionDrawable;

		Points(int worth, TextureRegionDrawable regionDrawable){
			this.worth = worth;
			this.regionDrawable = regionDrawable;
		}
		public int getWorth(){
			return worth;
		}
		public TextureRegionDrawable getDrawable(){
			return regionDrawable;
		}
	}
}
