package bb.net.packets;

import bb.net.interfaces.APacket;
import bb.net.interfaces.IPacketRegistrie;
import bb.net.packets.connecting.PacketSyncPacket;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by BB20101997 on 30.08.2014.
 */
public class PacketRegistrie implements IPacketRegistrie {

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
	@SuppressWarnings("unchecked")
	public PacketSyncPacket getSyncPacket() {
		Class<? extends APacket>[] pa = new Class[0];
		pa = PList.toArray(pa);
		return new PacketSyncPacket(pa);
	}

	@Override
	public void handelSyncPacket(PacketSyncPacket psp) {
		PList.clear();
		Collections.addAll(PList, psp.getPackageClasses());
	}

	public APacket getNewPacketOfID(int id) {
		try {
			return PList.get(id).getConstructor().newInstance();
		} catch(InstantiationException e) {
			e.printStackTrace();
			//Log.getInstance().logError("PacketRegistrie", "Could not Instantiate " + PList.get(id) + " probably missing public default Constructor");
			throw new RuntimeException("Instantiation Exception while Instantiating " + PList.get(id));
		} catch(IllegalAccessException e) {
			e.printStackTrace();
			//Log.getInstance().logError("PacketRegistrie", "Could not Instantiate " + PList.get(id) + " probably missing public default Constructor");
			throw new RuntimeException("IllegalAccess Exception while Instantiating " + PList.get(id));
		} catch(NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("NoSuchMethodException while Instantiating " + PList.get(id));
		} catch(InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException("InvocationTargetException while Instantiating " + PList.get(id));
		}
	}

	public Class<? extends APacket> getPacketClassByID(int id) {
		return PList.get(id);
	}
}
