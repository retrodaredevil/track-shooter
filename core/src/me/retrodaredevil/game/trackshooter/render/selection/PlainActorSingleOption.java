package me.retrodaredevil.game.trackshooter.render.selection;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.Collection;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;

public class PlainActorSingleOption extends ContainerSingleOption{
	private final Actor actor;
	private final float width, height;

	public PlainActorSingleOption(Actor actor, float width, float height){
		super();
		this.actor = actor;
		this.width = width;
		this.height = height;
	}

	@Override
	protected void onInit(Table container) {
		super.onInit(container);
		container.add(actor).width(this.width).height(this.height);
	}

	@Override
	public void selectUpdate(float delta, JoystickPart selector, InputPart select, InputPart back, Collection<SelectAction> requestedActions) {
		fireInputEvents(actor, InputEvent.Type.enter);
		if(select.isPressed()){
			fireInputEvents(actor, InputEvent.Type.touchDown);
			fireInputEvents(actor, InputEvent.Type.touchUp);
		}
//		if(select.isReleased()){
//		}
	}

	@Override
	public void deselect() {
		fireInputEvents(actor, InputEvent.Type.exit);

	}
}
