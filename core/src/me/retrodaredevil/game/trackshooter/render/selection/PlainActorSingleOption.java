package me.retrodaredevil.game.trackshooter.render.selection;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.Collection;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.game.trackshooter.util.ActorUtil;
import me.retrodaredevil.game.trackshooter.util.Size;

public class PlainActorSingleOption extends ContainerSingleOption{
	private final Actor actor;

	public PlainActorSingleOption(Actor actor, Size size){
		super(size);
		this.actor = actor;
	}

	@Override
	protected void onInit(Table container) {
		super.onInit(container);
		Cell<Actor> cell = container.add(actor);
		getSize().apply(cell);
	}

	@Override
	public void selectUpdate(float delta, JoystickPart selector, InputPart select, InputPart back, Collection<SelectAction> requestedActions) {
		ActorUtil.fireInputEvents(actor, InputEvent.Type.enter);
		if(select.isPressed()){
			ActorUtil.fireInputEvents(actor, InputEvent.Type.touchDown, InputEvent.Type.touchUp);
		}
	}

	@Override
	public void deselect() {
		ActorUtil.fireInputEvents(actor, InputEvent.Type.exit);
	}
}
