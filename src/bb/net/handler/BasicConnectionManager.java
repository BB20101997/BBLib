package bb.net.handler;

import bb.net.client.ConnectionEstablishment;
import bb.net.enums.ServerStatus;
import bb.net.enums.Side;
import bb.net.interfaces.*;
import bb.net.packets.DataOut;
import bb.net.packets.PacketDistributor;
import bb.net.packets.PacketRegistrie;
import bb.net.packets.connecting.DisconnectPacket;
import bb.net.server.ConnectionListener;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@SuppressWarnings("WeakerAccess")
public class BasicConnectionManager implements IConnectionManager {


	private final static Logger log;
	static {
		log = Logger.getLogger(BasicConnectionManager.class.getName());
		log.addHandler(new BBLogHandler(Constants.getBBLibLogFile()));
	}

	protected     ServerStatus     serverStatus   = ServerStatus.NOT_STARTED;
	private final List<IIOHandler> connections    = new ArrayList<>();
	private       int              maxConnections = 20;

	protected IPacketRegistrie   packetRegistrie;
	protected IPacketDistributor packetDistributor;
	protected List<AIConnectionEventHandler> aichehList = new ArrayList<>();

	protected List<IConnectionEventHandler> IConnectionEventHandlerList = new ArrayList<>();
	//just a loopback to the inbound connection
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
		public void run() {
		}
	};
	//the Server connection on the Clients side, a loopback on the Servers side
	private IIOHandler SERVER;
	//server or client?
	private Side       side;
	//for broadcasts
	private IIOHandler ALL = new IIOHandler() {
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
					th = new Thread(() -> {iio.sendPacket(p);});
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

	};
	private SSLSocket socket;

	protected ConnectionListener conLis;

	public BasicConnectionManager() {
		this(Side.CLIENT);
	}

	public BasicConnectionManager(Side s) {
		this(s, 256);
	}

	public BasicConnectionManager(Side s, int port) {
		log.log(Level.INFO, "Constructor");
		packetRegistrie = new PacketRegistrie();
		packetDistributor = new PacketDistributor(this);
		packetDistributor.registerPacketHandler(new DefaultPacketHandler(this));
		side = s;
		if(side == Side.SERVER) {
			//whoops was supposed to be vice-versa
			SERVER = LOCAL;
			conLis = new ConnectionListener(port, this);
			new Thread(conLis).start();
		}
	}

	public void shutdown() {
		log.log(Level.INFO,"Starting Shutdown");
		if(conLis != null) {
			conLis.end();
		}
		disconnect(ALL());
		serverStatus = ServerStatus.SHUTDOWN;
		log.log(Level.INFO, "Finished Shutdown");
	}

	public IIOHandler LOCAL() {
		return LOCAL;
	}

	public IIOHandler SERVER() {
		return SERVER;
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
		log.log(Level.FINER,"Adding an EventHandler");
		IConnectionEventHandlerList.add(iceh);
	}

	public void handleIConnectionEvent(IConnectionEvent event) {
		log.log(Level.FINE,"Handling Event:"+event);
		for(IConnectionEventHandler iceh : IConnectionEventHandlerList) {
			iceh.HandleEvent(event);
		}
	}

	@Override
	public void sendPackage(APacket p, IIOHandler target) {
		log.log(Level.FINE, "Sending Packet "+p+" to Target "+target);
		if(side == Side.CLIENT&&target!=LOCAL()) {
			if(SERVER != null) {
				SERVER.sendPacket(p);
			} else {
				log.log(Level.FINE,"SERVER is equivalent to null when sending Package!");
			}
		} else {
			if(target!=null){
			target.sendPacket(p);
		}else{
				log.log(Level.FINE, "Target is equivalent to null when sending Package!");
				throw new RuntimeException("Null target critical failure!");
			}
		}
	}

	@Override
	public void disconnect(IIOHandler a) {
		log.fine("Disconnect:"+a);
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
					connections.forEach(bb.net.interfaces.IIOHandler::stop);
					connections.clear();
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
		log.fine("Connecting to:"+host+" on Port:"+port);
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