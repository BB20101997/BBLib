package bb.net.handler;

import bb.net.enums.Side;
import bb.net.event.ConnectEvent;
import bb.net.event.DisconnectEvent;
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

	@SuppressWarnings("UnusedParameters")
	public void handlePacket(DisconnectPacket dp, IIOHandler iio) {
		ich.handleIConnectionEvent(new DisconnectEvent(iio, ich.getSide()== Side.SERVER?Side.CLIENT:Side.SERVER));
		ich.disconnect(iio);
	}

	public void handlePacket(HandshakePacket hp, IIOHandler iio) {
		ich.handleIConnectionEvent(new ConnectEvent(iio, hp.isUserClient()));
		iio.receivedHandshake();
	}

	@SuppressWarnings("UnusedParameters")
	public void handlePacket(PacketSyncPacket psp, IIOHandler iio) {
		packetRegistrie.handelSyncPacket(psp);
	}
}
