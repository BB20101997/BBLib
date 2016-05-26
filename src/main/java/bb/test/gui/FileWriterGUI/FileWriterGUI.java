package bb.test.gui.FileWriterGUI;

import bb.util.file.FileChooser;
import bb.util.file.database.FileWriter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by BB20101997 on 20.11.2014.
 */
class FileWriterGUI extends JPanel implements ListSelectionListener {

	public static void main(String[] tArgs) throws IOException {

		File f = null;

		JFrame frame = new JFrame();

		if(tArgs.length>0&&tArgs[0] != null) {
			f = new File(tArgs[0]).getAbsoluteFile();
		} else {
			f = FileChooser.chooseFile(frame, "Load", JFileChooser.FILES_ONLY);
		}

		FileWriter FW = new FileWriter();

		if(f == null) {
			return;
		}

		if(!f.exists()) {
			try {
				//noinspection ResultOfMethodCallIgnored
				f.createNewFile();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}

		try {
			FW.readFromFile(f);
		} catch(IOException e) {
			e.printStackTrace();
		}

		FileWriterGUI FWG = new FileWriterGUI();
		FWG.setFileWriter(FW);
		FWG.updateStructure();

		frame.add(FWG);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	private FileWriter FW;
	private JList      jList;
	private Box        mainBox;
	private JPanel     displayValue;

	private final ListModel<String> listModel = new DefaultListModel<String>() {

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
	};

	private FileWriterGUI() {
		setup();
	}

	private void setup() {
		mainBox = Box.createHorizontalBox();
		jList = new JList<>(listModel);
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.addListSelectionListener(this);
		displayValue = new JPanel();
		mainBox.add(jList);
		mainBox.add(displayValue);
		add(mainBox);
	}

	private FileWriterGUI(FileWriter fw) {
		this();
		setFileWriter(fw);
	}

	void setFileWriter(FileWriter fw) {
		FW = fw;
		updateStructure();
	}

	private void updateStructure() {
		int i = jList.getSelectedIndex();
		if(i > -1) {
			String name = FW.getObjectNames().get(i);
			FileWriter.Types type = FW.getObjectType(name);

			if(type != null && type == FileWriter.Types.ISAVEABLE) {

				displayValue = new FileWriterGUI((FileWriter) FW.get(name));
				displayValue.setBorder(BorderFactory.createLoweredBevelBorder());

			} else {

				displayValue = new JPanel();
				displayValue.setBorder(BorderFactory.createLoweredBevelBorder());
				JTextField tf = new JTextField(String.valueOf(FW.get(name)));
				displayValue.add(tf);

			}
			mainBox.removeAll();
			mainBox.add(jList);
			mainBox.add(displayValue);
			invalidate();
			repaint();
			revalidate();
			getParent().setSize(getParent().getPreferredSize());
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		updateStructure();
	}
}
