package me.retrodaredevil.game.trackshooter.input.implementations;

import com.badlogic.gdx.math.Rectangle;

public final class ScreenAreas {
	private ScreenAreas(){ throw new UnsupportedOperationException(); }

	public static ScreenArea inRectangle(Rectangle rect){
		return new RectangleArea(rect);
	}
	public static ScreenArea leftOfX(float xValue){
		return (x, y) -> x < xValue;
	}
	public static ScreenArea rightOfX(float xValue){
		return (x, y) -> x > xValue;
	}
	public static ScreenArea exactX(float xValue){
		return (x, y) -> x == xValue;
	}

	public static ScreenArea leftOfY(float yValue){
		return (x, y) -> y < yValue;
	}
	public static ScreenArea rightOfY(float yValue){
		return (x, y) -> y > yValue;
	}
	public static ScreenArea exactY(float yValue){
		return (x, y) -> y == yValue;
	}

	public static ScreenArea allOf(ScreenArea... areas){
		return (x, y) -> {
			for(ScreenArea area : areas){
				if(!area.containsPoint(x, y)){
					return false;
				}
			}
			return true;
		};
	}
	public static ScreenArea anyOf(ScreenArea... areas){
		return (x, y) -> {
			for(ScreenArea area : areas){
				if(area.containsPoint(x, y)){
					return true;
				}
			}
			return false;
		};
	}

	private static class RectangleArea implements ScreenArea {
		private final Rectangle rect;

		private RectangleArea(Rectangle rect) {
			this.rect = rect;
		}

		@Override
		public boolean containsPoint(float x, float y) {
			return rect.contains(x, y);
		}
	}
}
