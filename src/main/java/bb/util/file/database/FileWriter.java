package bb.util.file.database;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author BB20101997
 */
@SuppressWarnings("javadoc")
public final class FileWriter implements ISaveAble {

	/**
	 * The Version of the FileWriter
	 */
	@SuppressWarnings("FieldCanBeLocal")
	private final float VERSION = 0.02f;

	private static final String VersionName = "$VERSION$";

	/**
	 * @author BB20101997
	 */
	public enum Types {
		INT, STRING, ISAVEABLE, BYTE, BOOLEAN, FLOAT, DOUBLE, SHORT, LONG, CHAR
	}

	public FileWriter() {
		add(VERSION, VersionName);
	}

	/**
	 * A list of Objects , to be saved or to be read
	 */
	private final List<Object> ObjectList  = new ArrayList<>();
	/**
	 * The names of the Objects for easy retrieval
	 */
	private final List<String> ObjectNames = new ArrayList<>();
	/**
	 * The Type of Object saved in the ObjectList
	 */
	private final List<Types>  ObjectType  = new ArrayList<>();

	private void toObject(String name, Types type, String s) {

		Object o;

		switch(type) {
			case STRING:{
				add(s,name);
				break;
			}
			case INT: {
				o = Integer.valueOf(s);
				add((int) o, name);
				break;
			}
			case BYTE: {
				o = Byte.valueOf(s);
				add((byte) o, name);
				break;
			}
			case BOOLEAN: {
				o = Boolean.valueOf(s);
				add((boolean) o, name);
				break;
			}

			case FLOAT: {
				o = Float.valueOf(s);
				add((float) o, name);
				break;
			}
			case DOUBLE: {
				o = Double.valueOf(s);
				add((double) o, name);
				break;
			}
			case SHORT: {
				o = Short.valueOf(s);
				add((short) o, name);
				break;
			}
			case LONG: {
				o = Long.valueOf(s);
				add((long) o, name);
				break;
			}
			case CHAR: {
				if(s.length() > 0) {
					o = s.charAt(0);
					add((char) o, name);
				}
				break;
			}

		}


	}

	public void readFromStream(InputStream is,boolean clear) throws IOException {
		if(clear) {
			ObjectList.clear();
			ObjectNames.clear();
			ObjectType.clear();
		}

		DataInputStream dis = new DataInputStream(is);

		String name;
		Types type;
		String s;
		FileWriter fw;

		while(dis.readBoolean()) {
			name = dis.readUTF();
			type = Types.values()[dis.readInt()];

			if(type!=Types.ISAVEABLE) {
				s = dis.readUTF();
				toObject(name,type,s);
			}else{
				fw = new FileWriter();
				fw.readFromStream(dis, true);
				add(fw, name);
			}
		}
	}

	public void readFromFile(File f) throws IOException {
		InputStream is = new FileInputStream(f);
		readFromStream(is,true);
	}

	/**
	 * @param os the OutputStream to be written to
	 *
	 * @return if the execution was successful
	 *
	 * @throws IOException is thrown when the writing of one Object to Steam fails
	 */
	public void writeToStream(OutputStream os) throws IOException {

		DataOutputStream dos = new DataOutputStream(os);

		for(int i = 0; i < ObjectList.size(); i++) {
			dos.writeBoolean(true);
			writeObjectToStream(ObjectList.get(i), ObjectType.get(i), ObjectNames.get(i), dos);
		}

		dos.writeBoolean(false);

		dos.flush();

	}

	public void writeToFile(File f) throws IOException {
		OutputStream os = new FileOutputStream(f);
		writeToStream(os);
	}

	/**
	 * @param obj  the object to be written
	 * @param t    the type of the Object
	 * @param name the name of the Object
	 * @param os   the OutputStream to be written to
	 *
	 * @return if the operation was successful
	 */
	@SuppressWarnings("StringConcatenationMissingWhitespace")
	private void writeObjectToStream(Object obj, Types t, String name, DataOutputStream os) throws IOException {
		//Write Name

		os.writeUTF(name);
		//Write Type
		os.writeInt(t.ordinal());

		//Write Value
		switch(t) {
			case ISAVEABLE: {
				ISaveAble is = (ISaveAble) obj;
				FileWriter fw = new FileWriter();
				is.writeToFileWriter(fw);
				fw.writeToStream(os);
				break;
			}
			default: {
				os.writeUTF(String.valueOf(obj));
				break;
			}
		}
	}

