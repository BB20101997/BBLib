package bb.net.handler;

import bb.net.enums.NetworkState;
import bb.net.enums.Side;
import bb.net.interfaces.APacket;
import bb.net.interfaces.IConnectionManager;
import bb.net.interfaces.IIOHandler;
import bb.net.packets.DataOut;
import bb.net.packets.connecting.DisconnectPacket;
import bb.net.packets.connecting.HandshakePacket;
import com.sun.istack.internal.Nullable;

import java.io.*;


/**
 * @author BB20101997
 */
public class BasicIOHandler implements Runnable, IIOHandler {

	private final IConnectionManager IMH;
	private final DataInputStream    dis;
	private final DataOutputStream   dos;
	private boolean handshakeReceived = false;

	@Nullable
	private boolean continueLoop = true;
	private Thread thread;
	private NetworkState status = NetworkState.UNKNOWN;

	public BasicIOHandler(final InputStream IS, OutputStream OS, IConnectionManager imh, boolean client) {
		IMH = imh;
		//Log.getInstance().logDebug("BasicIOHandler", "Creation Streams");
		dis = new DataInputStream(IS);
		dos = new DataOutputStream(OS);
		status = NetworkState.PRE_HANDSHAKE;
		if(imh.getSide() == Side.CLIENT) {
			startHandshake(client);
		} else {
			sendPacket(imh.getPacketRegistrie().getSyncPacket());
		}
	}

	private class handshakeRunnable implements Runnable {

		public final Object obj = new Object();

		@Override
		public void run() {
			for(int i = 0; !handshakeReceived || i > 900000; i++) {
				try {
					synchronized(obj) {
						obj.wait(10);
					}
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(!handshakeReceived) {
				stop();
				//Log.getInstance().logInfo("BasicIOHandler", "Shutting down : No Handshake!");
				status = NetworkState.SHUTDOWN;
			} else {
				status = NetworkState.POST_HANDSHAKE;
				//Log.getInstance().logInfo("BasicIOHandler", "Handshake received!");
			}
		}
	}

	private void startHandshake(boolean client) {
		sendPacket(new HandshakePacket(client));
	}

	public void start() {
		if(thread == null) {
			thread = new Thread(this);
		}
		if(thread.getState() == Thread.State.NEW) {
			thread.start();
		}
	}

	public void stop() {
		continueLoop = false;
		if(thread != null) {
			thread.interrupt();
		}
	}

	@Override
	public boolean isDummy() {
		return false;
	}

	@SuppressWarnings("unchecked")
	public boolean sendPacket(APacket p) {

		if(IMH.getPacketRegistrie().containsPacket(p.getClass())) {

			//Log.getInstance().logDebug("BasicIOHandler", "Sending Packet : " + p.getClass());

			int id = IMH.getPacketRegistrie().getID(p.getClass());

			DataOut dataOut = DataOut.newInstance();

			try {
				p.writeToData(dataOut);
			} catch(IOException e) {
				e.printStackTrace();
				return false;
			}

			byte[] b = dataOut.getBytes();


			try {
				dos.writeInt(id);
				dos.writeInt(b.length);
				dos.write(b);
			} catch(IOException e) {
				return false;
			}


			return true;

		}

		return false;
	}	@Override
	public void run() {

		//Log.getInstance().logDebug("BasicIOHandler", "Starting BasicIOHandler");

		if(IMH.getSide() == Side.SERVER) {
			Thread t = new Thread(new handshakeRunnable());
			t.start();
		}

		int id;
		int length;

		while(continueLoop) {
			try {
				id = dis.readInt();
				length = dis.readInt();
				byte[] by = new byte[length];
				dis.readFully(by);
				//Log.getInstance().logDebug("BasicIOHandler", "IOHandler: PacketReceived : " + IMH.getIChatInstance().getPacketRegistrie().getPacketClassByID(id) + " on Side : " + IMH.getSide());
				IMH.getPacketDistributor().distributePacket(id, by, this);

			} catch(Exception e) {
				sendPacket(new DisconnectPacket());
				//Log.getInstance().logError("BasicIOHandler", "Exception in IOHandler, closing connection!");
				e.printStackTrace();
				continueLoop = false;
			}
		}

		//Log.getInstance().logDebug("BasicIOHandler","Stopping IOHandler");

		IMH.disconnect(this);

		status = NetworkState.SHUTDOWN;

		try {
			dis.close();
		} catch(IOException e) {
			e.printStackTrace();
		}

		try {
			dos.close();
		} catch(IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @return if the end() method was called or the run method ended
	 */
	public boolean isAlive() {
		return continueLoop;
	}

	@Override
	public void receivedHandshake() {
		handshakeReceived = true;
	}

	@Override
	public NetworkState getNetworkState() {
		return status;
	}

	@Override
	public void finalize() throws Throwable {
		if(isAlive())
			stop();
		super.finalize();

	}




}
