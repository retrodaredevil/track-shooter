package me.retrodaredevil.game.trackshooter.util;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;

/**
 * @deprecated should not be called in the main code but may be used for debug
 */
@Deprecated
public class GdxControllerTester extends ControllerAdapter {
	private GdxControllerTester(){
		Controllers.addListener(this);
		System.out.println(getClass().getSimpleName() + " is now listening for controller events! There are " + (Controllers.getListeners().size - 1) + " other listeners as of right now.");
		System.out.println("There are " + Controllers.getControllers().size + " controllers connected as of right now.");
	}
	public static void initialize(){
		new GdxControllerTester();
	}

	@Override
	public void connected(Controller controller) {
		System.out.println("controller: " + controller.getName() + " got connected!");
	}

	@Override
	public void disconnected(Controller controller) {
		System.out.println("controller: " + controller.getName() + " got disconnected!");
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonIndex) {

		System.out.println(buttonIndex + " was pressed on " + controller.getName());
		return false;
	}
}
