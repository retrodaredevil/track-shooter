package me.retrodaredevil.game.trackshooter.render.parts;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.Collection;
import java.util.Collections;

import me.retrodaredevil.controller.options.ConfigurableControllerPart;
import me.retrodaredevil.game.input.GameInput;
import me.retrodaredevil.game.trackshooter.InputFocusable;
import me.retrodaredevil.game.trackshooter.render.RenderObject;
import me.retrodaredevil.game.trackshooter.render.Renderable;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;
import me.retrodaredevil.game.trackshooter.save.SaveObject;

public class OptionMenu implements Renderable, InputFocusable {
	private final RenderObject renderObject;
	private final SaveObject saveObject;
	private final Stage preferredStage;
	private OptionMenuRenderComponent renderComponent = null;
	public OptionMenu(RenderObject renderObject, SaveObject saveObject) {
		this.renderObject = renderObject;
		this.saveObject = saveObject;

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
		renderComponent = new OptionMenuRenderComponent(renderObject, menuController, configController, this, saveObject);
	}
	public void closeMenu(){
		setToController(null, null);
	}

	public boolean isOpen() {
		return renderComponent != null;
	}

	//region loading/saving
	//endregion

	@Override
	public Stage getPreferredStage() {
		return preferredStage;
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
