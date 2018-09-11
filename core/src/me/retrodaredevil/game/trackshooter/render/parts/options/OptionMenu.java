package me.retrodaredevil.game.trackshooter.render.parts.options;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.Collection;
import java.util.Collections;

import me.retrodaredevil.controller.options.ConfigurableControllerPart;
import me.retrodaredevil.game.input.GameInput;
import me.retrodaredevil.game.trackshooter.InputFocusable;
import me.retrodaredevil.game.trackshooter.RenderObject;
import me.retrodaredevil.game.trackshooter.render.Renderable;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;

public class OptionMenu implements Renderable, InputFocusable {
	private final RenderObject renderObject;
	private final Stage preferredStage;
	private OptionMenuRenderComponent renderComponent = null;
	public OptionMenu(RenderObject renderObject) {
		this.renderObject = renderObject;

		this.preferredStage = new Stage(new ExtendViewport(480, 480), renderObject.getBatch());
	}

	@Override
	public RenderComponent getRenderComponent() {
		return renderComponent;
	}
	public void setToController(ConfigurableControllerPart configController, GameInput menuController){
		if (renderComponent != null) {
			if(renderComponent.getConfigController() == configController){
				System.out.println("it's the same!");
				return; // don't do anything, it's the same
			}
			renderComponent.dispose();
		}
		if(configController == null){
			renderComponent = null;
			return;
		}
		renderComponent = new OptionMenuRenderComponent(this, renderObject, configController, menuController);
	}
	public void closeMenu(){
		setToController(null, null);
	}

	@Override
	public Stage getPreferredStage() {
		return preferredStage;
	}


	@Override
	public void onFocusGiven(Stage mainStage) {
	}

	@Override
	public Collection<? extends InputProcessor> getInputProcessorsToFocus(Stage mainStage) {
		return Collections.singleton(preferredStage);
	}

	@Override
	public boolean isWantsToFocus() {
		return renderComponent != null;
	}

	@Override
	public int getFocusPriority() {
		return 0;
	}
}
