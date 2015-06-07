package bb.net.interfaces;

/**
 * Created by BB20101997 on 31.08.2014.
 */
public interface IPacketHandler {

	void HandlePacket(APacket p, IIOHandler sender);

	Class<? extends APacket>[] getAssociatedPackets();

}
