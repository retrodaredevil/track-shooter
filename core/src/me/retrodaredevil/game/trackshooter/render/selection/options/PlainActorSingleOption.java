package me.retrodaredevil.game.trackshooter.render.selection.options;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;

import java.util.Set;

import me.retrodaredevil.controller.input.InputPart;
import me.retrodaredevil.controller.input.JoystickPart;
import me.retrodaredevil.game.trackshooter.render.selection.ContainerSingleOption;
import me.retrodaredevil.game.trackshooter.render.selection.SelectAction;
import me.retrodaredevil.game.trackshooter.util.ActorUtil;
import me.retrodaredevil.game.trackshooter.util.Size;

public class PlainActorSingleOption extends ContainerSingleOption {
	private final Actor actor;

	public PlainActorSingleOption(Actor actor, Size size){
		super(size);
		this.actor = actor;
	}

	@Override
	protected void onInit() {
		super.onInit();
		Cell<Actor> cell = container.add(actor);
		getSize().apply(cell);
	}

	@Override
	public void selectUpdate(float delta, JoystickPart selector, InputPart select, InputPart back, Set<? super SelectAction> requestedActions) {
		ActorUtil.fireInputEvents(actor, InputEvent.Type.enter);
		if(select.isJustPressed()){
			ActorUtil.fireInputEvents(actor, InputEvent.Type.touchDown, InputEvent.Type.touchUp);
		}
	}

	@Override
	public void deselect() {
		ActorUtil.fireInputEvents(actor, InputEvent.Type.exit);
	}
}
