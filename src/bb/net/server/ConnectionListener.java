package bb.net.server;

import bb.net.enums.ServerStatus;
import bb.net.handler.BasicIOHandler;
import bb.net.interfaces.IConnectionManager;
import bb.net.interfaces.IIOHandler;
import bb.net.packets.connecting.DisconnectPacket;
import com.sun.istack.internal.NotNull;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BB20101997 on 30.04.2015.
 */
public class ConnectionListener extends Thread {

	private final int port;
	private boolean      continueLoop     = true;
	final   List<Socket> clientSocketList = new ArrayList<>();
	final IConnectionManager MH;

	/**
	 * @param p the Port the ConnectionListener will use
	 */
	public ConnectionListener(int p, @NotNull IConnectionManager m) {
		MH = m;
		port = p;
	}

	/**
	 * Stop the ConnectionListener
	 */
	public void end() {

		continueLoop = false;
		interrupt();
		//Log.getInstance().logDebug("ServerConnectionHandler", "Closing for new Connections");

		for(Socket cl : clientSocketList) {
			try {
				cl.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}

		for(IIOHandler ica : MH.getConnections()) {

			if(ica instanceof BasicIOHandler) {
				BasicIOHandler io = (BasicIOHandler) ica;

				try {
					io.stop();
				} catch(Throwable e) {
					e.printStackTrace();
				}

			}
		}
		MH.getConnections().clear();
	}

	@Override
	protected void finalize() throws Throwable {
		end();
		super.finalize();
	}

	/**
	 * the main Function called by the run Function ,it is listening for new Connections
	 */
	@SuppressWarnings("unchecked")
	public void listen() {
		try {

			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, null, null);

			SSLServerSocketFactory ssf = sc.getServerSocketFactory();
			SSLServerSocket socketS = (SSLServerSocket) ssf.createServerSocket(port);

			bb.net.util.Socket.enableAnonConnection(socketS);

			if(MH != null) {
				//MH.println("Awaiting connections on " + socketS.getLocalSocketAddress());
				while(continueLoop) {
					SSLSocket s = (SSLSocket) socketS.accept();
					if(!continueLoop) {
						s.close();
						break;
					}

					BasicIOHandler c = new BasicIOHandler(s.getInputStream(), s.getOutputStream(), MH, false);

					clientSocketList.add(s);
					MH.getConnections().add(c);
					Thread t = new Thread(c);
					t.start();

					updateUserCount();
				}
				MH.setServerStatus(ServerStatus.SHUTDOWN);
				MH.sendPackage(new DisconnectPacket(), MH.ALL());
				for(IIOHandler ica : MH.getConnections()) {
					try {
						ica.stop();
					} catch(Throwable e) {
						e.printStackTrace();
					}
				}
				MH.getConnections().clear();
			}

		} catch(KeyManagementException | NoSuchAlgorithmException | IOException e) {
			e.printStackTrace();
		}

	}

	private void updateUserCount() {
		int i = MH.getConnections().size();
		MH.setServerStatus(i > 0 ? i == MH.getMaxConnections() ? ServerStatus.FULL : i > MH.getMaxConnections() ? ServerStatus.OVERFILLED : ServerStatus.READY : ServerStatus.EMPTY);
	}

	@Override
	public void run() {

		if((port >= 0) && (port <= 65535)) {
			listen();
		} else {
			throw new RuntimeException("Port not in Range[0-65535]");
		}
	}

}
