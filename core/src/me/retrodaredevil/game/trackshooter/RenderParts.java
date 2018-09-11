package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.utils.Disposable;

import me.retrodaredevil.game.trackshooter.render.parts.Overlay;
import me.retrodaredevil.game.trackshooter.render.parts.Background;
import me.retrodaredevil.game.trackshooter.render.parts.options.OptionMenu;

public final class RenderParts implements Disposable{
	private final Background background;
	private final OptionMenu optionsMenu;
	private final Overlay overlay;
	private final InputMultiplexer inputMultiplexer;

	public RenderParts(Background background, OptionMenu optionsMenu, Overlay overlay, InputMultiplexer inputMultiplexer){
		this.background = background;
		this.overlay = overlay;
		this.optionsMenu = optionsMenu;
		this.inputMultiplexer = inputMultiplexer;
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
	public OptionMenu getOptionsMenu(){
		return optionsMenu;
	}
	public InputMultiplexer getInputMultiplexer(){ return inputMultiplexer; }
}
