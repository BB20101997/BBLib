package bb.net.handler;

import bb.net.interfaces.IConnectionManager;
import bb.net.interfaces.IIOHandler;
import bb.net.packets.connecting.DisconnectPacket;
import bb.net.packets.connecting.HandshakePacket;
import bb.net.packets.connecting.PacketSyncPacket;

/**
 * Created by BB20101997 on 09.04.2015.
 */
public class DefaultPacketHandler extends BasicPacketHandler {

	final IConnectionManager ich;

	public DefaultPacketHandler(IConnectionManager pr) {
		super(pr.getPacketRegistrie());
		ich = pr;
		addAssociatedPacket(DisconnectPacket.class);
		addAssociatedPacket(HandshakePacket.class);
		addAssociatedPacket(PacketSyncPacket.class);
	}

	public void handlePacket(DisconnectPacket dp, IIOHandler iio) {
		ich.disconnect(iio);
	}

	public void handlePacket(HandshakePacket hp,IIOHandler iio){
		iio.receivedHandshake();
	}

	public void handlePacket(PacketSyncPacket psp,IIOHandler iio){
		packetRegistrie.handelSyncPacket(psp);
	}
}
