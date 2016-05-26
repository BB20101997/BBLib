package bb.util.file.saving;

import bb.util.file.FileChooser;

import javax.swing.*;
import java.io.File;
import java.nio.file.attribute.FileTime;

/**
 * Created by BB20101997 on 28.07.2015.
 */
public class FileSaving {

	private File      open;
	private FileTime  lastSaving;
	private JMenuItem newFile, save, saveAs, load;
	private SaveInstance instance;

	public FileSaving(SaveInstance ins) {
		instance = ins;
		initMenuItems();
	}

	private void initMenuItems(){
		//init New
		newFile = new JMenuItem("New");
		newFile.addActionListener(e -> executeNew());

		//init Save
		save = new JMenuItem("Save");
		save.addActionListener(e -> executeSave());

		//init SaveAs
		saveAs = new JMenuItem("SaveAs");
		saveAs.addActionListener(e -> executeSaveAs());
	}

	//returns the MenuItem for performing a
	public JMenuItem getNew() {
		return newFile;
	}

	public JMenuItem getSave(){
		return save;
	}

	public JMenuItem getSaveAs() {
		return saveAs;
	}

	@SuppressWarnings("WeakerAccess")
	public void executeNew() {
		if(instance.hasChanged()) {
			int i = JOptionPane.showInternalConfirmDialog(newFile, "Do you want to save your changes before starting a New one?", "Do you consider saving?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			if(i != JOptionPane.CANCEL_OPTION) {
				if(i == JOptionPane.YES_OPTION) {
					if(open != null) {
						open = FileChooser.chooseFile(null, "Save", JFileChooser.FILES_ONLY);
						if(open == null) {
							JOptionPane.showConfirmDialog(newFile, "Something went wrong while saving!(ERR:1)\nAborting saving and creating a New File!");
							return;
						}
					}
					if(instance.saveToFile(open)) {
						JOptionPane.showConfirmDialog(newFile, "Something went wrong while saving!(ERR:2)\nAborting Creating a New File!\nIf you tried overriding your last save it might be corrupted!");
						return;
					}
				}
				instance.resetChanged();
				instance.resetData();
			}
		}
	}

	@SuppressWarnings("WeakerAccess")
	public void executeSave() {
		if(instance.hasChanged()) {
			if(open == null) {
				open = FileChooser.chooseFile(null, "Save", JFileChooser.FILES_ONLY);
				if(open == null) {
					JOptionPane.showConfirmDialog(newFile, "Something went wrong while saving!(ERR:3)\nAborting saving!");
					return;
				}
			}
			if(!instance.saveToFile(open)) {
				JOptionPane.showConfirmDialog(newFile, "Something went wrong while saving!(ERR:4)\nAborting Creating a New File!\nIf you tried overriding your last save it might be corrupted!");
			}
		}
	}

	@SuppressWarnings("WeakerAccess")
	public void executeSaveAs() {
		open = FileChooser.chooseFile(null, "Save", JFileChooser.FILES_ONLY);
		if(open == null) {
			JOptionPane.showConfirmDialog(newFile, "Something went wrong while saving!(ERR:3)\nAborting saving!");
			return;
		}
		if(!instance.saveToFile(open)) {
			JOptionPane.showConfirmDialog(newFile, "Something went wrong while saving!(ERR:4)\nAborting Creating a New File!\nIf you tried overriding your last save it might be corrupted!");
		}
	}

}