package bb.test.gui.FileWriterGUI;

import bb.util.file.database.FileWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by BB20101997 on 03.07.2015.
 */
public class FileWriterGUITest {

	File f = new File("test.fw").getAbsoluteFile();

	@Before
	public void setUp(){
		FileWriter FW;
		FW = new FileWriter();
		FW.add("TestText", "Test");
		FW.add(58, "Achtundfünfzig");
		FW.add(true,"bool");
		FW.add(false,"bool2");
		try {
			FW.writeToFile(f);
		} catch(IOException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testInt() throws Exception {
		FileWriter FW = new FileWriter();
		FW.readFromFile(f);
		assert 58 == (int)FW.get("Achtundfünfzig");
	}

	@Test
	public void testString() throws Exception {
		FileWriter FW = new FileWriter();
		FW.readFromFile(f);
		assert "TestText".equals(FW.get("Test"));
	}

	@Test
	public void testBoolean() throws Exception {
		FileWriter FW = new FileWriter();
		FW.readFromFile(f);
		assert (boolean)FW.get("bool");
		assert !(boolean) FW.get("bool2");
	}
}