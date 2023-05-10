package me.retrodaredevil.game.trackshooter.account.multiplayer;

import me.retrodaredevil.game.trackshooter.account.Show;
import me.retrodaredevil.game.trackshooter.multiplayer.Multiplayer;

public interface AccountMultiplayer {
	Show getShowRoomConfig();
	Show getShowInbox();

	ConnectionState getConnectionState();

	/**
	 *
	 * @throws IllegalStateException if {@link #getConnectionState()} != {@link ConnectionState#CONNECTED}
	 * @return The {@link Multiplayer}
	 */
	Multiplayer getMultiplayer();

	enum ConnectionState {
		CONNECTED, JOINING, DISCONNECTED, LEAVING
	}

	class Defaults {
		public static final AccountMultiplayer NOT_SUPPORTED = new AccountMultiplayer() {
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

			@Override
			public Multiplayer getMultiplayer() {
				throw new IllegalStateException();
			}
		};
	}
}
