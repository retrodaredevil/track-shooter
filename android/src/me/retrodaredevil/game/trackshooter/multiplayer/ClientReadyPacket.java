package me.retrodaredevil.game.trackshooter.multiplayer;

import java.util.List;

/**
 * Sent by non-hosts to the host to make sure everyone is on the same page
 */
public class ClientReadyPacket implements Packet {
	private final String hostId;
	private final String participantId;
	private final List<String> allParticipants;

	public ClientReadyPacket(String hostId, String participantId, List<String> allParticipants) {
		this.hostId = hostId;
		this.participantId = participantId;
		this.allParticipants = allParticipants;
	}

	public String getHostId() {
		return hostId;
	}

	public String getParticipantId() {
		return participantId;
	}

	public List<String> getAllParticipants() {
		return allParticipants;
	}

	@Override
	public boolean isReliable() {
		return true;
	}
}
