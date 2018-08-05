package me.retrodaredevil.controller;

import java.util.ArrayList;
import java.util.List;

public class DefaultControllerManager implements ControllerManager {

	private ControlConfig config;
	private List<ControllerInput> controllers = new ArrayList<>();

	public DefaultControllerManager(){
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
				throw new IllegalStateException("The controllers handled by DefaultControllerManager cannot have parents");
			}
			try {
				controller.update(config);
			} catch(Exception ex){
				System.err.println("Error while updating controller: " + controller + " which is of class: " + controller.getClass().getSimpleName());
				throw ex;
			}
		}
	}
}