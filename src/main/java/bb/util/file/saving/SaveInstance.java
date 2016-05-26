package bb.util.file.saving;

import java.io.File;

/**
 * Created by BB20101997 on 08. Apr. 2016.
 */
public interface SaveInstance {

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
