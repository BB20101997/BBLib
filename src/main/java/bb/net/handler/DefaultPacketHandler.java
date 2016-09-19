package bb.net.handler;

import bb.net.enums.Side;
import bb.net.event.ConnectEvent;
import bb.net.event.DisconnectEvent;
import bb.net.interfaces.IConnectionManager;
import bb.net.interfaces.IIOHandler;
import bb.net.interfaces.IPacketRegistrie;
import bb.net.packets.connecting.DisconnectPacket;
import bb.net.packets.connecting.HandshakePacket;
import bb.net.packets.connecting.PacketSyncPacket;

/**
 * Created by BB20101997 on 09.04.2015.
 */
public class DefaultPacketHandler extends BasicPacketHandler {

	protected final IConnectionManager ich;

	public DefaultPacketHandler(IConnectionManager icm) {
		super(icm.getPacketRegistrie());
		ich = icm;
		IPacketRegistrie pr = icm.getPacketRegistrie();
		addAssociatedPacket(pr,DisconnectPacket.class);
		addAssociatedPacket(pr,HandshakePacket.class);
		addAssociatedPacket(pr,PacketSyncPacket.class);
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
		ich.getPacketRegistrie().handelSyncPacket(psp);
	}
}
