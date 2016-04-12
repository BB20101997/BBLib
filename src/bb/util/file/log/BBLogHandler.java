package bb.util.file.log;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Created by BB20101997 on 06.06.2015.
 */
public class BBLogHandler extends Handler {

	private final static Logger log;
	static {
		log = Logger.getLogger(BBLogHandler.class.getName());
		log.addHandler(new BBLogHandler(Constants.getBBLibLogFile()));
	}
	public boolean open = true,closing = false;
	//java's native FileWriter not bb.file.database.FileWriter
	private FileWriter fw;
	private boolean toStream = false, toFile = false, toConsole = false;
	private OutputStream outStream;

	public BBLogHandler(File f) {
		log.finer("Creating new BBLogHandlerInstance");
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

	public void setToConsole(boolean b){
		toConsole = b;
	}

	public void setOutStream(OutputStream os){
		toStream = os!=null;
		outStream = os;
	}


	@Override
	public void publish(LogRecord record) {
		if(open&&!closing) {
			Level l = record.getLevel();
			String loggerName = record.getLoggerName();
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
				sb.append("[").append(l).append("]\u0009");
				sb.append("[").append(loggerName).append("]\u0009");
				sb.append("[Line:").append(String.format("%03d", i)).append("/").append(String.format("%03d", lines.length)).append("]\u0009");
				sb.append(s);
				sb.append("\n");
				doLog(sb.toString());
				i++;
			}

		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		close();
	}

	private void doLog(String msg){
		if(toConsole) {
			System.out.append(msg);
			System.out.flush();
		}
		if(toFile) {
			try {
				fw.append(msg);
				fw.flush();
			} catch(IOException e) {
				e.printStackTrace();
				log.log(Level.SEVERE, "IOException while writing to LogFile!"); //<_may cause loop!?
			}
		}
		if(toStream) {
			try {
				outStream.write(msg.getBytes());
			} catch(IOException e) {
				e.printStackTrace();
				log.log(Level.SEVERE, "IOException while writing to LogStream!"); //<_may cause loop!?
			}
		}
	}

	@Override
	public void flush() {
		if(open) {
			if(toConsole) {
				System.out.flush();
			}
			if(toFile) {
				try {
					fw.flush();
				} catch(IOException e) {
					e.printStackTrace();
				}
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
		closing = true;
		flush();
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
