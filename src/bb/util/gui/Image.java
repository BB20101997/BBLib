package bb.util.gui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Image {

	public static BufferedImage loadImage(String s) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(s));
		} catch(IOException e) {
			e.printStackTrace();
		}
		return img;
	}

}
