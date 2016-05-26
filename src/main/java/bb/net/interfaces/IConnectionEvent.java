package bb.net.interfaces;

import bb.util.event.IEvent;

/**
 * Created by BB20101997 on 10.04.2015.
 */
@FunctionalInterface
public interface IConnectionEvent extends IEvent {

	IIOHandler getIIOHandler();

}
