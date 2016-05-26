package bb.util.file;

import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by BB20101997 on 08. Apr. 2016.
 */
public class BBLogHandlerTest {

	private Logger l1,l2;

	@Before
	public void setUp() throws Exception {
		Logger.getLogger("").setLevel(Level.ALL);
		l1 = Logger.getLogger("bb.util.file.BBLogHandlerTest.1");
		l1.addHandler(new BBLogHandler(Constants.getLogFile("Test")));

		l2 = Logger.getLogger("bb.util.file.BBLogHandlerTest.2");
		l2.addHandler(new BBLogHandler(Constants.getLogFile("Test")));
	}

	@Test
	public void testPublish() throws Exception {
		l1.info("Test Message:1");
		l2.info("Test Message:2");
		StringBuilder sb = new StringBuilder();
		for(int i = 1; i <= 10; i++) {
			sb.append("Test Line ").append(i).append("!");
			sb.append("\n");
		}
		l1.info(sb.toString()+1);
		l2.info(sb.toString()+2);
	}
}