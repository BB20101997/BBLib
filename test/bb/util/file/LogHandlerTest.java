package bb.util.file;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by BB20101997 on 06.06.2015.
 */
public class LogHandlerTest {

	Logger l;

	@Before
	public void setUp() throws Exception {
		File f = new File("/log/"+new SimpleDateFormat("dd-MM-yyyy").format(new Date())+"-log.txt");
		l = Logger.getLogger("bb.util.file.LogHandlerTest");
		l.addHandler(new LogHandler("LogHandlerTest",f.getCanonicalFile()));
	}

	@Test
	public void testPublish() throws Exception {
		l.info("Test Message");
		StringBuilder sb = new StringBuilder();
		for(int i= 1;i<=10;i++){
			sb.append("Test Line ").append(i).append("!");
			if(i!=100){
			sb.append("\n");
		}}
		l.info(sb.toString());
	}
}