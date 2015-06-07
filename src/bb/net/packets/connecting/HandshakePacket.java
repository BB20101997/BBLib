package bb.net.packets.connecting;

import bb.net.enums.NetworkState;
import bb.net.interfaces.APacket;
import bb.net.packets.DataIn;
import bb.net.packets.DataOut;

import java.io.IOException;

/**
 * Created by BB20101997 on 31.08.2014.
 */
public class HandshakePacket extends APacket {

	private String Version = "1.0";
	private boolean userClient;

	public HandshakePacket() {
		minNetworkState = NetworkState.UNKNOWN;
	}

	public HandshakePacket(boolean user) {
		minNetworkState = NetworkState.UNKNOWN;
		userClient = user;
	}

	public String getVersion() {
		return Version;
	}

	public boolean isUserClient() {
		return userClient;
	}

	@Override
	public void writeToData(DataOut dataOut) throws IOException {
		dataOut.writeUTF(Version);
		dataOut.writeBoolean(userClient);
	}

	@Override
	public void readFromData(DataIn dataIn) throws IOException {
		Version = dataIn.readUTF();
		userClient = dataIn.readBoolean();
	}
}
