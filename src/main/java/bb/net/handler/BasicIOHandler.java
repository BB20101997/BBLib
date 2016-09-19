package bb.net.handler;

import bb.net.enums.Side;
import bb.net.event.ConnectionClosedEvent;
import bb.net.interfaces.APacket;
import bb.net.interfaces.IConnectionManager;
import bb.net.interfaces.IIOHandler;
import bb.net.packets.DataOut;
import bb.net.packets.connecting.DisconnectPacket;
import bb.net.packets.connecting.HandshakePacket;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.io.*;
import java.util.logging.Logger;


/**
 * @author BB20101997
 */
public class BasicIOHandler implements Runnable, IIOHandler {

	private final static Logger log;
	
	static{
		log = Logger.getLogger(BasicIOHandler.class.getName());
		log.addHandler(new BBLogHandler(Constants.getBBLibLogFile()));
	}

	private final IConnectionManager IMH;
	private final DataInputStream    dis;
	private final DataOutputStream   dos;
	private boolean handshakeReceived = false;

	private boolean continueLoop = true;
	private Thread thread;

	public BasicIOHandler(final InputStream IS, OutputStream OS, IConnectionManager imh, boolean client) {
		IMH = imh;
		log.fine("Creating Streams");
		dis = new DataInputStream(IS);
		dos = new DataOutputStream(OS);
		if(imh.getSide() == Side.CLIENT) {
			startHandshake(client);
		} else {
			sendPacket(imh.getPacketRegistrie().getSyncPacket());
		}
	}

	private class handshakeRunnable implements Runnable {

		final Object obj = new Object();

		@Override
		public void run() {
			for(int i = 0; (!handshakeReceived) && (i < 900000); i++) {
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
				log.fine("Shutting down : No Handshake!");
			}else {
				//TODO: Fill else Statement
				log.fine("Handshake received!");
			}
		}
	}

	private void startHandshake(boolean client) {
		log.fine("Starting Handshake");
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

			log.finest("Sending Packet:"+p.toString());

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

		log.entering(this.getClass().getName(),"run");

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
				log.fine("Packet Received:"+IMH.getPacketRegistrie().getPacketClassByID(id)+ " on Side: "+IMH.getSide());
				IMH.getPacketDistributor().distributePacket(id, by, this);

			} catch(Exception e) {
				sendPacket(new DisconnectPacket());
				log.severe("Exception in run, closing connection!");
				e.printStackTrace();
				continueLoop = false;
			}
		}

		log.fine("Stopping IOHandler!");

		IMH.disconnect(this);

		boolean successfullClosed = true;

		try {
			dis.close();
		} catch(IOException e) {
			successfullClosed = false;
			e.printStackTrace();
		}

		try {
			dos.close();
		} catch(IOException e) {
			successfullClosed = false;
			e.printStackTrace();
		}

		if(successfullClosed){
			IMH.handleIConnectionEvent(new ConnectionClosedEvent(this));
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
	public void finalize() throws Throwable {
		if(isAlive())
			stop();
		super.finalize();

	}




}
