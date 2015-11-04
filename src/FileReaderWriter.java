import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import javax.imageio.ImageIO;

public class FileReaderWriter {
	public static BufferedImage openBitmapFromFile(final String bitmapFilePath) {
		try {
			return ImageIO.read(new File(bitmapFilePath));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(MessageFormat.format("File under {0} cannot be opened", bitmapFilePath));
		}
	}

	public static DataInputStream openFileToHide(final String fileToHidePath) {
		try {
			return new DataInputStream(new BufferedInputStream(new FileInputStream(new File(fileToHidePath))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(MessageFormat.format("File under {0} cannot be found", fileToHidePath));
		}
	}

	public static void saveImage(BufferedImage bufferedImage, String bitmapFilePath) {
		File outputfile = new File(bitmapFilePath + "2");
		try {
			ImageIO.write(bufferedImage, "bmp", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static DataOutputStream createDataOutputStream(String outputFilePath) {
		try {
			return new DataOutputStream(new FileOutputStream(outputFilePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("Output Stream cound not be created");
		}
	}
	
	public static void saveDecodedData(String outputFilePath, List<Boolean> outputDataArray){
		DataOutputStream dataOutputStream = createDataOutputStream(outputFilePath);
		if (outputDataArray.size() % 8 != 0)
			throw new RuntimeException("Wrong data length");
		
		try {
			dataOutputStream.write(Common.toByteArray(outputDataArray));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
