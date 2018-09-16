package me.retrodaredevil.game.trackshooter.render.parts.options;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.Collection;
import java.util.Collections;

import me.retrodaredevil.controller.options.ConfigurableControllerPart;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.controller.options.OptionValue;
import me.retrodaredevil.game.input.GameInput;
import me.retrodaredevil.game.trackshooter.InputFocusable;
import me.retrodaredevil.game.trackshooter.RenderObject;
import me.retrodaredevil.game.trackshooter.render.Renderable;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;

public class OptionMenu implements Renderable, InputFocusable {
	private final RenderObject renderObject;
	private final Stage preferredStage;
	private final Preferences optionPreferences;
	private OptionMenuRenderComponent renderComponent = null;
	public OptionMenu(RenderObject renderObject) {
		this.renderObject = renderObject;

		this.preferredStage = new Stage(new ExtendViewport(480, 480), renderObject.getBatch());
		this.optionPreferences = Gdx.app.getPreferences("options");
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
		renderComponent = new OptionMenuRenderComponent(this, renderObject, configController, menuController, optionPreferences);
	}
	public void closeMenu(){
		setToController(null, null);
	}

	//region loading/saving
	public void loadControllerConfiguration(ConfigurableControllerPart configController){
		Collection<? extends ControlOption> options = configController.getControlOptions();
		for(ControlOption option : options){
			loadControlOption(option);
		}
		System.out.println("Loaded " + options.size() + " options");
	}

	/**
	 * Overrides the value of the control option using the saved value or leaves it untouched if
	 * there was no saved value found
	 * @param controlOption
	 */
	public void loadControlOption(ControlOption controlOption){
		OptionValue value = controlOption.getOptionValue();
		float savedValue = optionPreferences.getFloat(controlOption.getKey(), (float) value.getDefaultOptionValue());
		if(savedValue < value.getMinOptionValue() || savedValue > value.getMaxOptionValue()){
			value.setToDefaultOptionValue();
		} else {
			value.setOptionValue(savedValue);
		}
	}

	/**
	 * Saves the value of the control option to a file
	 * @param controlOption The control option
	 */
	public void saveControlOption(ControlOption controlOption, boolean flush){
		optionPreferences.putFloat(controlOption.getKey(), (float) controlOption.getOptionValue().getOptionValue());
		if(flush){
			optionPreferences.flush();
		}
	}
	/**
	 * @see #saveControlOption(ControlOption, boolean)
	 */
	public void saveControlOption(ControlOption controlOption){
		this.saveControlOption(controlOption, true);
	}
	//endregion

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
