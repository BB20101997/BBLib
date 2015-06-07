package bb.net.packets;

import bb.net.interfaces.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BB20101997 on 31.08.2014. All incoming Packets land here and get distributed to the appropriate Handler
 */
public class PacketDistributor implements IPacketDistributor {

	private final IConnectionManager IMH;

	public PacketDistributor(IConnectionManager imh) {
		IMH = imh;
	}

	private final List<IPacketHandler> PHList = new ArrayList<>();

	@Override
	public int registerPacketHandler(IPacketHandler iph) {
		synchronized(PHList) {
			if(!PHList.contains(iph)) {
				PHList.add(iph);
			}
			return PHList.indexOf(iph);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void distributePacket(int id, byte[] data, IIOHandler sender) {

		APacket p = IMH.getPacketRegistrie().getNewPacketOfID(id);

		//Log.getInstance().logDebug("PacketDistributor", "Distributing Packet\nID:" + id + "\nClass:" + p.getClass());

		try {
			p.readFromData(DataIn.newInstance(data.clone()));
		} catch(IOException e) {
			e.printStackTrace();
		}

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
