package processing;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Decoder {
	public static void decode(Configuration configuration) {
		BufferedImage bufferedImage = FileReaderWriter.openBitmapFromFile(configuration.getImageFilePath());
		configuration = loadConfigValuesFromBitmap(bufferedImage, configuration);

		long readBits = 0;
		byte rMask = configuration.getRMask();
		byte gMask = configuration.getGMask();
		byte bMask = configuration.getBMask();

		List<Boolean> outputBits = new ArrayList<Boolean>();
		int inputLengthBitsAmount = 80;
		int extensionLengthBitsAmount = 24;
		int additionalDataSize = 40;
		bigLoop: for (int y = 0; y < bufferedImage.getHeight(); y++)
			for (int x = 0; x < bufferedImage.getWidth(); x++) {
				if (y == 0 && x <= 3)
					continue;

				if (readBits >= 40) {
					inputLengthBitsAmount = Common.byteArrayToInt(Common.toByteArray(outputBits.subList(0, Integer.SIZE))) * 8;
					extensionLengthBitsAmount = Common.toByteArray(outputBits.subList(Integer.SIZE, Integer.SIZE + Byte.SIZE))[0] * 8;
					additionalDataSize = (configuration.getAdditionalDataSize() * 8) + extensionLengthBitsAmount;
				}
				int rgb = bufferedImage.getRGB(x, y);

				byte dataInRComponent = getBitsFromRComponent(rgb, rMask);
				for (int r = configuration.getRBitsAmount() - 1; r >= 0; r--)
					outputBits.add(Common.isBitSet(dataInRComponent, r));
				if ((readBits += configuration.getRBitsAmount()) >= inputLengthBitsAmount + additionalDataSize)
					break bigLoop;

				byte dataInGComponent = getBitsFromGComponent(rgb, gMask);
				for (int g = configuration.getGBitsAmount() - 1; g >= 0; g--)
					outputBits.add(Common.isBitSet(dataInGComponent, g));
				if ((readBits += configuration.getGBitsAmount()) >= inputLengthBitsAmount + additionalDataSize)
					break bigLoop;

				byte dataInBComponent = getBitsFromBComponent(rgb, bMask);
				for (int b = configuration.getBBitsAmount() - 1; b >= 0; b--)
					outputBits.add(Common.isBitSet(dataInBComponent, b));
				if ((readBits += configuration.getBBitsAmount()) >= inputLengthBitsAmount + additionalDataSize)
					break bigLoop;
			}

		configuration.setDecodedExtension(Common.toByteArray(outputBits.subList(Integer.SIZE + Byte.SIZE, Integer.SIZE + Byte.SIZE + extensionLengthBitsAmount)));
		
		outputBits = outputBits.subList(additionalDataSize, inputLengthBitsAmount + additionalDataSize);
		FileReaderWriter.saveDecodedData(outputBits, configuration);
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

		if (bits >= 0 && bits < 9)
			return bits;
		else
			throw new RuntimeException();
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
