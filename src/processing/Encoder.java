package processing;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

public class Encoder {

	public static BufferedImage encode(Configuration configuration) {
		BufferedImage bufferedImage = FileReaderWriter.openBitmapFromFile(configuration.getImageFilePath());
		String inputFilePath = configuration.getInputFilePath();
		String inputFileExtension = configuration.getInputFileExtension();
		DataInputStream dataInputStream = FileReaderWriter.openFileToHide(inputFilePath);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		int inputFileLength = (int) Checkers.getSizeOfInputFileInBytes(inputFilePath);
		byte[] inputArray = new byte[inputFileLength];
		int counter = 0;

		try {
			dataInputStream.readFully(inputArray);
		} catch (IOException e) {
			e.printStackTrace();
		}

		encodeRGBContribution(bufferedImage, configuration);
		byte[] inputFileLengtInBytes = Common.intToByteArray(inputFileLength);

		List<Boolean> validInput = Common.bitSetToValidListOfBoolean(BitSet.valueOf(ArrayUtils
				.addAll(inputFileLengtInBytes, ArrayUtils.addAll(new byte[] { (byte) inputFileExtension.length(), 'U' },
						ArrayUtils.addAll(inputFileExtension.getBytes(), inputArray)))));

		boolean finished = false;

		outterLoop: for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (x <= 3 && y == 0)
					continue;
				int rgb = bufferedImage.getRGB(x, y);

				for (int iR = configuration.getRBitsAmount() - 1; iR >= 0 && !finished; iR--) {
					rgb = setR(rgb, iR, validInput.get(counter++));
					if (validInput.size() - 1 == counter)
						finished = true;

				}
				for (int iG = configuration.getGBitsAmount() - 1; iG >= 0 && !finished; iG--) {
					rgb = setG(rgb, iG, validInput.get(counter++));
					if (validInput.size() - 1 == counter)
						finished = true;
				}
				for (int iB = configuration.getBBitsAmount() - 1; iB >= 0 && !finished; iB--) {
					rgb = setB(rgb, iB, validInput.get(counter++));
					if (validInput.size() - 1 == counter)
						finished = true;
				}
				bufferedImage.setRGB(x, y, rgb);
				if (finished)
					break outterLoop;
			}
		}
		FileReaderWriter.saveImage(bufferedImage, configuration.getImageFilePath());
		return bufferedImage;
	}

	private static void encodeRGBContribution(final BufferedImage bufferedImage, final Configuration configuration) {
		byte[] RGBcontribution = configuration.getRGBContribution();
		for (int i = 0; i < 3; i++) {
			int rgb = bufferedImage.getRGB(i, 0);
			BitSet input = BitSet.valueOf(new byte[] { RGBcontribution[i] });
			List<Boolean> inputWithPadding = new ArrayList<Boolean>();

			for (int j = 7; j >= 0; j--)
				if (input.length() > j)
					inputWithPadding.add(input.get(j));
				else
					inputWithPadding.add(false);

			int counter = 0;
			for (int iR = 1; iR >= 0; iR--)
				rgb = setR(rgb, iR, inputWithPadding.get(counter++));

			for (int iG = 2; iG >= 0; iG--)
				rgb = setG(rgb, iG, inputWithPadding.get(counter++));

			for (int iB = 2; iB >= 0; iB--)
				rgb = setB(rgb, iB, inputWithPadding.get(counter++));
			bufferedImage.setRGB(i, 0, rgb);
		}
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
