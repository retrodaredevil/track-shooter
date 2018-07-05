package me.retrodaredevil.game.trackshooter.util;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

import java.util.ArrayList;
import java.util.Collection;

public final class Util {
	private Util(){}

	/**
	 * @param list The original list
	 * @param clazz The type of all the elements that will be in the returned list that will be gotten from list
	 * @param <T> The type of the original list that usually is a superclass of T
	 * @param <V> The type of elements the returned list will have
	 * @return A list of elements with the type of V. Each element from list where element instanceof V will be in this list.
	 */
	public static <T, V> Collection<V> getElementsOfClass(Collection<T> list, Class<V> clazz){
		Collection<V> r = null;
		for(T effect : list){
			if(clazz.isInstance(effect)){
				if(r == null){
					r = new ArrayList<>();
				}
				r.add((V) effect);
			}
		}

		return r;
	}

	public static boolean isControllerConnected(Controller controller){
		return Controllers.getControllers().contains(controller, true);
	}
}
