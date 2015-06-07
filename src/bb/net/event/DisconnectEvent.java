package bb.net.event;

import bb.net.enums.Side;
import bb.net.interfaces.IConnectionEvent;
import bb.net.interfaces.IIOHandler;

/**
 * Created by BB20101997 on 12.04.2015.
 */
public class DisconnectEvent implements IConnectionEvent {

	private IIOHandler iio;
	//the side that disconnected
	private Side       side;

	public DisconnectEvent(IIOHandler io, Side side) {
		iio = io;
		this.side = side;
	}

	@Override
	public IIOHandler getIIOHandler() {
		return iio;
	}

	public Side getSide() {
		return side;
	}
}
