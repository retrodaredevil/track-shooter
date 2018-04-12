package me.retrodaredevil.input;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 */
public class Joysticks implements Iterable<JoystickInput> {
	private Collection<JoystickInput> joysticks;

	public Joysticks(JoystickInput... joysticks){
		this.joysticks = Arrays.asList(joysticks);
	}

	@Override
	public Iterator<JoystickInput> iterator() {
		return joysticks.iterator();
	}
}
