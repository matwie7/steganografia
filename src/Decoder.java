import java.awt.image.BufferedImage;
import java.io.DataOutputStream;

public class Decoder {
	public static void decode(final String inputFilePath, final String outputFilePath) {
		BufferedImage bufferedImage = FileReaderWriter.openBitmapFromFile(inputFilePath);
		Configuration configuration = loadConfigValuesFromBitmap(bufferedImage, Configuration.create());
		DataOutputStream dataOutputStream = FileReaderWriter.createDataOutputStream(outputFilePath);
		System.out.println();
	}

	public static Configuration loadConfigValuesFromBitmap(BufferedImage bufferedImage, Configuration configuration) {
		if (bufferedImage.getWidth() >= 3)
			return configuration.setRBitsAmount(getBitsAmount(bufferedImage, 0))
					.setGBitsAmount(getBitsAmount(bufferedImage, 1)).setBBitsAmount(getBitsAmount(bufferedImage, 2));
		else
			throw new RuntimeException("Invalid image size");
	}

	private static byte getBitsAmount(BufferedImage bufferedImage, int pixelNumber) {
		byte bits = 0;

		bits += getBitsFromRComponent(bufferedImage, pixelNumber);
		bits = (byte) (bits << 3);
		bits += getBitsFromGComponent(bufferedImage, pixelNumber);
		bits = (byte) (bits << 3);
		bits += getBitsFromBComponent(bufferedImage, pixelNumber);

		return bits;
	}

	private static byte getBitsFromRComponent(BufferedImage bufferedImage, int pixelNumber) {
		return (byte) (0b00000011 & getByteFromRGB(bufferedImage, 'R', pixelNumber));
	}

	private static byte getBitsFromGComponent(BufferedImage bufferedImage, int pixelNumber) {
		return (byte) (0b00000111 & getByteFromRGB(bufferedImage, 'G', pixelNumber));
	}

	private static byte getBitsFromBComponent(BufferedImage bufferedImage, int pixelNumber) {
		return (byte) (0b00000111 & getByteFromRGB(bufferedImage, 'B', pixelNumber));
	}

	private static byte getByteFromRGB(BufferedImage bufferedImage, char component, int pixelNumber) {
		switch (component) {
		case 'R':
			return Common.intToByteArray(bufferedImage.getRGB(pixelNumber, 0))[1];
		case 'G':
			return Common.intToByteArray(bufferedImage.getRGB(pixelNumber, 0))[2];
		case 'B':
			return Common.intToByteArray(bufferedImage.getRGB(pixelNumber, 0))[3];
		default:
			throw new RuntimeException("Invalid component name");
		}
	}
}
