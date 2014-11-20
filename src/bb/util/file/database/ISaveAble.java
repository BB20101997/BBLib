package bb.util.file.database;

public interface ISaveAble {
	
	public abstract void writeToFileWriter(FileWriter fw);
	
	public abstract void loadFromFileWriter(FileWriter fw);

}
