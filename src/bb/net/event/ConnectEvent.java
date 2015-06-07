package bb.net.event;

import bb.net.interfaces.IConnectionEvent;
import bb.net.interfaces.IIOHandler;

/**
 * Created by BB20101997 on 12.04.2015.
 */
public class ConnectEvent implements IConnectionEvent {

	private IIOHandler iio;

	public ConnectEvent(IIOHandler io){
		iio = io;
	}

	@Override
	public IIOHandler getIIOHandler() {
		return iio;
	}

}
