package me.retrodaredevil.game.trackshooter.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class InputCanceller extends InputAdapter{
	private final Collection<Integer> cancelKeys;

	public InputCanceller(Collection<Integer> cancelKeys){
		if(cancelKeys instanceof Set){
			this.cancelKeys = cancelKeys;
		} else {
			this.cancelKeys = new HashSet<>(cancelKeys);
		}
	}

	public static InputCanceller createVolumeButtonCancellingListener(){
		return new InputCanceller(Arrays.asList(Input.Keys.VOLUME_DOWN, Input.Keys.VOLUME_UP));
	}

	@Override
	public boolean keyDown(int keycode) {
		if(cancelKeys.contains(keycode)){
			System.out.println(Input.Keys.toString(keycode) + " was pressed!");
			return true;
		}
		return super.keyDown(keycode);
	}
}
