package bb.net.interfaces;

import bb.net.packets.DataIn;
import bb.net.packets.DataOut;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by BB20101997 on 30.08.2014.
 */
public abstract class APacket {

	private static final Logger log;
	static {
		log = Logger.getLogger(APacket.class.getName());
		log.addHandler(new BBLogHandler(Constants.getBBLibLogFile()));
	}

	public APacket copy() {
		log.finer("Copying a Packet!");
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
			log.fine("Missing default constructor in Packet class:"+getClass().getName());
			throw new RuntimeException("Missing default constructor in Packet class : " + getClass().getName());
		}
	}

	public abstract void writeToData(DataOut dataOut) throws IOException;

	public abstract void readFromData(DataIn dataIn) throws IOException;

}
