package processing;

import java.awt.image.BufferedImage;
import java.io.File;

public class Checkers {

	public static boolean isDataEncoded(Configuration configuration) {
		boolean isEncoded = false;
		try {
			Decoder.loadConfigValuesFromBitmap(FileReaderWriter.openBitmapFromFile(configuration.getImageFilePath()),
					configuration);
			isEncoded = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isEncoded && configuration.isVerificationBitCorrect();
	}

	public static int getTotalFreeBitsInBitmap(BufferedImage bufferedImage, Configuration configuration) {
		return bufferedImage.getHeight() * bufferedImage.getWidth() * configuration.getBitsPerPixel();
	}

	public static float getTotalFreeBytesInBitmap(BufferedImage bufferedImage, Configuration configuration) {
		return getTotalFreeBitsInBitmap(bufferedImage, configuration) / 8;
	}

	public static float getTotalFreeBytesInBitmap(Configuration configuration) {
		return getTotalFreeBitsInBitmap(FileReaderWriter.openBitmapFromFile(configuration.getImageFilePath()),
				configuration) / 8;
	}

	public static long getSizeOfInputFileInBytes(String inputFilePath) {
		if (!inputFilePath.equals("") && inputFilePath != null)
			return new File(inputFilePath).length();
		else
			return 0;
	}

	public boolean isPossibleToHideInputFileInBitmap(BufferedImage bufferedImage, String inputFilePath,
			Configuration configuration) {
		return getTotalFreeBitsInBitmap(bufferedImage, configuration) >= getSizeOfInputFileInBytes(inputFilePath) * 8;
	}
}
