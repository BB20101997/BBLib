package bb.net.interfaces;


import bb.net.packets.connecting.PacketSyncPacket;

/**
 * Created by BB20101997 on 03.09.2014.
 */
public interface IPacketRegistrie{

	int registerPacket(Class<? extends APacket> p);

	int getID(Class<? extends APacket> p);

	boolean containsPacket(Class<? extends APacket> p);

	PacketSyncPacket getSyncPacket();

	void handelSyncPacket(PacketSyncPacket psp);

	APacket getNewPacketOfID(int id);

	Class<? extends APacket> getPacketClassByID(int id);
}
