package bb.util.file.database;

public interface ISaveAble {
	//Beware don´t save an ISaveAble inside itself, will cause endless loop!!!
	public abstract void writeToFileWriter(FileWriter fw);
	
	public abstract void loadFromFileWriter(FileWriter fw);

}
