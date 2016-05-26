package bb.util.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by BB20101997 on 12.04.2015.
 */
public abstract class EventHandler<E extends IEvent> {

	//name of the function that Handles the Event
	private final String funcName;

	public EventHandler() {
		//use default name for handeling function
		this("handleEvent");
	}

	public EventHandler(String s) {
		if(!"".equals(s)) {
			funcName = s;
		} else {
			throw new IllegalArgumentException("Empty String at EventHandler Constructor!");
		}
	}

	public void HandleEvent(E event) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Method method = this.getClass().getDeclaredMethod(funcName, event.getClass());
		method.invoke(this, event);
	}

}
