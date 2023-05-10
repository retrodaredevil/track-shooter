package me.retrodaredevil.game.trackshooter.input.implementations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

import java.util.Objects;

import me.retrodaredevil.controller.input.implementations.AutoCachingInputPart;
import me.retrodaredevil.controller.input.AxisType;
import me.retrodaredevil.controller.options.OptionValue;
import me.retrodaredevil.game.trackshooter.util.Util;

public class GdxMouseAxis extends AutoCachingInputPart {
	public static final int MAX_POINTERS = 20;

	private static final int TIME_FOR_MOUSE_CATCH_TIMEOUT = 500; // ms
	private final boolean testForAllPointers, yAxis, needsDrag, needsTouchScreenForConnection, useDeltaMethods;
//	private final float multiplier;
//	private final OptionValue multiplierOption;
//	private final OptionValue isInvertedBooleanOption; // may be null
	private final MultiplierGetter multiplierGetter;
	/** May be null*/
	private final ScreenArea screenArea;

	private int lastPosition = 0; // only changed if useDeltaMethods == false and testForAllPointers == false

	// only used if testForAllPointers == true and useDeltaMethods == false
	private final int[] lastPositions;
	private final boolean[] downLastFrame;

	private long lastPositionRequest = 0;
	private boolean isCatched = false;

	/**
	 *
	 * @param testForAllPointers Should this test for multiple pointers (Useful on mobile devies)
	 * @param yAxis false for x, true for y
	 * @param needsDrag Does the left mouse button/is touched need to be down for this to return a non zero value
//	 * @param multiplier The multiplier for this axis which will be multiplied by multiplierOption
//	 * @param multiplierOption The multiplier option for the output of this axis. (Mutated outside of this class)
	 * @param needsTouchScreenForConnection set to true if the touchscreen Peripheral needs to be available for isConnected() to return true
	 * @param screenArea the area of the screen for this to respond to
	 * @param useDeltaMethods if true, will use Gdx.input.getDeltaX/Y. If false, it will store the last position
	 *                        and use that to determine how much the mouse has moved. For a mouse on desktop,
	 *                        setting this to false is a better option otherwise setting to true may be more optimised.
	 */
	public GdxMouseAxis(boolean testForAllPointers, boolean yAxis, boolean needsDrag, MultiplierGetter multiplierGetter,
						boolean needsTouchScreenForConnection, ScreenArea screenArea, boolean useDeltaMethods) {
		super(new AxisType(true, true, true, false), false);
		this.testForAllPointers = testForAllPointers;
		this.yAxis = yAxis;
		this.needsDrag = needsDrag;
//		this.multiplier = multiplier;
//		this.multiplierOption = multiplierOption;
//		this.isInvertedBooleanOption = isInvertedBooleanOption;
		this.multiplierGetter = multiplierGetter;
		this.needsTouchScreenForConnection = needsTouchScreenForConnection;
		this.screenArea = Objects.requireNonNull(screenArea);
		this.useDeltaMethods = useDeltaMethods;

		if(testForAllPointers && !useDeltaMethods){
			lastPositions = new int[MAX_POINTERS];
			downLastFrame = new boolean[MAX_POINTERS];
		} else {
			lastPositions = null;
			downLastFrame = null;
		}
	}

	/**
	 * Creates a GdxMouseAxis used for just a mouse
	 */
	public GdxMouseAxis(boolean yAxis, MultiplierGetter multiplierGetter){
		this(false, yAxis, false, multiplierGetter, false, (x, y) -> true, false);
	}

	/**
	 * Creates a GdxMouseAxis used for dragging in a certain area of the screen
	 */
	public GdxMouseAxis(boolean yAxis, MultiplierGetter multiplierGetter, ScreenArea screenArea){
		this(true, yAxis, true, multiplierGetter,
				true, screenArea, true);
	}
	private float getMultiplier(){
//		float inverted = 1;
//		if(isInvertedBooleanOption != null){
//			inverted = isInvertedBooleanOption.getBooleanOptionValue() ? -1 : 1;
//		}
//		return multiplier * (float) multiplierOption.getOptionValue() * inverted;
		return multiplierGetter.getMultiplier();
	}

	private void setCatched(boolean b){
		if(!b && !isCatched){ // if we want it to be catched, just make sure
			return;
		}
		isCatched = b;
		Gdx.input.setCursorCatched(b);

	}

	@Override
	protected void onUpdate() {
		super.onUpdate();
		if(lastPositionRequest + TIME_FOR_MOUSE_CATCH_TIMEOUT <= System.currentTimeMillis()){
			setCatched(false);
		}
	}

	@Override
	public double getPosition() {
		// TODO find a better way to enable cursor catching instead of this tightly coupled way
		lastPositionRequest = System.currentTimeMillis();
		if(!needsDrag) {
			setCatched(true);
		}
		return super.getPosition();
	}

	@Override
	protected double calculatePosition() {
		if(!testForAllPointers){
			if(needsDrag && !Gdx.input.isTouched()){
				return 0;
			}
			if(!useDeltaMethods){
				int position = yAxis ? -Gdx.input.getY() : Gdx.input.getX();
				int r = position - lastPosition;
				lastPosition = position;
				return r * getMultiplier();
			}
			return (yAxis ? -Gdx.input.getDeltaY() : Gdx.input.getDeltaX()) * getMultiplier();
		}
		assert useDeltaMethods || (downLastFrame != null && lastPositions != null) : "We didn't initialize downLastFrame or lastPositions correctly.";
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();

		// now we are testing for multiple pointers
		int highest = 0;
		for(int i = 0; i < MAX_POINTERS; i++){
			try {
				boolean isTouched = Gdx.input.isTouched(i);
				boolean justTouched = false; // not accurate unless useDeltaMethods == false
				if(!useDeltaMethods) {
					justTouched = isTouched && !downLastFrame[i];
					downLastFrame[i] = isTouched;
				}
				if ((!needsDrag || (isTouched && !justTouched))){
					float x = Gdx.input.getX(i) / width;
					float y = 1 - (Gdx.input.getY(i) / height);
					if(screenArea.containsPoint(x, y)) {
						int r;
						if (useDeltaMethods) {
							r = yAxis ? -Gdx.input.getDeltaY(i) : Gdx.input.getDeltaX(i);
						} else {
							int lastPosition = lastPositions[i];
							int position = yAxis ? -Gdx.input.getY(i) : Gdx.input.getX(i);
							r = position - lastPosition;
						}
						if (Math.abs(r) > Math.abs(highest)) {
							highest = r;
						}
					}
				}
				if(!useDeltaMethods){
//					System.out.println("updating lastPosition " + System.currentTimeMillis());
					lastPositions[i] = yAxis ? -Gdx.input.getY(i) : Gdx.input.getX(i);
				}
			} catch(IndexOutOfBoundsException ex){
				ex.printStackTrace();
				System.err.println("index: " + i + " is too big to be a pointer.");
			}
		}
		return highest * getMultiplier();
	}

	@Override
	public boolean isConnected() {
		return !needsTouchScreenForConnection || Gdx.input.isPeripheralAvailable(Input.Peripheral.MultitouchScreen);
	}
	public interface MultiplierGetter {
		float getMultiplier();
	}
}
