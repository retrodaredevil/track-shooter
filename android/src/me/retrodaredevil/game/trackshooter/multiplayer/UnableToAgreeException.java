package me.retrodaredevil.game.trackshooter.multiplayer;

class UnableToAgreeException extends Exception {
	UnableToAgreeException(){
		super();
	}
	UnableToAgreeException(String message){
		super(message);
	}
	UnableToAgreeException(String message, Throwable cause){
		super(message, cause);
	}
	UnableToAgreeException(Throwable cause){
		super(cause);
	}
}
