package bb.net.packets;

import bb.net.interfaces.IData;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

/**
 * Created by BB20101997 on 31.08.2014.
 */
public class DataIn extends DataInputStream implements IData {

	public static DataIn newInstance(byte[] b) {
		return new DataIn(new InStream(b));
	}


	private DataIn(InputStream inputStream) {
		super(inputStream);
	}

	private static class InStream extends InputStream {

		public InStream(byte[] b) {
			for(byte by : b) {
				bList.add(by);
			}
		}

		private final LinkedList<Byte> bList = new LinkedList<>();

		@Override
		public int read() throws IOException {
			if(bList.isEmpty()) {
				return -1;
			} else {
				return bList.pollFirst();
			}
		}
	}

}
