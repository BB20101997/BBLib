package bb.util.file;


import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;


public class FileChooser {
	
	public static File chooseFile(JFrame frame, String Button,int mode){
		
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(mode);
		jfc.setMultiSelectionEnabled(false);
		jfc.setAcceptAllFileFilterUsed(true);
		
		int succes = jfc.showDialog(frame,Button);
		if(succes == JFileChooser.APPROVE_OPTION){
			File f = jfc.getSelectedFile();
			return f;
		}
		return null;
	}
	
	public static File[] chooseFiles(JFrame frame,String Button,int mode){
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(mode);
		jfc.setMultiSelectionEnabled(false);
		jfc.setAcceptAllFileFilterUsed(true);
		
		int succes = jfc.showDialog(frame,Button);
		if(succes == JFileChooser.APPROVE_OPTION){
			File[] f = jfc.getSelectedFiles();
			return f;
		}
		return null;
	}
	
}
