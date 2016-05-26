package bb.net.packets.connecting;

import bb.net.interfaces.APacket;
import bb.net.packets.DataIn;
import bb.net.packets.DataOut;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by BB20101997 on 31.08.2014.
 */
public class HandshakePacket extends APacket {

	private final static Logger log;
	static{
		log = Logger.getLogger(APacket.class.getName());
		log.addHandler(new BBLogHandler(Constants.getBBLibLogFile()));
	}
	private String Version = "1.0";
	private boolean userClient;

	//for automatic instantiation on the receiving end
	public HandshakePacket(){
		log.finer("Constructor!");
	}

	public HandshakePacket(boolean user) {
		this();
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
		log.finer("Writing to Data");
		dataOut.writeUTF(Version);
		dataOut.writeBoolean(userClient);
	}

	@Override
	public void readFromData(DataIn dataIn) throws IOException {
		log.finer("Reading from Data");
		Version = dataIn.readUTF();
		userClient = dataIn.readBoolean();
	}
}
