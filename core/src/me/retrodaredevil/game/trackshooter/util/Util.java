package me.retrodaredevil.game.trackshooter.util;

import java.util.ArrayList;
import java.util.Collection;

public final class Util {
	private Util(){}

	public static <T extends V, V> Collection<T> getElementsOfClass(Collection<V> list, Class<T> clazz){
		Collection<T> r = null;
		for(V effect : list){
			if(clazz.isInstance(effect)){
				if(r == null){
					r = new ArrayList<>();
				}
				r.add((T) effect);
			}
		}

		return r;
	}
}
