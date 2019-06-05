package me.retrodaredevil.game.trackshooter.account.multiplayer;

import me.retrodaredevil.game.trackshooter.account.Show;

public interface Multiplayer {
	Show getShowRoomConfig();
	Show getShowInbox();

	ConnectionState getConnectionState();

	enum ConnectionState {
		CONNECTED, JOINING, DISCONNECTED, LEAVING
	}

	class Defaults {
		public static final Multiplayer NOT_SUPPORTED = new Multiplayer() {
			@Override
			public Show getShowRoomConfig() {
				return Show.Defaults.NOT_ABLE;
			}

			@Override
			public Show getShowInbox() {
				return Show.Defaults.NOT_ABLE;
			}

			@Override
			public ConnectionState getConnectionState() {
				return ConnectionState.DISCONNECTED;
			}
		};
	}
}
