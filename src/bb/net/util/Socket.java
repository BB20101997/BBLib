package bb.net.util;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BB20101997 on 29.08.2014.
 */
public class Socket {

	public static void enableAnonConnection(SSLServerSocket socket) {
		String[] a = socket.getSupportedCipherSuites();
		List<String> b = new ArrayList<>();
		for(String s : a) {
			if(s.indexOf("_anon_") > 0) {
				b.add(s);
			}
		}

		String[] c = b.toArray(new String[b.size()]);
		socket.setEnabledCipherSuites(c);
	}

	public static void enableAnonConnection(SSLSocket socket) {
		String[] a = socket.getSupportedCipherSuites();
		List<String> b = new ArrayList<>();
		for(String s : a) {
			if(s.indexOf("_anon_") > 0) {
				b.add(s);
			}
		}

		String[] c = b.toArray(new String[b.size()]);
		socket.setEnabledCipherSuites(c);
	}

}
