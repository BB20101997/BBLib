package bb.net.handler;

import bb.net.interfaces.IConnectionEvent;
import bb.net.interfaces.IConnectionEventHandler;
import bb.util.event.EventHandler;

/**
 * Created by BB20101997 on 10.04.2015.
 */
public abstract class AIConnectionEventHandler extends EventHandler<IConnectionEvent> implements IConnectionEventHandler {

	public AIConnectionEventHandler() {
		super();
	}

}
