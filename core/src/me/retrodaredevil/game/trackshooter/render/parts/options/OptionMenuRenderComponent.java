package me.retrodaredevil.game.trackshooter.render.parts.options;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import me.retrodaredevil.controller.options.ConfigurableControllerPart;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.game.input.GameInput;
import me.retrodaredevil.game.trackshooter.RenderObject;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;

public class OptionMenuRenderComponent implements RenderComponent {
	private final OptionMenu optionMenu;
	private final RenderObject renderObject;
	private final ConfigurableControllerPart configController;
	private final GameInput menuController;

	private final Table table;
	private final Map<ControlOption, OptionHandle> handleMap = new HashMap<>();

	private boolean disposed = false;

	OptionMenuRenderComponent(OptionMenu optionMenu, RenderObject renderObject,
							  ConfigurableControllerPart configController, GameInput menuController){
		this.optionMenu = optionMenu;
		this.renderObject = renderObject;
		this.configController = configController;
		this.menuController = menuController;

		table = new Table();
		table.setFillParent(true);
	}
	@Override
	public void render(float delta, Stage stage) {
		if(disposed) {
			System.out.println("Rendering when already disposed!");
		}
		if(menuController.getBackButton().isDown()){
			optionMenu.closeMenu();
		}

		stage.addActor(table);

		Set<OptionHandle> handledOptions = new HashSet<>();
		for(ControlOption option : configController.getControlOptions()){
			OptionHandle handle = handleMap.get(option);
			if(handle == null){
				handle = new OptionHandle(option, renderObject);
				handleMap.put(option, handle);
				handle.init(table);
			}
			handledOptions.add(handle);
			handle.update();
		}
		for(OptionHandle handle : handleMap.values()){
			if(!handledOptions.contains(handle)){
				handle.remove();
			}
		}

	}

	public ConfigurableControllerPart getConfigController() {
		return configController;
	}

	@Override
	public void dispose() {
		disposed = true;
		table.remove();
		table.reset();
	}
}
