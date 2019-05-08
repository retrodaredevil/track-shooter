package me.retrodaredevil.game.trackshooter.account.multiplayer;

import me.retrodaredevil.game.trackshooter.account.Show;

public interface Multiplayer {
	Show getShowInvitePlayers();
	State getState();

	enum State {
		CONNECTED, JOINING, DISCONNECTED, LEAVING
	}

	class Defaults {
		public static final Multiplayer NOT_SUPPORTED = new Multiplayer() {
			@Override
			public Show getShowInvitePlayers() {
				return Show.Defaults.NOT_ABLE;
			}

			@Override
			public State getState() {
				return State.DISCONNECTED;
			}
		};
	}
}
