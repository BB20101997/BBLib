package bb.util.database;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.swing.JFileChooser;

import bb.util.file.FileChooser;

public class FileWriter {
	
	public static void main(String[] tArgs){
	
		
		File file  = FileChooser.chooseFile(null,"Open", JFileChooser.FILES_ONLY);
		
	try(Writer out = new BufferedWriter(new  OutputStreamWriter(new FileOutputStream(file)));)
	{
	out.append("test");
	out.flush();
	}
	catch(Exception e){
		e.printStackTrace();
	}
	}
	
	}
