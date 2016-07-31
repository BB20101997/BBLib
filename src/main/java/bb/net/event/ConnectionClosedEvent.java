package bb.net.event;

import bb.net.interfaces.IConnectionEvent;
import bb.net.interfaces.IIOHandler;

/**
 * Created by BB20101997 on 03. Jun. 2016.
 */
public class ConnectionClosedEvent implements IConnectionEvent {

	private IIOHandler closed;


	public ConnectionClosedEvent(IIOHandler cl){
		closed = cl;
	}

	@Override
	public IIOHandler getIIOHandler() {
		return closed;
	}
}
