package bb.util.file.database;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileWriter {

	private enum Types {
		INT, STRING, ISAVEABLE, BYTE, BOOLEAN, FLOAT, DOUBLE, SHORT, LONG, CHAR

	}

	private final float VERSION;

	private String name = "";
	private List<Object> ObjectList = new ArrayList<Object>();
	private List<String> ObjectNames = new ArrayList<String>();
	private List<Types> ObjectType = new ArrayList<Types>();

	public FileWriter() {
		VERSION = 0.01f;
	}

	public boolean readFromStream(InputStream is) {
		return false;
	}

	public boolean writeToStream(OutputStream os) throws IOException {
		
		boolean noFail = true;
		
		os.write(("VERSION:" + Types.FLOAT.ordinal() + ":" + VERSION + ";")
				.getBytes());
		os.write(("name:" + Types.STRING.ordinal() + ":" + name + "" + ";")
				.getBytes());
		for (int i = 0; i < ObjectList.size(); i++) {
				if(!writeObjectToStream(ObjectList.get(i), ObjectType.get(i),ObjectNames.get(i), os)){
					noFail = false;
				}
		}
		return noFail;
	}

	private boolean writeObjectToStream(Object obj, Types t, String name,
			OutputStream os) {
		boolean noFail = true;
		
		try {
			os.write((name + ":" + t.ordinal() + ":").getBytes());

			switch (t) {
			case STRING: {
				os.write(((String) obj).getBytes());
				break;
			}
			case ISAVEABLE: {
				os.write("{".getBytes());
				ISaveAble is = (ISaveAble) obj;
				FileWriter fw = new FileWriter();
				is.writeToFileWriter(fw);
				fw.writeToStream(os);
				os.write("}".getBytes());
				break;
			}
			default: {
				os.write(String.valueOf(obj).getBytes());
				break;
			}
			}
		} catch (Exception e) {
			noFail = false;
			e.printStackTrace();
		}
		
		return noFail;
	}

	public void add(int i, String name) {
		ObjectList.add(i);
		ObjectNames.add(name);
		ObjectType.add(Types.INT);
	}

	public void add(String i, String name) {
		ObjectList.add(i);
		ObjectNames.add(name);
		ObjectType.add(Types.STRING);
	}

	public void add(ISaveAble i, String name) {
		ObjectList.add(i);
		ObjectNames.add(name);
		ObjectType.add(Types.ISAVEABLE);
	}

	public void add(byte i, String name) {
		ObjectList.add(i);
		ObjectNames.add(name);
		ObjectType.add(Types.BYTE);
	}

	public void add(boolean i, String name) {
		ObjectList.add(i);
		ObjectNames.add(name);
		ObjectType.add(Types.BOOLEAN);
	}

	public void add(float i, String name) {
		ObjectList.add(i);
		ObjectNames.add(name);
		ObjectType.add(Types.FLOAT);
	}

	public void add(double i, String name) {
		ObjectList.add(i);
		ObjectNames.add(name);
		ObjectType.add(Types.DOUBLE);
	}

	public void add(short i, String name) {
		ObjectList.add(i);
		ObjectNames.add(name);
		ObjectType.add(Types.SHORT);
	}

	public void add(long i, String name) {
		ObjectList.add(i);
		ObjectNames.add(name);
		ObjectType.add(Types.LONG);
	}

	public void add(char i, String name) {
		ObjectList.add(i);
		ObjectNames.add(name);
		ObjectType.add(Types.CHAR);
	}
}
