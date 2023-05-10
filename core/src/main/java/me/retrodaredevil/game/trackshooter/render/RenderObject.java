package me.retrodaredevil.game.trackshooter.render;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;

/**
 * An object that contains other necessary objects to help render things
 */
public final class RenderObject implements Disposable {

	private final Batch batch;
	private final Skin mainSkin;
	private final Skin uiSkin;
	private final Skin arcadeSkin;

	public RenderObject(Batch batch, Skin mainSkin, Skin uiSkin, Skin arcadeSkin){
		this.batch = batch;
		this.mainSkin = mainSkin;
		this.uiSkin = uiSkin;
		this.arcadeSkin = arcadeSkin;
	}

	public Batch getBatch(){ return batch; }
	public Skin getMainSkin(){ return mainSkin; }
	public Skin getUISkin(){ return uiSkin; }
	public Skin getArcadeSkin(){ return arcadeSkin; }

	@Override
	public void dispose() {
		batch.dispose();
		mainSkin.dispose();
		uiSkin.dispose();
		arcadeSkin.dispose();
	}
}
