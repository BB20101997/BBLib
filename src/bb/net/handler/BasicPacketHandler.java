package bb.net.handler;

import bb.net.interfaces.APacket;
import bb.net.interfaces.IIOHandler;
import bb.net.interfaces.IPacketHandler;
import bb.net.interfaces.IPacketRegistrie;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BB20101997 on 31.08.2014.
 */
public abstract class BasicPacketHandler implements IPacketHandler {

	protected final IPacketRegistrie packetRegistrie;

	private final List<Class<? extends APacket>> CList = new ArrayList<>();

	public BasicPacketHandler(IPacketRegistrie pr) {
		packetRegistrie = pr;
	}

	@SuppressWarnings("unchecked")
	public final void HandlePacket(APacket aPacket, IIOHandler sender) {

		int id = packetRegistrie.getID(aPacket.getClass());

		//Log.getInstance.logInfo("BasicPacketHandler", aPacket.getClass() + ", ID : " + id);

		Method m = null;
		try {
			m = getClass().getDeclaredMethod("handlePacket", aPacket.getClass(), IIOHandler.class);
			m.invoke(this, aPacket, sender);
		} catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			if(m!=null) {
				System.err.println(m.getName());
			}
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	protected final void addAssociatedPacket(Class<? extends APacket> cp) {
		synchronized(CList) {
			if(!CList.contains(cp)) {
				CList.add(cp);
				if(!packetRegistrie.containsPacket(cp)) {
					packetRegistrie.registerPacket(cp);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Class<APacket>[] getAssociatedPackets() {
		return CList.toArray(new Class[CList.size()]);
	}

}
