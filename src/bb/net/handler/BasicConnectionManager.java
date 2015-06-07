package bb.net.handler;

import bb.net.client.ConnectionEstablishment;
import bb.net.enums.NetworkState;
import bb.net.enums.ServerStatus;
import bb.net.enums.Side;
import bb.net.interfaces.*;
import bb.net.packets.DataOut;
import bb.net.packets.PacketDistributor;
import bb.net.packets.PacketRegistrie;
import bb.net.packets.connecting.DisconnectPacket;
import bb.net.server.ConnectionListener;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BasicConnectionManager implements IConnectionManager {


	protected       ServerStatus     serverStatus   = ServerStatus.NOT_STARTED;
	protected final List<IIOHandler> connections    = new ArrayList<>();
	protected       int              maxConnections = 20;

	protected IPacketRegistrie   packetRegistrie;
	protected IPacketDistributor packetDistributor;
	protected List<AIConnectionEventHandler> aichehList = new ArrayList<>();

	protected List<IConnectionEventHandler> IConnectionEventHandlerList = new ArrayList<>();
	protected IIOHandler                    LOCAL                       = new IIOHandler() {
		@Override
		public void start() {

		}

		@Override
		public void stop() {

		}

		@Override
		public boolean isDummy() {
			return true;
		}

		@Override
		public boolean sendPacket(APacket p) {

			DataOut dao = DataOut.newInstance();
			try {
				p.writeToData(dao);
			} catch(IOException e) {
				e.printStackTrace();
			}
			//may cause a loop when the PacketHandler sends back a Packet to the sender!
			getPacketDistributor().distributePacket(packetRegistrie.getID(p.getClass()), dao.getBytes(), LOCAL);
			return true;
		}

		@Override
		public boolean isAlive() {
			return true;
		}

		@Override
		public void receivedHandshake() {

		}

		@Override
		public NetworkState getNetworkState() {
			return NetworkState.USER_CLIENT;
		}

		@Override
		public void run() {

		}
	};
	protected IIOHandler SERVER;
	protected Side       side;
	protected IIOHandler ALL = new IIOHandler() {
		@Override
		public void start() {

		}

		@Override
		public void run() {

		}

		@Override
		public void stop() {
		}

		@Override
		public boolean isDummy() {
			return true;
		}

		@Override
		public boolean sendPacket(APacket p) {
			if(side == Side.SERVER) {
				Thread th;
				for(IIOHandler iio : connections) {
					th = new Thread(()->{iio.sendPacket(p);});
					th.start();
				}
				return true;
			} else {
				return SERVER().sendPacket(p);
			}
		}

		@Override
		public boolean isAlive() {
			return true;
		}

		@Override
		public void receivedHandshake() {
		}

		@Override
		public NetworkState getNetworkState() {
			return side == Side.SERVER ? NetworkState.USER_CLIENT : NetworkState.MANAGEMENT;
		}


	};
	protected SSLSocket socket;

	protected ConnectionListener conLis;

	public BasicConnectionManager() {
		packetRegistrie = new PacketRegistrie();
		packetDistributor = new PacketDistributor(this);
		packetDistributor.registerPacketHandler(new DefaultPacketHandler(this));
	}

	public BasicConnectionManager(Side s) {
		this(s, 256);
	}

	public BasicConnectionManager(Side s, int port) {
		this();
		side = s;
		if(side == Side.SERVER) {
			LOCAL = SERVER;
			conLis = new ConnectionListener(port, this);
			new Thread(conLis).start();
		}
	}

	public void shutdown() {
		if(conLis != null) {
			conLis.end();
		}
		disconnect(ALL());
		serverStatus = ServerStatus.SHUTDOWN;
	}

	public IIOHandler LOCAL() {
		return LOCAL;
	}

	public IIOHandler SERVER() {
		return side == Side.SERVER ? LOCAL : SERVER;
	}

	@Override
	public IIOHandler ALL() {
		return ALL;
	}

	@Override
	public IPacketRegistrie getPacketRegistrie() {
		return packetRegistrie;
	}

	@Override
	public IPacketDistributor getPacketDistributor() {
		return packetDistributor;
	}

	@Override
	public void addConnectionEventHandler(IConnectionEventHandler iceh) {
		IConnectionEventHandlerList.add(iceh);
	}

	public void handleIConnectionEvent(IConnectionEvent event) {
		for(IConnectionEventHandler iceh : IConnectionEventHandlerList) {
			iceh.HandleEvent(event);
		}
	}

	@Override
	public void sendPackage(APacket p, IIOHandler target) {
		if(side == Side.CLIENT) {
			if(SERVER != null) {
				SERVER.sendPacket(p);
			} else {
				//Log.getInstance().logWarning("ClientConnectionHandler", "Couldn't send Packet to Server!");
			}
		} else {
			target.sendPacket(p);
		}
	}

	@Override
	public void disconnect(IIOHandler a) {

		if(side == Side.CLIENT) {

			if(SERVER != null) {
				sendPackage(new DisconnectPacket(), SERVER());
				SERVER.stop();
				SERVER = null;
			}
			try {
				if(socket != null) {
					socket.close();
				}

			} catch(IOException e) {
				e.printStackTrace();
			}


		} else {
			if(a != ALL() && a != SERVER()) {
				a.stop();
				connections.remove(a);
			} else {
				synchronized(connections) {
					for(IIOHandler iioHandler : connections) {
						iioHandler.stop();
						connections.remove(iioHandler);
					}
				}
			}
		}

	}

	@Override
	public final Side getSide() {
		return side;
	}

	@Override
	public boolean connect(String host, int port) {
		if(side == Side.CLIENT) {
			if(SERVER != null) {
				disconnect(SERVER);
				try {
					SERVER.stop();
				} catch(Throwable e) {

					e.printStackTrace();
				}
				SERVER = null;
			}

			socket = new ConnectionEstablishment(host, port).getSocket();

			if(socket != null) {
				try {
					SERVER = new BasicIOHandler(socket.getInputStream(), socket.getOutputStream(), this, true);
					SERVER.start();
				} catch(IOException e) {
					e.printStackTrace();
					return false;
				}
			} else {
				return false;
			}
			return true;
		} else {
			return true;
		}
	}

	@Override
	public int getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(int maxC) {
		maxConnections = maxC;
	}

	@Override
	public List<IIOHandler> getConnections() {
		return connections;
	}

	@Override
	public ServerStatus getServerStatus() {
		return serverStatus;
	}

	@Override
	public void setServerStatus(ServerStatus serverStat) {
		serverStatus = serverStat;
	}

}