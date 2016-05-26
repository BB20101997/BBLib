package bb.net.interfaces;

/**
 * Created by BB20101997 on 05.09.2014.
 */
public interface IIOHandler extends Runnable {

	void start();

	void stop();

	boolean isDummy();

	boolean sendPacket(APacket p);

	boolean isAlive();

	void receivedHandshake();

}
