package bb.util.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image
{

	public static BufferedImage loadImage(String s)
	{
		BufferedImage img = null;
		try
		{
			img = ImageIO.read(new File(s));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return img;
	}

}
