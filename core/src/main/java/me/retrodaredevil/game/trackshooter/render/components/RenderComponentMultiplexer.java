package me.retrodaredevil.game.trackshooter.render.components;

import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.Collection;
import java.util.LinkedHashSet;

public class RenderComponentMultiplexer implements RenderComponent {

	private final Collection<RenderComponent> components = new LinkedHashSet<>();


	public boolean addComponent(RenderComponent toAdd){
		return components.add(toAdd);
	}
	public boolean removeComponent(RenderComponent toRemove){
		return components.remove(toRemove);
	}
	public void clear(){
		components.clear();
	}

	@Override
	public void render(float delta, Stage stage) {
		for(RenderComponent renderComponent : components){
			renderComponent.render(delta, stage);
		}
	}

	@Override
	public void dispose() {
		for(RenderComponent renderComponent : components){
			renderComponent.dispose();
		}
	}
}
