package bb.net.packets;

import bb.net.interfaces.*;
import bb.net.packets.connecting.PacketSyncPacket;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by BB20101997 on 19. Sep. 2016.
 */
public class PacketManager implements IPacketManager {

	private final        IConnectionManager IMH;
	private static final Logger             log;

	static {
		log = Logger.getLogger("bb.net.packets.PacketDistributor");
		log.addHandler(new BBLogHandler(Constants.getBBLibLogFile()));
	}

	public PacketManager(IConnectionManager imh) {
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
	public void distributePacket(int id, final byte[] data, IIOHandler sender) {

		APacket p = IMH.getPacketRegistrie().getNewPacketOfID(id);

		log.fine("Distributing Packet\nID:" + id + "\nClass:" + p.getClass());
		log.fine("Incoming Packet with " + data.length + " bytes!" + System.lineSeparator() + "Packet is of class:" + p.getClass());
		try {
			p.readFromData(DataIn.newInstance(data.clone()));
		}
		catch(IOException e) {
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

	private final List<Class<? extends APacket>> PList = new ArrayList<>();

	public int registerPacket(Class<? extends APacket> p) {
		if(!PList.contains(p)) {
			PList.add(p);
		}
		return PList.indexOf(p);
	}

	public int getID(Class<? extends APacket> p) {
		return PList.indexOf(p);
	}

	public boolean containsPacket(Class<? extends APacket> p) {
		return PList.contains(p);
	}

	@Override
	public PacketSyncPacket getSyncPacket() {
		List<Class<? extends APacket>> clazzList = new ArrayList<>(PList);
		return new PacketSyncPacket(clazzList);
	}

	@Override
	public void handelSyncPacket(PacketSyncPacket psp) {
		PList.clear();
		PList.addAll(psp.getPackageClassList());
	}

	public APacket getNewPacketOfID(int id) {
		try {
			return PList.get(id).getConstructor().newInstance();
		}
		catch(InstantiationException e) {
			e.printStackTrace();
			//Log.getInstance().logError("PacketRegistrie", "Could not Instantiate " + PList.get(id) + " probably missing public default Constructor");
			throw new RuntimeException("Instantiation Exception while Instantiating " + PList.get(id));
		}
		catch(IllegalAccessException e) {
			e.printStackTrace();
			//Log.getInstance().logError("PacketRegistrie", "Could not Instantiate " + PList.get(id) + " probably missing public default Constructor");
			throw new RuntimeException("IllegalAccess Exception while Instantiating " + PList.get(id));
		}
		catch(NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("NoSuchMethodException while Instantiating " + PList.get(id));
		}
		catch(InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException("InvocationTargetException while Instantiating " + PList.get(id));
		}
	}

	public Class<? extends APacket> getPacketClassByID(int id) {
		return PList.get(id);
	}

	}
