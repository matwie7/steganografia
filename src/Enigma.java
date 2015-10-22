import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.BitSet;

import javax.imageio.ImageIO;

public class Enigma {
	public static void enigmate(final String bitmapFilePath, final String fileToHidePath) {
		Configuration configuration = Configuration.create(2, 2, 2);
		BufferedImage bufferedImage = FileReaderWriter.openBitmapFromFile(bitmapFilePath);
		DataInputStream dataInputStream = FileReaderWriter.openFileToHide(fileToHidePath);

		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		int counter = 0;
		byte[] aaaa = new byte[(int) Checkers.getSizeOfInputFileInBytes(fileToHidePath)];

		try {
			dataInputStream.readFully(aaaa);
		} catch (IOException e) {
			e.printStackTrace();
		}

		BitSet input = BitSet.valueOf(aaaa);
		System.out.println(input.toString());

		outerLoop: for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int rgb = bufferedImage.getRGB(x, y);

				String aaa = Integer.toBinaryString(rgb);
				System.out.println("pixel before: " + aaa);

				for (int iR = 0; iR < configuration.getRBitsAmount(); iR++)
					rgb = setR(rgb, iR, input.get(counter++));

				for (int iG = 0; iG < configuration.getGBitsAmount(); iG++)
					rgb = setG(rgb, iG, input.get(counter++));

				for (int iB = 0; iB < configuration.getBBitsAmount(); iB++)
					rgb = setB(rgb, iB, input.get(counter++));

				aaa = Integer.toBinaryString(rgb);
				System.out.println("pixel after : " + aaa);
				System.out.println(counter);

				bufferedImage.setRGB(x, y, rgb);

				if (input.length() < counter)
					break outerLoop;
			}
		}
		System.out.println(Checkers.getTotalFreeKilobytesInBitmap(FileReaderWriter.openBitmapFromFile(bitmapFilePath),
				configuration));

		File outputfile = new File(bitmapFilePath + "2");
		try {
			ImageIO.write(bufferedImage, "bmp", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(Checkers.getSizeOfInputFileInBytes(fileToHidePath));
		System.out.println();
	}

	public static void unenigmate(final String inputFilePath, final String outputFilePath) {

	}

	private static int setR(int rgb, int RBit, boolean inputBit) {
		BitSet bitSet = new BitSet();
		bitSet = BitSet.valueOf(new long[] { rgb });
		bitSet.set(16 + RBit, inputBit);
		return (int) bitSet.toLongArray()[0];
	}

	private static int setG(int rgb, int GBit, boolean inputBit) {
		BitSet bitSet = new BitSet();
		bitSet = BitSet.valueOf(new long[] { rgb });
		bitSet.set(8 + GBit, inputBit);
		return (int) bitSet.toLongArray()[0];
	}

	private static int setB(int rgb, int BBit, boolean inputBit) {
		BitSet bitSet = new BitSet();
		bitSet = BitSet.valueOf(new long[] { rgb });
		bitSet.set(0 + BBit, inputBit);
		return (int) bitSet.toLongArray()[0];
	}
}
