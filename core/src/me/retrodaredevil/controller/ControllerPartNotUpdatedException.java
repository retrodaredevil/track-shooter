package me.retrodaredevil.controller;

/**
 * Represents an exception when a ControllerPart's update() wasn't called correctly or at all, OR
 * if ControllerParts that another ControllerPart relies upon wasn't updated when it should have
 * been.
 */
public class ControllerPartNotUpdatedException extends RuntimeException {
	public ControllerPartNotUpdatedException(){
		super();
	}
	public ControllerPartNotUpdatedException(String message){
		super(message);
	}
}
