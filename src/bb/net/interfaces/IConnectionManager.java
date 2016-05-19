package bb.net.interfaces;

import bb.net.enums.ServerStatus;
import bb.net.enums.Side;
import bb.util.event.EventHandler;

import java.util.List;

/**
 * @author BB20101997
 */
public interface IConnectionManager {

	/**
	 * for Client side this should return a connection to this Client equivalent to a Server connection for Server side
	 * this should return a connection to this Server equivalent to a Client connection
	 */
	IIOHandler LOCAL();

	/**
	 * returns the IIOHandler handling the Server connection, should be the only one on the Client Side and should be
	 * equivalent to LOCAL() on Server side
	 */
	IIOHandler SERVER();

	/**
	 * Should be a constant dummy to perform an action on all connections
	 */
	IIOHandler ALL();

	IPacketRegistrie getPacketRegistrie();

	IPacketDistributor getPacketDistributor();

	void addConnectionEventHandler(EventHandler<IConnectionEvent> iceh);

	void handleIConnectionEvent(IConnectionEvent event);

	void sendPackage(APacket p, IIOHandler target);

	// disconnect the connection to a IIOHandler
	void disconnect(IIOHandler a);

	Side getSide();

	// connects to the host at the port port
	boolean connect(String host, int port);

	int getMaxConnections();

	void setMaxConnections(int i);

	List<IIOHandler> getConnections();


	ServerStatus getServerStatus();

	void setServerStatus(ServerStatus serverStat);

}
