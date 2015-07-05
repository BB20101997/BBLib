package bb.test.gui.FileWriterGUI;

import bb.util.file.database.FileWriter;
import org.junit.Test;

import java.io.File;

/**
 * Created by BB20101997 on 03.07.2015.
 */
public class FileWriterGUITest {

	@Test
	public void testMain() throws Exception {
		File f = new File("test.fw").getAbsoluteFile();
		FileWriter FW = new FileWriter();
		FW.add(58,"Achtundfünfzig");
		FW.add("TestText", "Test");
		FileWriterGUI.main(new String[]{f.getAbsolutePath()});
		FW.writeToFile(f);
		FW.readFromFile(f);

		assert "TestText".equals(FW.get("Test"));
		assert 58 == (int)FW.get("Achtundfünfzig");

	}
}