package bb.net.handler;

import bb.net.interfaces.APacket;
import bb.net.interfaces.IIOHandler;
import bb.net.interfaces.IPacketHandler;
import bb.net.interfaces.IPacketRegistrie;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by BB20101997 on 31.08.2014.
 */
public abstract class BasicPacketHandler implements IPacketHandler {

	private static final Logger log;

	static {
		log = Logger.getLogger(BasicPacketHandler.class.getName());
		log.addHandler(new BBLogHandler(Constants.getLogFile("BBLib")));
	}

	protected final IPacketRegistrie packetRegistrie;

	private final List<Class<? extends APacket>> CList = new ArrayList<>();

	public BasicPacketHandler(IPacketRegistrie pr) {
		packetRegistrie = pr;
	}

	@SuppressWarnings("unchecked")
	public final void HandlePacket(APacket aPacket, IIOHandler sender) {


		log.finer("Handling Packet \n"+aPacket.getClass().toString());

		Method m = null;
		try {
			m = getClass().getDeclaredMethod("handlePacket", aPacket.getClass(), IIOHandler.class);
			m.invoke(this, aPacket, sender);
		} catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			if(m != null) {
				System.err.println(m.getName()+":"+aPacket.getClass().getName());
			}
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public Class<APacket>[] getAssociatedPackets() {
		return CList.toArray(new Class[CList.size()]);
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

}
