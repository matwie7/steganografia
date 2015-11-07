package processing;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Decoder {
	public static void decode(final String inputFilePath, final String outputFilePath) {
		BufferedImage bufferedImage = FileReaderWriter.openBitmapFromFile(inputFilePath);
		Configuration configuration = loadConfigValuesFromBitmap(bufferedImage, Configuration.create());

		// int inputLengthBytes =
		// Common.getEncodedDataLengthFromImage(bufferedImage, configuration);
		// int inputLengthBits = inputLengthBytes * 8;
		long readBits = 0;
		byte rMask = configuration.getRMask();
		byte gMask = configuration.getGMask();
		byte bMask = configuration.getBMask();

		List<Boolean> outputBits = new ArrayList<Boolean>();
		int inputLengthBits = 80;
		int additionalDataSize = 40;
		bigLoop: for (int y = 0; y < bufferedImage.getHeight(); y++)
			for (int x = 0; x < bufferedImage.getWidth(); x++) {
				if (y == 0 && x <= 3)
					continue;

				if (readBits >= Integer.SIZE){
					inputLengthBits = Common.byteArrayToInt(Common.toByteArray(outputBits.subList(0, Integer.SIZE))) * 8;
					additionalDataSize = configuration.getAdditionalDataSize() * 8;
				}
				int rgb = bufferedImage.getRGB(x, y);

				byte dataInRComponent = getBitsFromRComponent(rgb, rMask);
				for (int r = configuration.getRBitsAmount() - 1; r >= 0; r--)
					outputBits.add(Common.isBitSet(dataInRComponent, r));
				if ((readBits += configuration.getRBitsAmount()) >= inputLengthBits + additionalDataSize)
					break bigLoop;

				byte dataInGComponent = getBitsFromGComponent(rgb, gMask);
				for (int g = configuration.getGBitsAmount() - 1; g >= 0; g--)
					outputBits.add(Common.isBitSet(dataInGComponent, g));
				if ((readBits += configuration.getGBitsAmount()) >= inputLengthBits + additionalDataSize)
					break bigLoop;

				byte dataInBComponent = getBitsFromBComponent(rgb, bMask);
				for (int b = configuration.getBBitsAmount() - 1; b >= 0; b--)
					outputBits.add(Common.isBitSet(dataInBComponent, b));
				if ((readBits += configuration.getBBitsAmount()) >= inputLengthBits + additionalDataSize)
					break bigLoop;
			}

		outputBits = outputBits.subList(additionalDataSize, inputLengthBits + additionalDataSize);
		FileReaderWriter.saveDecodedData(outputFilePath, outputBits);
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

		bits += getBitsFromRComponent(bufferedImage.getRGB(pixelNumber, 0), (byte) 3);// 011
		bits = (byte) (bits << 3);
		bits += getBitsFromGComponent(bufferedImage.getRGB(pixelNumber, 0), (byte) 7);// 111
		bits = (byte) (bits << 3);
		bits += getBitsFromBComponent(bufferedImage.getRGB(pixelNumber, 0), (byte) 7);// 111

		return bits;
	}

	private static byte getBitsFromRComponent(int pixel, byte mask) {
		return (byte) (mask & getByteFromRGB(pixel, 'R'));
	}

	private static byte getBitsFromGComponent(int pixel, byte mask) {
		return (byte) (mask & getByteFromRGB(pixel, 'G'));
	}

	private static byte getBitsFromBComponent(int pixel, byte mask) {
		return (byte) (mask & getByteFromRGB(pixel, 'B'));
	}

	private static byte getByteFromRGB(int pixel, char component) {
		switch (component) {
		case 'R':
			return Common.intToByteArray(pixel)[1];
		case 'G':
			return Common.intToByteArray(pixel)[2];
		case 'B':
			return Common.intToByteArray(pixel)[3];
		default:
			throw new RuntimeException("Invalid component name");
		}
	}
}
