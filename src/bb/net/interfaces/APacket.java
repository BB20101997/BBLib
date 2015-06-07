package bb.net.interfaces;

import bb.net.enums.NetworkState;
import bb.net.packets.DataIn;
import bb.net.packets.DataOut;

import java.io.IOException;

/**
 * Created by BB20101997 on 30.08.2014.
 */
public abstract class APacket {


	public APacket copy() {
		try {
			APacket p = this.getClass().newInstance();
			DataOut dataOut = DataOut.newInstance();
			writeToData(dataOut);
			p.readFromData(DataIn.newInstance(dataOut.getBytes()));
			return p;
		} catch(InstantiationException | IllegalAccessException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("CanBeFinal")
	protected NetworkState minNetworkState = NetworkState.POST_HANDSHAKE;

	protected APacket() {
		//TODO: Add a history for the server the packet has been past over for preventing loop's
		try {
			getClass().getConstructor();
		} catch(NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("Missing default constructor in Packet class : " + getClass().getName());
		}
	}


	public final NetworkState allowedFrom() {
		return minNetworkState;
	}

	public abstract void writeToData(DataOut dataOut) throws IOException;

	public abstract void readFromData(DataIn dataIn) throws IOException;

}
