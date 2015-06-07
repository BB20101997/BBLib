package bb.util.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by BB20101997 on 12.04.2015.
 */
public abstract class EventHandler<E extends IEvent> {

	private final String funcName;

	public EventHandler() {
		funcName = "handleEvent";
	}

	public EventHandler(String s) {
		if(!"".equals(s)) {
			funcName = s;
		} else {
			throw new IllegalArgumentException("Empty String at EventHandler Constructor!");
		}
	}

	public void HandleEvent(E iicev) {
		HandleEvent(this, funcName, iicev);
	}

	public static <V extends IEvent> void HandleEvent(Object instance, String funcName, V iicev) {
		try {
			Method method = instance.getClass().getDeclaredMethod(funcName, iicev.getClass());
			method.invoke(instance, iicev);
		} catch(NoSuchMethodException e) {
			System.err.println("The IIConnectionHandlerEventHandler " + instance.getClass() + " didn't support the event " + iicev.getClass());
			System.err.println("Probably not a bug just an Event not handled by this Handler!");
		} catch(InvocationTargetException e) {
			System.err.println("The IIConnectionHandlerEventHandler " + instance.getClass() + " didn't fail handling the event " + iicev.getClass());
			System.err.println("All Exceptions should be handled internally and shouldn't land here!This is therefor a bug,pleas contact the developer! ");
			e.printStackTrace();
		} catch(IllegalAccessException e) {
			System.err.println("The IIConnectionHandlerEventHandler " + instance.getClass() + " was not accessible for the event " + iicev.getClass());
			System.err.println("Please check if the access is public!Inform the developer this is most definitely a bug!");
		}
	}

}
