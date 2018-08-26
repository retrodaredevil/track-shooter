package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;

public class RenderObject implements Disposable {

	private final Batch batch;
	private final Skin mainSkin;
	private final Skin uiSkin;

	public RenderObject(Batch batch, Skin mainSkin, Skin uiSkin){
		this.batch = batch;
		this.mainSkin = mainSkin;
		this.uiSkin = uiSkin;
	}

	public Batch getBatch(){ return batch; }
	public Skin getMainSkin(){ return mainSkin; }
	public Skin getUISkin(){ return uiSkin; }

	@Override
	public void dispose() {
		batch.dispose();
		mainSkin.dispose();
		uiSkin.dispose();
	}
}