	/**
	 * @param i    the Integer to be added
	 * @param name the Name of the Integer
	 */
	public void add(int i, String name) {

		ObjectList.add(i);
		ObjectNames.add(name);
		ObjectType.add(Types.INT);
	}

	/**
	 * @param i    the String to be added
	 * @param name the name of the String
	 */
	public void add(String i, String name) {

		ObjectList.add(i);
		ObjectNames.add(name);
		ObjectType.add(Types.STRING);
	}

	/**
	 * @param i    the ISaveAble to be added
	 * @param name the name of the ISaveAble
	 */
	public FileWriter add(ISaveAble i, String name) {

		FileWriter fw;
		ObjectList.add(fw = new FileWriter());
		ObjectNames.add(name);
		ObjectType.add(Types.ISAVEABLE);
		i.writeToFileWriter(fw);
		return fw;
	}

	/**
	 * @param i    the byte to be added
	 * @param name the name of the byte
	 */
	public void add(byte i, String name) {

		ObjectList.add(i);
		ObjectNames.add(name);
		ObjectType.add(Types.BYTE);
	}

	/**
	 * @param i    the boolean to be added
	 * @param name the name of the boolean
	 */
	public void add(boolean i, String name) {

		ObjectList.add(i);
		ObjectNames.add(name);
		ObjectType.add(Types.BOOLEAN);
	}

	/**
	 * @param i    the float to be added
	 * @param name the name of the float
	 */
	public void add(float i, String name) {

		ObjectList.add(i);
		ObjectNames.add(name);
		ObjectType.add(Types.FLOAT);
	}

	/**
	 * @param i    the double to be added
	 * @param name the name of the double
	 */
	public void add(double i, String name) {

		ObjectList.add(i);
		ObjectNames.add(name);
		ObjectType.add(Types.DOUBLE);
	}

	/**
	 * @param i    the short to be added
	 * @param name the name of the short
	 */
	public void add(short i, String name) {

		ObjectList.add(i);
		ObjectNames.add(name);
		ObjectType.add(Types.SHORT);
	}

	/**
	 * @param i    the long to be added
	 * @param name the name of the long
	 */
	public void add(long i, String name) {

		ObjectList.add(i);
		ObjectNames.add(name);
		ObjectType.add(Types.LONG);
	}

	/**
	 * @param i    the chat to be added
	 * @param name the name of the char
	 */
	public void add(char i, String name) {

		ObjectList.add(i);
		ObjectNames.add(name);
		ObjectType.add(Types.CHAR);
	}

	public Object get(String name) {
		int index;
		if((index = ObjectNames.indexOf(name)) != -1) {
			return ObjectList.get(index);
		}
		return null;
	}

	public Types getObjectType(String name) {
		int index;
		if((index = ObjectNames.indexOf(name)) != -1) {
			return ObjectType.get(index);
		}
		return null;
	}

	public List<String> getObjectNames() {
		List<String> a = new ArrayList<>();
		a.addAll(ObjectNames);
		return a;
	}

	public boolean containsObject(String s) {
		return ObjectNames.contains(s);
	}

	@Override
	public void writeToFileWriter(FileWriter fw) {
		//Clearing Lists
		fw.ObjectList.clear();
		fw.ObjectNames.clear();
		fw.ObjectType.clear();
		//Filling Lists
		fw.ObjectList.addAll(ObjectList);
		fw.ObjectNames.addAll(ObjectNames);
		fw.ObjectType.addAll(ObjectType);
	}

	@Override
	public void loadFromFileWriter(FileWriter fw) {
		//Clearing Lists
		ObjectList.clear();
		ObjectNames.clear();
		ObjectType.clear();
		//Filling Lists
		ObjectList.addAll(fw.ObjectList);
		ObjectNames.addAll(fw.ObjectNames);
		ObjectType.addAll(fw.ObjectType);
	}
}