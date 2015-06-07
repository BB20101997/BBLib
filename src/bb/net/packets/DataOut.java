package bb.net.packets;

import bb.net.interfaces.IData;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BB20101997 on 31.08.2014.
 */
public class DataOut extends DataOutputStream implements IData {

	private final OutStream os;

	public static DataOut newInstance() {
		return new DataOut(new OutStream());
	}

	private DataOut(OutStream outStream) {
		super(outStream);
		os = outStream;
	}

	public byte[] getBytes() {
		return os.getBytes();
	}

	private static class OutStream extends OutputStream {

		private final List<Byte> bList = new ArrayList<>();

		@Override
		public void write(int b) throws IOException {
			byte a = (byte) b;
			bList.add(a);
		}

		public byte[] getBytes() {
			Byte[] a = bList.toArray(new Byte[bList.size()]);
			byte[] b = new byte[a.length];
			for(int i = 0; i < a.length; i++) {
				b[i] = a[i];
			}
			return b;
		}
	}


}
