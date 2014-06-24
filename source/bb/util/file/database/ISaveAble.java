package bb.util.file.database;

public interface ISaveAble {
	
	public void writeToFileWriter(FileWriter fw);
	
	public void loadFromFileWriter(FileWriter fw);

}
