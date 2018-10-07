package me.retrodaredevil.game.trackshooter;

import com.badlogic.gdx.Screen;

public interface UsableScreen extends Screen {

	boolean isScreenDone();
	UsableScreen createNextScreen();

}
