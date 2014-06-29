package bb.util.file.database;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import bb.util.exceptions.WrongVersionException;

/**
 * @author BB20101997
 */
@SuppressWarnings("javadoc")
public final class FileWriter
{

	/**
	 * The Vesion of the FileWriter
	 */
	private final float			VERSION					= 0.01f;

	// kann bis mindestens F8FF fortgeführt werden

	public static final char	splitCharacter			= '\uF8F0';
	public static final char	typeSeperator			= '\uF8F5';

	public static final char	innerFileWriterBeginn	= '\uF8F7';
	public static final char	innerFileWriterEnd		= '\uF8F8';

	public static final char	beginOfString			= '\uF8F1';
	public static final char	endOfString				= '\uF8F2';

	public static final char	escapeCaracter			= '\uF8F3';

	public static final char	endOfFileWriter			= '\uF8F4';

	public static final String	VersionName				= "\uF8F6" + "VERSION";

	/**
	 * @author BB20101997
	 */
	private enum Types
	{
		INT, STRING, ISAVEABLE, BYTE, BOOLEAN, FLOAT, DOUBLE, SHORT, LONG, CHAR

	}

	public FileWriter()
	{

		add(VERSION, VersionName);
	}

	/**
	 * A list of Objects , to be saved or to be read
	 */
	private List<Object>	ObjectList	= new ArrayList<Object>();
	/**
	 * The names of the Objects for easy retrieval
	 */
	private List<String>	ObjectNames	= new ArrayList<String>();
	/**
	 * The Type of Object saved in the ObjectList
	 */
	private List<Types>		ObjectType	= new ArrayList<Types>();

	private Object toObject(String name, String type, String s)
	{

		return null;
	}

	private Object toObject(String name, String type, FileWriter fw)
	{

		return null;
	}

	/**
	 * @param is
	 *            the Inputstream to read the Objects from
	 * @return if reading was successful
	 */
	public boolean readFromStream(InputStream is) throws WrongVersionException
	{

		InputStreamReader ISR = new InputStreamReader(is);

		char c;
		boolean lastEscape = false;

		int i;

		// 0 if at the Name,1 if Type,2 if value
		int postition = 0;
		String name = "";
		String type = "";
		String s = "";
		boolean inString = false;
		FileWriter fw = null;
		try
		{
			wloop:
			while((i = ISR.read()) != -1)
			{
				c = (char) i;
				if(!inString)
				{

					if((c == endOfFileWriter))
					{
						break wloop;
					}

					if(c == splitCharacter)
					{
						if(type.equals(String.valueOf(Types.ISAVEABLE.ordinal())))
						{
							toObject(name, type, fw);
						}
						else
						{
							toObject(name, type, s);
						}
						postition = 0;
					}

					if(c == innerFileWriterBeginn)
					{
						fw = new FileWriter();
						fw.readFromStream(is);
					}

					if(c == typeSeperator)
					{

						switch(postition){
							case 0 : {
								name = s;
								break;
							}
							case 1 : {
								type = s;
								break;
							}
						}
						s = "";
						postition++;
					}

				}

				if((c == endOfString) && !lastEscape)
				{
					inString = false;
					lastEscape = false;
				}

				if(inString && ((c != escapeCaracter) && !lastEscape))
				{
					s = s + c;
				}

				if(c == beginOfString)
				{
					inString = true;
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param os
	 *            the Outputstream to be written to
	 * @return if the execution was successful
	 * @throws IOException
	 *             is thrown when the writing of one Objet to Steam fails
	 */
	public boolean writeToStream(OutputStream os) throws IOException
	{

		boolean noFail = true;
		for(int i = 0; i < ObjectList.size(); i++)
		{
			if(!writeObjectToStream(ObjectList.get(i), ObjectType.get(i), ObjectNames.get(i), os))
			{
				noFail = false;
				throw new IOException();
			}
		}
		os.write(endOfFileWriter);
		return noFail;
	}

	/**
	 * @param obj
	 *            the object to be written
	 * @param t
	 *            the type of the Object
	 * @param name
	 *            the name of the Object
	 * @param os
	 *            the Outputstream to be written to
	 * @return if the Outprinting was successful
	 */
	private boolean writeObjectToStream(Object obj, Types t, String name, OutputStream os)
	{

		boolean noFail = true;

		try
		{
			os.write((name + typeSeperator + t.ordinal() + typeSeperator).getBytes());

			switch(t){
				case ISAVEABLE : {
					os.write(beginOfString);
					ISaveAble is = (ISaveAble) obj;
					FileWriter fw = new FileWriter();
					is.writeToFileWriter(fw);
					fw.writeToStream(os);
					os.write(endOfString);
					break;
				}
				default : {
					os.write((beginOfString + String.valueOf(obj) + endOfString).getBytes());
					break;
				}
			}
		}
		catch(Exception e)
		{
			noFail = false;
			e.printStackTrace();
		}

		return noFail;
	}

	/**
	 * @param i
	 *            the Integer to be added
	 * @param name
	 *            the Name of the Integer
	 */
	public void add(int i, String name)
	{

		ObjectList.add(i);
		ObjectNames.add(name);
		ObjectType.add(Types.INT);
	}

	/**
	 * @param i
	 *            the String to be added
	 * @param name
	 *            the name of the String
	 */
	public void add(String i, String name)
	{

		ObjectList.add(i);
		ObjectNames.add(name);
		ObjectType.add(Types.STRING);
	}

	/**
	 * @param i
	 *            the ISaveAble to be added
	 * @param name
	 *            the name of the ISaveAble
	 */
	public void add(ISaveAble i, String name)
	{

		ObjectList.add(i);
		ObjectNames.add(name);
		ObjectType.add(Types.ISAVEABLE);
	}

	/**
	 * @param i
	 *            the byte to be added
	 * @param name
	 *            the name of the byte
	 */
	public void add(byte i, String name)
	{

		ObjectList.add(i);
		ObjectNames.add(name);
		ObjectType.add(Types.BYTE);
	}

	/**
	 * @param i
	 *            the boolean to be added
	 * @param name
	 *            the name of the boolean
	 */
	public void add(boolean i, String name)
	{

		ObjectList.add(i);
		ObjectNames.add(name);
		ObjectType.add(Types.BOOLEAN);
	}

	/**
	 * @param i
	 *            the float to be added
	 * @param name
	 *            the name of the float
	 */
	public void add(float i, String name)
	{

		ObjectList.add(i);
		ObjectNames.add(name);
		ObjectType.add(Types.FLOAT);
	}

	/**
	 * @param i
	 *            the double to be added
	 * @param name
	 *            the name of the double
	 */
	public void add(double i, String name)
	{

		ObjectList.add(i);
		ObjectNames.add(name);
		ObjectType.add(Types.DOUBLE);
	}

	/**
	 * @param i
	 *            the short to be added
	 * @param name
	 *            the name of the short
	 */
	public void add(short i, String name)
	{

		ObjectList.add(i);
		ObjectNames.add(name);
		ObjectType.add(Types.SHORT);
	}

	/**
	 * @param i
	 *            the long to be added
	 * @param name
	 *            the name of the long
	 */
	public void add(long i, String name)
	{

		ObjectList.add(i);
		ObjectNames.add(name);
		ObjectType.add(Types.LONG);
	}

	/**
	 * @param i
	 *            the chat to be added
	 * @param name
	 *            the name of the char
	 */
	public void add(char i, String name)
	{

		ObjectList.add(i);
		ObjectNames.add(name);
		ObjectType.add(Types.CHAR);
	}
}
