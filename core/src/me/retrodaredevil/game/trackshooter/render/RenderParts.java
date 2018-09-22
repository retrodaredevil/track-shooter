package me.retrodaredevil.game.trackshooter.render;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.utils.Disposable;

import me.retrodaredevil.game.trackshooter.render.parts.Overlay;
import me.retrodaredevil.game.trackshooter.render.parts.Background;
import me.retrodaredevil.game.trackshooter.render.parts.OptionMenu;
import me.retrodaredevil.game.trackshooter.render.parts.TouchpadRenderer;

public final class RenderParts implements Disposable{
	private final Background background;
	private final OptionMenu optionsMenu;
	private final Overlay overlay;
	private final TouchpadRenderer touchpadRenderer;
	private final InputMultiplexer inputMultiplexer;

	public RenderParts(Background background, OptionMenu optionsMenu, Overlay overlay, TouchpadRenderer touchpadRenderer, InputMultiplexer inputMultiplexer){
		this.background = background;
		this.overlay = overlay;
		this.optionsMenu = optionsMenu;
		this.touchpadRenderer = touchpadRenderer;
		this.inputMultiplexer = inputMultiplexer;
	}

	public void resize(int width, int height){
		background.resize(width, height);
		optionsMenu.resize(width, height);
		overlay.resize(width, height);
		touchpadRenderer.resize(width, height);
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
	public OptionMenu getOptionsMenu(){
		return optionsMenu;
	}
	public TouchpadRenderer getTouchpadRenderer() { return touchpadRenderer; }
	public InputMultiplexer getInputMultiplexer(){ return inputMultiplexer; }
}
