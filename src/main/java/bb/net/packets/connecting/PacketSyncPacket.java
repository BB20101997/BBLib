package bb.net.packets.connecting;

import bb.net.interfaces.APacket;
import bb.net.packets.DataIn;
import bb.net.packets.DataOut;

import java.io.IOException;

/**
 * Created by BB20101997 on 30.08.2014.
 */
public class PacketSyncPacket extends APacket {

	private Class<? extends APacket>[] classes;

	public PacketSyncPacket(Class<? extends APacket>[] cs) {
		classes = cs;
	}

	public PacketSyncPacket() {
	}

	public Class<? extends APacket>[] getPackageClasses() {
		return classes;
	}


	@Override
	public void writeToData(DataOut dataOut) throws IOException {
		dataOut.writeInt(classes.length);
		for(Class c : classes) {
			dataOut.writeUTF(c.getName());
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void readFromData(DataIn dataIn) throws IOException {
		int i = dataIn.readInt();

		classes = new Class[i];
		while(i > 0) {
			try {
				Class c = Class.forName(dataIn.readUTF());
				classes[classes.length - i] = c;
			} catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
			i--;
		}
	}
}
