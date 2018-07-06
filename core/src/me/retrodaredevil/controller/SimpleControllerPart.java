package me.retrodaredevil.controller;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class SimpleControllerPart implements ControllerPart{

	private ControllerPart parent = null;
	private final Set<ControllerPart> children = new HashSet<>();

	protected ControlConfig config;


	@Override
	public ControllerPart getParent() {
		return parent;
	}

	@Override
	public void setParent(final ControllerPart parent) {
		if(parent == this){
			throw new IllegalArgumentException("Cannot set parent to this");
		}
		if(this.parent == parent){ // don't do anything if it's the same
			return;
		}
		if(this.parent != null) {
			this.parent.removeChild(this);
		}
		this.parent = parent;
		if(parent != null){
			parent.addChild(this);
		}
	}

	@Override
	public Collection<ControllerPart> getChildren() {
		return children;
	}

	@Override
	public void addChild(ControllerPart part) {
		children.add(part);
		part.setParent(this);
	}

	@Override
	public boolean removeChild(ControllerPart part) {
		if(part.getParent() != this){
			return false;
		}
		if(part.getParent() == this) {
			part.setParent(null);
		}
		boolean wasRemoved = children.remove(part);
		if(!wasRemoved){
			throw new AssertionError("part: " + part + " had us a parent, but we didn't know because they weren't in our children list!");
		}
		return true;
	}

	@Override
	public void update(ControlConfig config) {
		this.config = config;
		onUpdate();
		onSecondUpdate();
		updateChildren();
		onAfterChildrenUpdate();
		onLateUpdate();
	}
	protected void updateChildren(){
		for(ControllerPart part : getChildren()){
			part.update(config);
		}
	}

	/**
	 * Should be overridden to update data that doesn't rely on another ControllerPart
	 */
	protected void onUpdate(){}

	/**
	 * Should be overridden to update data that relies on a subclass (abstract) implementation that is updated
	 * in onUpdate()
	 */
	protected void onSecondUpdate(){}

	/**
	 * Should be overridden to update data that relies on children.
	 */
	protected void onAfterChildrenUpdate(){}

	/**
	 * Should be overridden to act on updated data. This is called after all children are fully updated
	 */
	protected void onLateUpdate(){}

	protected boolean areAllChildrenConnected(){
		for(ControllerPart part : getChildren()){
			if(!part.isConnected()){
				return false;
			}
		}
		return true;
	}
	protected boolean areAnyChildrenConnected(){
		for(ControllerPart part : getChildren()){
			if(part.isConnected()){
				return true;
			}
		}
		return false;
	}
	/**
	 *
	 * @param parts The parts to change each element's parent to this
	 * @param changeParent true if you want to allow the parent of parts that already have a parent to be changed
	 * @param canAlreadyHaveParents Should be true if you expect one or more elements to already have a parent. If false,
	 *                                 it will throw an AssertionError if a part already has a parent. If false,
	 *                                 changeParent's value will do nothing.
	 * @throws AssertionError if canAlreadyHaveParents == false and one of the parts in the
	 *                        parts Iterable has a parent that isn't null, this will be thrown
	 * @throws IllegalArgumentException only thrown when changeParent == true and canAlreadyHaveParents == false
	 */
	public void addChildren(Iterable<? extends ControllerPart> parts,
	                        boolean changeParent, boolean canAlreadyHaveParents){
		if(changeParent && !canAlreadyHaveParents){
			throw new IllegalArgumentException("If changeParent == true, canAlreadyHaveParents cannot be false");
		}
		for(ControllerPart part : parts){
			boolean hasParent = part.getParent() != null;
			if(!canAlreadyHaveParents && hasParent){
				throw new AssertionError("A part already has a parent");
			}
			if(changeParent || !hasParent) {
				part.setParent(this);
//				this.addChild(part);
			}
		}
	}

}
