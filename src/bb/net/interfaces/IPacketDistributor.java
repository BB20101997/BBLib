package bb.net.interfaces;

/**
 * Created by BB20101997 on 03.09.2014.
 */
public interface IPacketDistributor {


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

}
