package bb.net.packets.connecting;

import bb.net.interfaces.APacket;
import bb.net.packets.DataIn;
import bb.net.packets.DataOut;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by BB20101997 on 30.08.2014.
 */
public class PacketSyncPacket extends APacket {

	private List<Class<? extends APacket>> classes;

	@Deprecated
	public PacketSyncPacket(Class<? extends APacket>[] cs) {
		classes = new ArrayList<>();
		Collections.addAll(classes, cs);
	}

	public PacketSyncPacket(List<Class<? extends APacket>> clazzList){
		classes = clazzList;
	}

	@SuppressWarnings("unused")
	public PacketSyncPacket() {
		//necessary for automatic instantiation
	}

	@Deprecated
	public Class<? extends APacket>[] getPackageClasses() {
		//noinspection unchecked
		return (Class<? extends APacket>[])classes.toArray();
	}

	public List<Class<? extends APacket>> getPackageClassList(){
		return new ArrayList<>(classes);
	}


	@Override
	public void writeToData(DataOut dataOut) throws IOException {
		dataOut.writeInt(classes.size());
		for(Class c : classes) {
			dataOut.writeUTF(c.getName());
		}
	}

	@Override
	public void readFromData(DataIn dataIn) throws IOException {
		int i = dataIn.readInt();

		classes = new ArrayList<>();
		while(i > 0) {
			try {
				@SuppressWarnings("unchecked")
				Class<? extends APacket> clazz = (Class<? extends APacket>) Class.forName(dataIn.readUTF());
				classes.add(clazz);
			} catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
			i--;
		}
	}
}
