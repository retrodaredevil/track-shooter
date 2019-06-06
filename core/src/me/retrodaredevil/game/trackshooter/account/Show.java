package me.retrodaredevil.game.trackshooter.account;

public interface Show {
	void show();
	boolean isCurrentlyAbleToShow();
	boolean isEverAbleToShow();

	class Defaults {
		public static final Show NOT_ABLE = new Show() {
			@Override
			public void show() {
				throw new UnsupportedOperationException();
			}

			@Override
			public boolean isCurrentlyAbleToShow() {
				return false;
			}

			@Override
			public boolean isEverAbleToShow() {
				return false;
			}
		};
	}
}
