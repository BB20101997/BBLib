package bb.net.client;

import bb.net.interfaces.IConnectionManager;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;


/**
 * @author BB20101997
 */
public class ConnectionEstablishment {

	private SSLSocket sock = null;

	/**
	 * @param imh  just needed to print out errors to the user may be removed later
	 * @param host the host to Connect to
	 * @param port the port the host is listening to
	 */
	public ConnectionEstablishment(String host, int port) {

		try {

			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, null, null);

			SSLSocketFactory ssf = sc.getSocketFactory();
			SSLSocket s = (SSLSocket) ssf.createSocket(host, port);
			bb.net.util.Socket.enableAnonConnection(s);
			s.startHandshake();
			sock = s;
		} catch(IOException | KeyManagementException | NoSuchAlgorithmException e) {
			//Log.getInstance().logError("ConnectionEstablisher", "I/OException : " + host);
			sock = null;
			e.printStackTrace();
		}
	}

	/**
	 * @return returns the Socket that has benn created
	 */
	public SSLSocket getSocket() {

		return sock;
	}

}
