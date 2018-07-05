package me.retrodaredevil.controller;

/**
 * Thrown when you try to access a value of a device/controller that isn't connected or is unavailable.
 * <br/><br/>
 * NOTE: This isn't required to be thrown when something isn't connected. But it may be thrown in
 * worse case scenarios
 */
public class NotConnectedException extends RuntimeException {
	public NotConnectedException(String message){ super(message); }
}
