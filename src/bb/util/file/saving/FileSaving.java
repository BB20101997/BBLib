package bb.util.file.saving;

import bb.util.file.FileChooser;
import com.sun.istack.internal.NotNull;

import javax.swing.*;
import java.io.File;
import java.nio.file.attribute.FileTime;
import java.util.ResourceBundle;

/**
 * Created by BB20101997 on 28.07.2015.
 */
public class FileSaving {

	File      open;
	FileTime  lastSaving;
	JMenuItem newFile, save, saveAs, load;
	final Instanter          instanter;
	final Instanter.Instance instance;

	public interface Instanter {

		//create a new Instance or some how return an Instance
		Instance createInstance();

		interface Instance {

			//load up the content of the given file
			boolean loadFromFile(File f);

			//save to the given File
			boolean saveToFile(File f);

			//have changes been made since the last resetChanged
			boolean hasChanged();

			//reset that changes have been mad, do't reset the changes
			void resetChanged();

			//reset,new creation of a dokument save what ever
			boolean resetData();
		}
	}


	public FileSaving(@NotNull Instanter i) {
		this(i, i.createInstance());
	}

	public FileSaving(Instanter i, Instanter.Instance ins) {
		instanter = i;
		instance = ins;
	}

	//returns the MenuItem for performing a
	public JMenuItem getMenuItemNew() {
		if(newFile == null) {
			newFile = new JMenuItem("New");
			newFile.addActionListener(e -> executeNew());
		}
		return newFile;
	}

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

	public void executeSave() {
		if(instance.hasChanged()) {
			if(open != null) {
				open = FileChooser.chooseFile(null, "Save", JFileChooser.FILES_ONLY);
				if(open == null) {
					JOptionPane.showConfirmDialog(newFile, "Something went wrong while saving!(ERR:3)\nAborting saving!");
					return;
				}
			}
			if(instance.saveToFile(open)) {
				JOptionPane.showConfirmDialog(newFile, "Something went wrong while saving!(ERR:4)\nAborting Creating a New File!\nIf you tried overriding your last save it might be corrupted!");
				return;
			}
		}
	}

	public void executeSaveAs() {
		if(instance.hasChanged()) {

			open = FileChooser.chooseFile(null, "Save", JFileChooser.FILES_ONLY);
			if(open == null) {
				JOptionPane.showConfirmDialog(newFile, "Something went wrong while saving!(ERR:3)\nAborting saving!");
				return;
			}

			if(instance.saveToFile(open)) {
				JOptionPane.showConfirmDialog(newFile, "Something went wrong while saving!(ERR:4)\nAborting Creating a New File!\nIf you tried overriding your last save it might be corrupted!");
				return;
			}
		}
	}
	{
		ResourceBundle.getBundle("test");
	}

}