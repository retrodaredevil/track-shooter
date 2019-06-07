package me.retrodaredevil.game.trackshooter.multiplayer;

import java.io.*;

final class PacketHelper {
	private PacketHelper(){ throw new UnsupportedOperationException(); }

	static Packet parseFromBytes(byte[] bytes){
		try (
				ByteArrayInputStream byteInput = new ByteArrayInputStream(bytes);
				ObjectInput objectInput = new ObjectInputStream(byteInput)
		) {
			Object object = objectInput.readObject();
			return (Packet) object;
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	static byte[] convertToBytes(Packet packet){
		try (
				ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
				ObjectOutput output = new ObjectOutputStream(byteOutput)
		) {
			output.writeObject(packet);
			return byteOutput.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
