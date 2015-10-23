import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.BitSet;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.ArrayUtils;

public class Encoder {
	public static void encode(final String bitmapFilePath, final String inputFilePath) {
		//mock
		Configuration configuration = Configuration.create((byte) 2, (byte) 2, (byte) 2);
		BufferedImage bufferedImage = FileReaderWriter.openBitmapFromFile(bitmapFilePath);
		DataInputStream dataInputStream = FileReaderWriter.openFileToHide(inputFilePath);

		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		int counter = 0;
		int inputFileLength = (int) Checkers.getSizeOfInputFileInBytes(inputFilePath);
		byte[] inputArray = new byte[inputFileLength];

		try {
			dataInputStream.readFully(inputArray);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// udzia³ bitów w poszczególnych sk³adowych
		byte[] RGBContribution = new byte[] { configuration.getRBitsAmount(), configuration.getGBitsAmount(),
				configuration.getBBitsAmount() };
		// d³ugoœæ pliku wejœciowego
		byte[] inputFileLengtInBytes = Common.intToByteArray(inputFileLength);

		BitSet input = BitSet.valueOf(ArrayUtils.addAll(RGBContribution,
				// 01010101 - ten znak bêdzie oznaczaæ, ¿e w tym obrazku jest
				// coœ ukryte
				ArrayUtils.addAll(new byte[] { (byte) 0b01010101 },
						ArrayUtils.addAll(inputFileLengtInBytes, inputArray))));

		outerLoop: for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int rgb = bufferedImage.getRGB(x, y);

				for (int iR = 0; iR < configuration.getRBitsAmount(); iR++)
					rgb = setR(rgb, iR, input.get(counter++));

				for (int iG = 0; iG < configuration.getGBitsAmount(); iG++)
					rgb = setG(rgb, iG, input.get(counter++));

				for (int iB = 0; iB < configuration.getBBitsAmount(); iB++)
					rgb = setB(rgb, iB, input.get(counter++));

				bufferedImage.setRGB(x, y, rgb);

				if (input.length() < counter)
					break outerLoop;
			}
		}
		System.out.println(Checkers.getTotalFreeKilobytesInBitmap(FileReaderWriter.openBitmapFromFile(bitmapFilePath),
				configuration));

		FileReaderWriter.saveImage(bufferedImage, bitmapFilePath);
	}

	private static int setR(int rgb, int RBit, boolean inputBit) {
		BitSet bitSet = Common.intToBitSet(rgb);
		bitSet.set(16 + RBit, inputBit);
		return (int) bitSet.toLongArray()[0];
	}

	private static int setG(int rgb, int GBit, boolean inputBit) {
		BitSet bitSet = Common.intToBitSet(rgb);
		bitSet.set(8 + GBit, inputBit);
		return (int) bitSet.toLongArray()[0];
	}

	private static int setB(int rgb, int BBit, boolean inputBit) {
		BitSet bitSet = Common.intToBitSet(rgb);
		bitSet.set(0 + BBit, inputBit);
		return (int) bitSet.toLongArray()[0];
	}
}
