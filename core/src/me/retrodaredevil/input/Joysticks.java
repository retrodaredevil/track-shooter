package me.retrodaredevil.input;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 */
public class Joysticks implements Iterable<JoystickPart> {
	private Collection<JoystickPart> joysticks;

	public Joysticks(JoystickPart... joysticks){
		this.joysticks = Arrays.asList(joysticks);
	}

	@Override
	public Iterator<JoystickPart> iterator() {
		return joysticks.iterator();
	}
}
