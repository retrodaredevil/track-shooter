package me.retrodaredevil.controller;

import java.util.ArrayList;
import java.util.List;

public class ControllerManager {

	private ControlConfig config;
	private List<ControllerInput> controllers = new ArrayList<>();

	public ControllerManager(){
		config = new ControlConfig();
	}
	public void addController(ControllerInput controller){
		this.controllers.add(controller);
	}
	public void update(){
		for(ControllerInput controller : controllers){
			assert controller.getParent() == null;
			controller.update(this.config);
		}
	}
}
