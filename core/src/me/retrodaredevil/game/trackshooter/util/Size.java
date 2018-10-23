package me.retrodaredevil.game.trackshooter.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;

import java.util.Arrays;
import java.util.Collection;

public class Size {
	private final Float width, height;
	private Size(Float width, Float height){
		this.width = width;
		this.height = height;
	}
	private Size(float width, float height){
		this(Float.valueOf(width), Float.valueOf(height));
	}
	public static Size createSize(float width, float height){
		return new Size(width, height);
	}
	public static Size widthOnly(float width){
		return new Size(width, null);
	}
	public static Size heightOnly(float height){
		return new Size(null, height);
	}

	public Float getWidth(){
		return width;
	}
	public Float getHeight(){
		return height;
	}
	public boolean hasWidth(){
		return width != null;
	}
	public boolean hasHeight(){
		return height != null;
	}
	public float requireWidth(){
		return width;
	}
	public float requireHeight(){
		return height;
	}

	public float ofWidth(float percent){
		return requireWidth() * percent;
	}
	public float ofHeight(float percent){
		return requireHeight() * percent;
	}
	public void apply(Cell<?> cell){ applyOnly(cell, Type.WIDTH, Type.HEIGHT); }
	public void applyOnly(Cell<?> cell, Type... types){
		Collection<Type> typeCollection = Arrays.asList(types);

		if(hasWidth() && typeCollection.contains(Type.WIDTH)){
			cell.width(requireWidth());
		}
		if(hasHeight() && typeCollection.contains(Type.HEIGHT)){
			cell.height(requireHeight());
		}
	}
	public void apply(Actor actor) { apply(actor, Type.WIDTH, Type.HEIGHT); }
	public void apply(Actor actor, Type... types){
		Collection<Type> typeCollection = Arrays.asList(types);

		if(hasWidth() && typeCollection.contains(Type.WIDTH)){
			actor.setWidth(requireWidth());
		}
		if(hasHeight() && typeCollection.contains(Type.HEIGHT)){
			actor.setHeight(requireHeight());
		}
	}

	public void applyOf(Cell<?> cell, Type type, float percent){
		switch(type){
			case WIDTH:
				cell.width(requireWidth() * percent);
				break;
			case HEIGHT:
				cell.height(requireHeight() * percent);
				break;
			default:
				throw new UnsupportedOperationException();
		}
	}
	public void applyOf(Actor actor, Type type, float percent){
		switch(type){
			case WIDTH:
				actor.setWidth(requireWidth() * percent);
				break;
			case HEIGHT:
				actor.setHeight(requireHeight() * percent);
				break;
			default:
				throw new UnsupportedOperationException();
		}
	}
	public void applyOfWidth(Cell<?> cell, float percent){
		applyOf(cell, Type.WIDTH, percent);
	}
	public void applyOfHeight(Cell<?> cell, float percent){
		applyOf(cell, Type.HEIGHT, percent);
	}

	public enum Type {
		WIDTH, HEIGHT
	}

}
