package me.retrodaredevil.game.trackshooter.render.parts.options;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.HashMap;
import java.util.Map;

import me.retrodaredevil.controller.options.ConfigurableControllerPart;
import me.retrodaredevil.controller.options.ControlOption;
import me.retrodaredevil.game.trackshooter.RenderObject;
import me.retrodaredevil.game.trackshooter.render.components.RenderComponent;

public class OptionMenuRenderComponent implements RenderComponent {
	private final RenderObject renderObject;
	private final ConfigurableControllerPart configController;

	private final Table table;
	private final Map<ControlOption, OptionHandle> handleMap = new HashMap<>();

	OptionMenuRenderComponent(RenderObject renderObject, ConfigurableControllerPart configController){
		this.renderObject = renderObject;
		this.configController = configController;

		table = new Table();
		table.setFillParent(true);
	}
	@Override
	public void render(float delta, Stage stage) {
//		System.out.println("rendering options menu");
		stage.addActor(table);

		for(ControlOption option : configController.getControlOptions()){
			OptionHandle handle = handleMap.get(option);
			if(handle == null){
				handle = new OptionHandle(option, renderObject);
				handleMap.put(option, handle);
				handle.init(table);
			}
			handle.update();
		}

	}

	public ConfigurableControllerPart getConfigController() {
		return configController;
	}

	@Override
	public void dispose() {
		table.remove();
	}
}
