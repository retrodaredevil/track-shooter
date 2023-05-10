package me.retrodaredevil.game.trackshooter.util;

public final class StringUtil {
	private StringUtil(){}

	public static String fillSpace(String toFill, int requiredSpace, boolean inFront, char fillChar){
		final int fillAmount = requiredSpace - toFill.length();
		if(fillAmount > 0){
			String fillString = new String(new char[fillAmount]).replace("\0", fillChar + "");
			if(inFront){
				return fillString + toFill;
			}
			return toFill + fillString;
		}
		return toFill;
	}
}
