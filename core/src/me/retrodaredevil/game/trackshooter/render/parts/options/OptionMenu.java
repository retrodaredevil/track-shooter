package me.retrodaredevil.game.trackshooter.render.parts.options;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import me.retrodaredevil.controller.options.ConfigurableControllerPart;
import me.retrodaredevil.game.trackshooter.RenderObject;
import me.retrodaredevil.game.trackshooter.render.InputFocusRenderable;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;

public class OptionMenu implements InputFocusRenderable {
	private final RenderObject renderObject;
	private final Stage preferredStage;
	private OptionMenuRenderComponent renderComponent = null;
	public OptionMenu(RenderObject renderObject) {
		this.renderObject = renderObject;

		this.preferredStage = new Stage(new FitViewport(640, 640), renderObject.getBatch());
	}

	@Override
	public RenderComponent getRenderComponent() {
		return renderComponent;
	}
	public void setToController(ConfigurableControllerPart configController){
		if (renderComponent != null) {
			if(renderComponent.getConfigController() == configController){
				return; // don't do anything, it's the same
			}
			renderComponent.dispose();
		}
		if(configController == null){
			renderComponent = null;
			return;
		}
		renderComponent = new OptionMenuRenderComponent(renderObject, configController);
//		System.out.println("set new render component in options menu");
	}

	@Override
	public Stage getPreferredStage() {
		return preferredStage;
	}

	@Override
	public boolean isWantsToHandleInput() {
		return renderComponent != null;
	}

	@Override
	public int getInputPriority() {
		return 0;
	}
}
