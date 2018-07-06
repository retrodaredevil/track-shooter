package me.retrodaredevil.controller;

import java.util.ArrayList;
import java.util.List;

public class SimpleControllerManager implements ControllerManager {

	private ControlConfig config;
	private List<ControllerInput> controllers = new ArrayList<>();

	public SimpleControllerManager(){
		config = new ControlConfig();
	}

	@Override
	public void addController(ControllerInput controller){
		this.controllers.add(controller);
	}
	@Override
	public void update(){
		for(ControllerInput controller : controllers){
			if(controller.getParent() != null){
				throw new IllegalStateException("The controllers handled by SimpleControllerManager cannot have parents");
			}
			controller.update(config);
		}
	}
}
