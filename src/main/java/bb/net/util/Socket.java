package bb.net.util;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by BB20101997 on 29.08.2014.
 */
public class Socket {

	public static void enableAnonConnection(SSLServerSocket socket) {
		String[] a = socket.getSupportedCipherSuites();
		Stream<String> stream = Arrays.stream(a);
		stream = stream.filter(s -> s.indexOf("_anon_")>0);
		String[] c = stream.toArray(String[]::new);
		socket.setEnabledCipherSuites(c);
	}

	public static void enableAnonConnection(SSLSocket socket) {
		String[]       a      = socket.getSupportedCipherSuites();
		Stream<String> stream = Arrays.stream(a);
		stream = stream.filter(s -> s.indexOf("_anon_") > 0);
		String[] c = stream.toArray(String[]::new);
		socket.setEnabledCipherSuites(c);
	}

}
