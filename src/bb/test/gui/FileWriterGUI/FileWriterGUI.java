package bb.test.gui.FileWriterGUI;

import bb.util.file.database.FileWriter;
import bb.util.file.database.ISaveAble;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.io.*;

/**
 * Created by BB20101997 on 20.11.2014.
 */
public class FileWriterGUI extends JFrame {

	public static void main(String[] tArgs) {
		FileWriter FW = new FileWriter();
		File f = new File("B://Test/FileWriterTest.fw");

		FW.add(true,"Boolean");
		FW.add("Test String","String");
		FileWriter fw = FW.add(new ISaveAble() {
			@Override
			public void writeToFileWriter(FileWriter fw) {

			}

			@Override
			public void loadFromFileWriter(FileWriter fw) {

			}
		},"FileWriter");

		fw.add("Test inner FileWriter","IFW");

		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}

		try {
			OutputStream os = new FileOutputStream(f);
			InputStream is = new FileInputStream(f);
			FW.writeToStream(os);
			FW.readFromStream(is);
		} catch(IOException e) {
			e.printStackTrace();
		}

		new FileWriterGUI().setFileWriter(FW);

	}

	public FileWriter FW;

	public ListModel<String> listModel = new ListModel<String>() {

		@Override
		public int getSize() {
			if(FW == null) {
				return 0;
			}
			return FW.getObjectNames().size();
		}

		@Override
		public String getElementAt(int index) {
			if(getSize() <= 0 || getSize() <= index) {
				return "Ups,not that many Entries!";
			}
			return FW.getObjectNames().get(index);
		}

		@Override
		public void addListDataListener(ListDataListener l) {

		}

		@Override
		public void removeListDataListener(ListDataListener l) {

		}
	};

	public FileWriterGUI() {
		setup();
	}

	private void setup() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		add(new JList(listModel));
		setVisible(true);
	}

	public FileWriterGUI(FileWriter fw) {
		this();
		setFileWriter(fw);
	}

	public void setFileWriter(FileWriter fw) {
		FW = fw;
		updateStruckture();
	}

	private void updateStruckture() {}

}
