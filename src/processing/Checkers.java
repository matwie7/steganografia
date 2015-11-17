package processing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.security.InvalidParameterException;

public class Checkers {

	public static boolean isDataEncoded(Configuration configuration) {
		boolean isEncoded = false;
		try {
			Decoder.loadConfigValuesFromBitmap(FileReaderWriter.openBitmapFromFile(configuration.getImageFilePath()),
					configuration);
			isEncoded = true;
		} catch (Exception e) {
		}
		return isEncoded && configuration.isVerificationBitCorrect();
	}

	public static int getTotalFreeBitsInBitmap(BufferedImage bufferedImage, Configuration configuration) {
		return bufferedImage.getHeight() * bufferedImage.getWidth() * configuration.getBitsPerPixel();
	}

	public static int getTotalFreeBytesInBitmap(BufferedImage bufferedImage, Configuration configuration) {
		return getTotalFreeBitsInBitmap(bufferedImage, configuration) / 8;
	}

	public static float getTotalFreeBytesInBitmap(Configuration configuration) {
		return getTotalFreeBitsInBitmap(FileReaderWriter.openBitmapFromFile(configuration.getImageFilePath()),
				configuration) / 8;
	}

	public static int getSizeOfInputFileInBytes(String inputFilePath) {
		if (inputFilePath == null)
			return 0;
		try {
			return (int) new File(inputFilePath).length();
		} catch (InvalidParameterException e) {
			return 0;
		}
	}

	public boolean isPossibleToHideInputFileInBitmap(BufferedImage bufferedImage, String inputFilePath,
			Configuration configuration) {
		return getTotalFreeBitsInBitmap(bufferedImage, configuration) >= getSizeOfInputFileInBytes(inputFilePath) * 8;
	}
}
