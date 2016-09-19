package bb.net.handler;

import bb.net.client.ConnectionEstablishment;
import bb.net.enums.ServerStatus;
import bb.net.enums.Side;
import bb.net.interfaces.*;
import bb.net.packets.DataOut;
import bb.net.packets.PacketManager;
import bb.net.packets.connecting.DisconnectPacket;
import bb.net.server.ConnectionListener;
import bb.util.event.EventHandler;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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

	protected IPacketManager packetManager;
	@Deprecated
	protected IPacketRegistrie   packetRegistrie;
	@Deprecated
	protected IPacketDistributor packetDistributor;

	protected List<EventHandler<IConnectionEvent>> IConnectionEventHandlerList = new ArrayList<>();
	//just a loopback to the inbound connection
	protected IIOHandler         LOCAL                       = new IIOHandler() {

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
			getPacketManager().distributePacket(getPacketManager().getID(p.getClass()), dao.getBytes(), LOCAL);
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
	protected IIOHandler SERVER;
	//server or client?
	private final Side       side;
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

	//init all client side
	public BasicConnectionManager() {
		this(Side.CLIENT);
	}

	//init all server side
	public BasicConnectionManager(int port){
		this(Side.SERVER);
		SERVER = LOCAL;
		conLis = new ConnectionListener(port, this);
		new Thread(conLis).start();
	}

	//init all side irrelevant
	private BasicConnectionManager(Side s){
		log.log(Level.INFO, "Constructor");
		packetManager = new PacketManager(this);
		packetRegistrie = packetManager;
		packetDistributor = packetManager;
		packetDistributor.registerPacketHandler(new DefaultPacketHandler(this));
		side = s;

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
	public IPacketManager getPacketManager() {
		return packetManager;
	}

	@Override
	public void addConnectionEventHandler(EventHandler<IConnectionEvent> iceh) {
		log.log(Level.FINER,"Adding an EventHandler");
		IConnectionEventHandlerList.add(iceh);
	}

	public void handleIConnectionEvent(IConnectionEvent event) {
		log.log(Level.FINE,"Handling Event:"+event);
		for(EventHandler<IConnectionEvent> iceh : IConnectionEventHandlerList) {
			try {
				iceh.HandleEvent(event);
			} catch(NoSuchMethodException e) {
				System.err.println("The IIConnectionHandlerEventHandler " + iceh.getClass() + " didn't support the event " + event.getClass());
				System.err.println("Probably not a bug just an Event not handled by this Handler!");
			} catch(InvocationTargetException e) {
				System.err.println("The IIConnectionHandlerEventHandler " + iceh.getClass() + " didn't fail handling the event " + event.getClass());
				System.err.println("All Exceptions should be handled internally and shouldn't land here!This is therefor a bug,pleas contact the developer! ");
				e.printStackTrace();
			} catch(IllegalAccessException e) {
				System.err.println("The IIConnectionHandlerEventHandler " + iceh.getClass() + " was not accessible for the event " + event.getClass());
				System.err.println("Please check if the access is public!Inform the developer this is most definitely a bug!");
			}
		}
	}

	@Override
	public void sendPackage(APacket p, IIOHandler target) {
		log.log(Level.FINE, "Sending Packet "+p+" to Target "+target);
		if(side == Side.CLIENT&&target!=LOCAL()) {
			if(SERVER != null) {
				SERVER.sendPacket(p);
			} else {
				log.log(Level.FINE,"SERVER is equivalent to null when sending Package! Packet will be dropped!");
			}
		} else {
			if(target!=null){
				target.sendPacket(p);
			}else{
				log.log(Level.FINE, "Target is equivalent to null when sending Package! Packet will be dropped!");
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