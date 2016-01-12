package bb.net.client;

import bb.util.file.BBLogHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;


/**
 * @author BB20101997
 */
public class ConnectionEstablishment {

	public static final Logger log = Logger.getLogger(ConnectionEstablishment.class.getName());

	static {
		log.addHandler(new BBLogHandler(new File("/log/BBLib.log").getAbsoluteFile()));
	}

	private SSLSocket sock = null;

	/**
	 * @param host the host to Connect to
	 * @param port the port the host is listening to
	 */
	public ConnectionEstablishment(String host, int port) {
		log.entering(this.getClass().toString(),"ConnectionEstablishment");
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, null, null);

			SSLSocketFactory ssf = sc.getSocketFactory();
			SSLSocket s = (SSLSocket) ssf.createSocket(host, port);
			bb.net.util.Socket.enableAnonConnection(s);
			s.startHandshake();
			sock = s;
		} catch(IOException | KeyManagementException | NoSuchAlgorithmException e) {
			log.warning("I/OException : " + host);
			sock = null;
		}
		log.exiting(this.getClass().toString(),"ConnectionEstablishment");
	}

	/**
	 * @return returns the Socket that has benn created
	 */
	public SSLSocket getSocket() {

		return sock;
	}

}
