package bb.util.file;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Created by BB20101997 on 06.06.2015.
 */
public class BBLogHandler extends Handler {

	public boolean open = true;
	private FileWriter fw;
	private boolean toStream = false, toFile = false, toConsole = false;
	private OutputStream outStream;

	public BBLogHandler(File f) {
		try {
			if(!f.exists()) {
				//noinspection ResultOfMethodCallIgnored
				f.getParentFile().mkdirs();
				//noinspection ResultOfMethodCallIgnored
				f.createNewFile();
			}
			fw = new FileWriter(f, true);
			toFile = true;
		} catch(IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void publish(LogRecord record) {
		if(open) {
			Level l = record.getLevel();
			String loggerName = record.getLoggerName();
			long mills = record.getMillis();
			String message = record.getMessage();
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(record.getMillis());
			String time = new SimpleDateFormat("dd.HH.yyyy-HH:mm:ss:SSS").format(c.getTime());

			String[] lines = message.split("\n");
			StringBuilder sb;
			int i = 1;
			for(String s : lines) {
				sb = new StringBuilder();
				sb.append("[").append(time).append("]\u0009");
				sb.append("[").append(record.getLevel()).append("]\u0009");
				sb.append("[").append(loggerName).append("]\u0009");
				sb.append("[Line:").append(String.format("%03d", i)).append("/").append(String.format("%03d", lines.length)).append("]\u0009");
				sb.append(s);
				sb.append("\n");
				if(toFile) {
					try {
						fw.append(sb.toString());
						fw.flush();
					} catch(IOException e) {
						e.printStackTrace();
						//log(LogType.ERROR, "Log", "IOException while writing to Log!"); //<_may cause loop!?
					}
				}
				if(toConsole) {
					System.out.append(sb.toString());
					System.out.flush();
				}
				if(toStream){
					try {
						outStream.write(sb.toString().getBytes());
					} catch(IOException e) {
						e.printStackTrace();
					}
				}
				i++;
			}

		}
	}

	@Override
	public void flush() {
		if(open) {
			if(toFile) {
				try {
					fw.flush();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
			if(toConsole) {
				System.out.flush();
			}

			if(toStream) {
				try {
					outStream.flush();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	@Override
	public void close() throws SecurityException {
		open = false;
		if(outStream!=null){
			try {
				outStream.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		if(fw!=null){
			try {
				fw.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
