import java.awt.image.BufferedImage;
import java.io.File;

public class Checkers {

	private final static float BITS_TO_KILOBYTES_RATIO = 8192f;

	public static int getTotalFreeBitsInBitmap(BufferedImage bufferedImage, Configuration configuration) {
		return bufferedImage.getHeight() * bufferedImage.getWidth() * configuration.getBitsPerPixel();
	}

	public static float getTotalFreeKilobytesInBitmap(BufferedImage bufferedImage, Configuration configuration) {
		return getTotalFreeBitsInBitmap(bufferedImage, configuration) / BITS_TO_KILOBYTES_RATIO;
	}

	public static long getSizeOfInputFileInBytes(String inputFilePath) {
		return new File(inputFilePath).length();
	}

	public boolean isPossibleToHideInputFileInBitmap(BufferedImage bufferedImage, String inputFilePath,
			Configuration configuration) {
		return getTotalFreeBitsInBitmap(bufferedImage, configuration) >= getSizeOfInputFileInBytes(inputFilePath) * 8;
	}
}
