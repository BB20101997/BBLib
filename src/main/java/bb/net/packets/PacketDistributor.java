package bb.net.packets;

import bb.net.interfaces.*;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by BB20101997 on 31.08.2014. All incoming Packets land here and get distributed to the appropriate Handler
 */
@SuppressWarnings("deprecation")
@Deprecated
public class PacketDistributor implements IPacketDistributor {

	private final IConnectionManager IMH;
	private static final Logger log;
	static {
		log = Logger.getLogger("bb.net.packets.PacketDistributor");
		log.addHandler(new BBLogHandler(Constants.getBBLibLogFile()));
	}

	public PacketDistributor(IConnectionManager imh) {
		IMH = imh;
	}

	private final List<IPacketHandler> PHList = new ArrayList<>();

	@Override
	public int registerPacketHandler(IPacketHandler iph) {
		log.finer("Registering a PacketHandler");
		synchronized(PHList) {
			if(!PHList.contains(iph)) {
				PHList.add(iph);
			}
			return PHList.indexOf(iph);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void distributePacket(int id,final byte[] data, IIOHandler sender) {

		APacket p = IMH.getPacketRegistrie().getNewPacketOfID(id);

		log.fine("Distributing Packet\nID:" + id + "\nClass:" + p.getClass());
		log.fine("Incoming Packet with " + data.length + " bytes!" + System.lineSeparator() + "Packet is of class:" + p.getClass());
		try {
			p.readFromData(DataIn.newInstance(data.clone()));
		} catch(IOException e) {
			e.printStackTrace();
		}
		log.finer("Packet:" + p);
		main:
		for(IPacketHandler iph : PHList) {
			for(Class c : iph.getAssociatedPackets()) {
				if(c.equals(IMH.getPacketRegistrie().getPacketClassByID(id))) {
					iph.HandlePacket(p.copy(), sender);
					continue main;
				}
			}
		}


	}


}
