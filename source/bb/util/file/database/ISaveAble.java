package bb.util.file.database;

public abstract class ISaveAble {
	
	public abstract void writeToFileWriter(FileWriter fw);
	
	public abstract void loadFromFileWriter(FileWriter fw);

}
