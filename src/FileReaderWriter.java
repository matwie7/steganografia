import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;

import javax.imageio.ImageIO;

public class FileReaderWriter {
	public static BufferedImage openBitmapFromFile(final String bitmapFilePath) {
		BufferedImage bitmap = null;

		try {
			bitmap = ImageIO.read(new File(bitmapFilePath));
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(MessageFormat.format("File under {0} cannot be opened", bitmapFilePath));
		}
		return bitmap;
	}

	public static DataInputStream openFileToHide(final String fileToHidePath) {
		DataInputStream dataInputStream = null;
		try {
			dataInputStream = new DataInputStream(
					new BufferedInputStream(new FileInputStream(new File(fileToHidePath))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(MessageFormat.format("File under {0} cannot be found", fileToHidePath));
		}
		return dataInputStream;
	}
}
