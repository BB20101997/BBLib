package bb.test.gui.FileWriterGUI;

import bb.util.file.database.FileWriter;
import bb.util.file.database.ISaveAble;
import bb.util.file.database.ISaveAbleFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by BB20101997 on 03.07.2015.
 */
public class FileWriterGUITest {

	File f = new File("test.fw").getAbsoluteFile();

	public static final int[] testArray = {1,5,23,57,128,-0,42,16,9,33};
	public static int[] testRandomArray = new int[10];

	private class TestClass implements ISaveAble, ISaveAbleFactory<TestClass>{

		public int val;

		TestClass(){
		}

		TestClass(int i){
			val = i;
		}

		@Override
		public void writeToFileWriter(FileWriter fw) {
			fw.add(val,"VAL");
		}

		@Override
		public void loadFromFileWriter(FileWriter fw) {
			val = (int) fw.get("VAL");
		}

		@Override
		public TestClass getNewInstance() {
			return new TestClass();
		}

		@Override
		public TestClass[] getNewArrayInstance(int size) {
			return new TestClass[size];
		}
	}

	@Before
	public void setUp(){
		FileWriter FW;
		FW = new FileWriter();
		FW.add("TestText", "Test");
		FW.add(58, "Achtundfünfzig");
		FW.add(true,"bool");
		FW.add(false,"bool2");
		TestClass[] tests = new TestClass[testArray.length];
		for(int i = 0;i<tests.length;i++){
			tests[i] = new TestClass(testArray[i]);
		}
		FW.add(tests, "arr");

		Random random = new Random();
		tests = new TestClass[testArray.length];

		for(int i = 0;i<testRandomArray.length;i++){
			tests[i] = new TestClass(testRandomArray[i] = random.nextInt());
		}
		FW.add(tests, "arrRand");

		try {
			FW.writeToFile(f);
		} catch(IOException e) {
			e.printStackTrace();
		}

		System.err.println("Written all the Things!");

	}

	@After
	public void cleanUp(){
		f.delete();
	}

	private FileWriter getFileWriter() throws IOException {
		FileWriter fileWriter = new FileWriter();
		fileWriter.readFromFile(f);
		return fileWriter;
	}

	@Test
	public void testInt() throws Exception {
		FileWriter FW = getFileWriter();
		assert 58 == (int)FW.get("Achtundfünfzig");
	}

	@Test
	public void testString() throws Exception {
		FileWriter FW = getFileWriter();
		assert "TestText".equals(FW.get("Test"));
	}

	@Test
	public void testBoolean() throws Exception {
		FileWriter FW = getFileWriter();
		assert (boolean)FW.get("bool");
		assert !(boolean) FW.get("bool2");
	}

	@Test
	public void testArray() throws Exception{
		FileWriter FW = getFileWriter();

		TestClass[] tests = FW.getArray("arr",new TestClass());

		assert tests.length == testArray.length;

		for(int i=0;i<tests.length;i++){
			assert tests[i].val==testArray[i];
		}
	}

	@Test
	public void testRandomArray() throws Exception {
		FileWriter FW = getFileWriter();

		TestClass[] tests = FW.getArray("arrRand", new TestClass());

		assert tests.length == testArray.length;

		for(int i = 0; i < tests.length; i++) {
			assert tests[i].val == testRandomArray[i];
		}
	}
}