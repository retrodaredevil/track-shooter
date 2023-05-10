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
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Array;

import me.retrodaredevil.game.trackshooter.render.RenderObject;
import me.retrodaredevil.game.trackshooter.render.RenderParts;

public final class Resources {


	public static void loadToSkin(Skin skin){
		// thanks http://www.freesfx.co.uk/soundeffects/lasers_weapons/
		skin.add("bullet", Gdx.audio.newSound(Gdx.files.internal("skins/main/sounds/bullet_sound.mp3")), Sound.class);
	}
	private static Drawable[] createRegions(boolean vertical, int amount, TextureRegion region){
		int width;
		int height;
		if(vertical){
			width = region.getRegionWidth();
			height = region.getRegionHeight() / amount;
		} else {
			width = region.getRegionWidth() / amount;
			height = region.getRegionHeight();
		}
		TextureRegion[][] regions2d = region.split(width, height);
		Drawable[] regions = new Drawable[regions2d.length * regions2d[0].length];
		int index = 0;
		for(TextureRegion[] nestRegion : regions2d) for(TextureRegion smallRegion : nestRegion){
			regions[index] = new TextureRegionDrawable(smallRegion);
			index++;
		}
		return regions;
	}
	public enum Points implements me.retrodaredevil.game.trackshooter.util.Points{
		P100(100, 0, "odd_bonus", 5),
		P300(300, 1, "odd_bonus", 5),
		P500(500, 2, "odd_bonus", 5),
		P700(700, 3, "odd_bonus", 5),
		P1000(1000, 4, "odd_bonus", 5),

		P200(200, 0, "even_bonus", 4),
		P400(400, 1, "even_bonus", 4),
		P800(800, 2, "even_bonus", 4),
		P1600(1600, 3, "even_bonus", 4),

		P2000(2000, 0, "large_bonus", 3),
		P3000(3000, 1, "large_bonus", 3),
		P5000(3000, 2, "large_bonus", 3),
		;
		private final int worth;
		private final int index;
		private final String imageName;
		private final int imagesSize;

		Points(int worth, int index, String imageName, int imagesSize){
			this.worth = worth;
			this.index = index;
			this.imageName = imageName;
			this.imagesSize = imagesSize;
			if(imagesSize <= 0){
				throw new IllegalArgumentException();
			}
			if(index >= imagesSize){
				throw new IllegalArgumentException();
			}
		}
		@Override
		public int getWorth(){
			return worth;
		}
		@Override
		public Drawable getDrawable(RenderObject renderObject){
			final TextureRegion region = renderObject.getMainSkin().getRegion(imageName);
			if(imagesSize == 1){
				return new TextureRegionDrawable(region);
			}
			final int width = region.getRegionWidth();
			final int height = region.getRegionHeight();

			return new TextureRegionDrawable(new TextureRegion(region, width * index / imagesSize, 0, width / imagesSize, height));
		}
	}
	public enum Shark {
		FULL_HEALTH("shark"),
		MIDDLE_HEALTH("shark_hit"),
		LOW_HEALTH("shark_worn")
		;
		private final String name;
		Shark(String name){
			this.name = name;
		}
		/**The frames for a shark [0] is right [1] is straight [2] is left */
		public Drawable[] getSprites(RenderObject renderObject){
			final TextureRegion region = renderObject.getMainSkin().getRegion(name);

			return createRegions(region.getRegionWidth() <= region.getRegionHeight(), 3, region);
		}
	}
}
