package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.utils.Disposable;

import me.retrodaredevil.game.trackshooter.render.parts.Overlay;
import me.retrodaredevil.game.trackshooter.render.parts.Background;

public final class RenderParts implements Disposable{
	private final Background background;
	private final Renderable optionsMenu;
	private final Overlay overlay;

	public RenderParts(Background background, Renderable optionsMenu, Overlay overlay){
		this.background = background;
		this.overlay = overlay;
		this.optionsMenu = optionsMenu;
	}

	public void resize(int width, int height){
		background.resize(width, height);
		optionsMenu.resize(width, height);
		overlay.resize(width, height);
	}

	@Override
	public void dispose() {
		background.disposeRenderComponent();
		overlay.dispose();
	}

	public Background getBackground(){
		return background;
	}

	public Overlay getOverlay(){
		return overlay;
	}
	public Renderable getOptionsMenu(){
		return optionsMenu;
	}
}
