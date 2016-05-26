package bb.util.file.log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by BB20101997 on 08. Apr. 2016.
 */
@SuppressWarnings("WeakerAccess")
public class Constants {
	public static final String LOG_DATE_FORMAT = "dd-MM-yyyy";
	public static final String LOG_FOLDER      = "logs";
	public static final String LOG_FILE_ENDING = "log";

	public static File getBBLibLogFile(){
		return getLogFile("BBLib");
	}

	public static File getLogFile(String name) {
		return new File(LOG_FOLDER + "/"+ new SimpleDateFormat(LOG_DATE_FORMAT).format(new Date())+"/"+name+"." + LOG_FILE_ENDING).getAbsoluteFile();
	}

}
