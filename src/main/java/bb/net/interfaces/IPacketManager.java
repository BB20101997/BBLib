package bb.net.interfaces;

import bb.net.packets.connecting.PacketSyncPacket;

/**
 * Created by BB20101997 on 19. Sep. 2016.
 */
@SuppressWarnings("deprecation")
public interface IPacketManager extends IPacketRegistrie, IPacketDistributor {

	//---- Start of old PacketDistributor ----
	/**
	 * if the instance of PH is already registered it should not be registered twice and the id of the old registration
	 * should be returned
	 */
	int registerPacketHandler(IPacketHandler ph);

	/**
	 * @param id     The id of the Packet the data is from
	 * @param data   the data of the packet to be created and distributed
	 * @param sender the IIOHandler that received/send the Packet (should be the IOHandler that received the packet)
	 */
	void distributePacket(int id, byte[] data, IIOHandler sender);
	//---- End of old PacketDistributor ----

	//---- Start of old PacketRegistrie ----
	int registerPacket(Class<? extends APacket> p);

	int getID(Class<? extends APacket> p);

	boolean containsPacket(Class<? extends APacket> p);

	PacketSyncPacket getSyncPacket();

	void handelSyncPacket(PacketSyncPacket psp);

	APacket getNewPacketOfID(int id);

	Class<? extends APacket> getPacketClassByID(int id);
	//---- End of old PacketRegistrie ----
}
