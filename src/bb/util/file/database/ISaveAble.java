package bb.util.file.database;

public interface ISaveAble {
	//Beware don´t save an ISaveAble inside itself, will cause endless loop!!!
	void writeToFileWriter(FileWriter fw);

	void loadFromFileWriter(FileWriter fw);

}
