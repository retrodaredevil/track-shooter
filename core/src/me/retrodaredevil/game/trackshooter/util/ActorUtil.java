package me.retrodaredevil.game.trackshooter.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Pools;

public final class ActorUtil {
	private ActorUtil(){ throw new UnsupportedOperationException(); }


	public static boolean fireInputEvents(Actor actor, InputEvent.Type... types){
		boolean eitherHandled = false;
		for(InputEvent.Type type : types){
			InputEvent event = Pools.obtain(InputEvent.class);
			event.setPointer(-1); // fix for hard coded ClickListener mouse pointer requirement
			event.setType(type);
			event.setButton(Input.Buttons.LEFT);

			actor.fire(event);
			if(!eitherHandled){
				eitherHandled = event.isHandled();
			}
			Pools.free(event);
		}
		return eitherHandled;
	}
}
