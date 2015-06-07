package bb.util.file;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by BB20101997 on 06.06.2015.
 */
public class LogHandler extends Handler {

	public boolean open = true;

	@Override
	public void publish(LogRecord record) {
		if(open){

		}
	}

	@Override
	public void flush() {
		if(open){

		}
	}

	@Override
	public void close() throws SecurityException {
		open = false;
	}
}
