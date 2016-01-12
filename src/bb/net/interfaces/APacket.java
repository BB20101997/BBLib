package bb.net.interfaces;

import bb.net.packets.DataIn;
import bb.net.packets.DataOut;

import java.io.IOException;

/**
 * Created by BB20101997 on 30.08.2014.
 */
public abstract class APacket {


	public APacket copy() {
		APacket p = null;
		try {
			p = this.getClass().newInstance();
			DataOut dataOut = DataOut.newInstance();
			writeToData(dataOut);
			byte[] b = dataOut.getBytes();
			p.readFromData(DataIn.newInstance(b));
		} catch(InstantiationException | IllegalAccessException | IOException e) {
			e.printStackTrace();
		}
		return p;
	}

	protected APacket() {
		//TODO: Add a history for the server the packet has been past over for preventing loop's
		try {
			getClass().getConstructor();
		} catch(NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("Missing default constructor in Packet class : " + getClass().getName());
		}
	}

	public abstract void writeToData(DataOut dataOut) throws IOException;

	public abstract void readFromData(DataIn dataIn) throws IOException;

}
