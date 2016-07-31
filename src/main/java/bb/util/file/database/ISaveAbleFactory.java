package bb.util.file.database;

/**
 * Created by BB20101997 on 31. Jul. 2016.
 */
public interface ISaveAbleFactory <E extends ISaveAble> {

	E getNewInstance();

	E[] getNewArrayInstance(int size);
}
