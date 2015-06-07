package bb.net.client;

import bb.net.interfaces.IConnectionManager;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.rmi.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;


/**
 * @author BB20101997
 */
public class ConnectionEstablishment {

	private SSLSocket sock = null;

	/**
	 * @param host the host to Connect to
	 * @param port the port the host is listening to
	 * @param imh  just needed to print out errors to the user may be removed later
	 */
	public ConnectionEstablishment(String host, int port, IConnectionManager imh) {

		try {

			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, null, null);

			SSLSocketFactory ssf = sc.getSocketFactory();
			SSLSocket s = (SSLSocket) ssf.createSocket(host, port);
			bb.net.util.Socket.enableAnonConnection(s);
			s.startHandshake();
			sock = s;
		} catch(UnknownHostException e) {
			//Log.getInstance().logError("ConnectionEstablisher", "UnknownHostException : " + host);
			sock = null;
			e.printStackTrace();
		} catch(IOException e) {
			//Log.getInstance().logError("ConnectionEstablisher", "I/OException : " + host);
			sock = null;
			e.printStackTrace();
		} catch(NoSuchAlgorithmException e) {
			//Log.getInstance().logError("ConnectionEstablisher", "NpSuchAlgorithmException : " + host);
			sock = null;
			e.printStackTrace();
		} catch(KeyManagementException e) {
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
