import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Common {
	public static int byteArrayToInt(byte[] b) {
		return 	 b[3] & 0xFF 		| 
				(b[2] & 0xFF) << 8  |
				(b[1] & 0xFF) << 16 |
				(b[0] & 0xFF) << 24;
	}

	public static byte[] intToByteArray(int a) {
		return new byte[] { 
				(byte) ((a >> 24) & 0xFF),
				(byte) ((a >> 16) & 0xFF),
				(byte) ((a >> 8) & 0xFF),
				(byte) (a & 0xFF) };
	}

	public static BitSet intToBitSet(int integer) {
		return BitSet.valueOf(new long[] { integer });
	}

	public static List<Boolean> bitSetToValidListOfBoolean(BitSet input) {
		List<Boolean> validInput = new ArrayList<Boolean>();
		if (input.size() % 8 != 0)
			throw new RuntimeException("Input non %8. WTF?!");

		for (int j = 0; j < input.size() / 8; j++)
			for (int i = 1; i <= 8; i++)
				validInput.add(input.get(((j + 1) * 8) - i));

		return validInput;
	}

	public static int getEncodedDataLengthFromImage(BufferedImage bufferedImage, Configuration configuration) {
		String lengthBinary = "";
		int bits = 0;
		outterLoop: for (int y = 0; y < bufferedImage.getHeight(); y++)
			for (int x = 0; x < bufferedImage.getWidth(); x++) {
				if (x <= 3 && y == 0)
					continue;

				int rgb = bufferedImage.getRGB(x, y);
				byte[] rgbArray = Common.intToByteArray(rgb);

				byte mask = 0;
				for (int i = 0; i < configuration.getRBitsAmount(); i++) {
					mask += Math.pow(2, i);
					bits++;
				}
				byte r = (byte) (rgbArray[1] & mask);
				lengthBinary += Integer.toBinaryString(r);

				mask = 0;
				for (int i = 0; i < configuration.getGBitsAmount(); i++) {
					mask += Math.pow(2, i);
					bits++;
				}
				byte g = (byte) (rgbArray[2] & mask);
				lengthBinary += Integer.toBinaryString(g);

				mask = 0;
				for (int i = 0; i < configuration.getBBitsAmount(); i++) {
					mask += Math.pow(2, i);
					bits++;
				}
				byte b = (byte) (rgbArray[3] & mask);
				lengthBinary += Integer.toBinaryString(b);

				if (bits >= Integer.SIZE)
					break outterLoop;
			}
		return Integer.parseInt(lengthBinary.substring(0, lengthBinary.length() - (bits - Integer.SIZE)), 2);
	}
	
	public static boolean isBitSet(byte number, int bitIndex){
		return (number & (1 << bitIndex)) != 0;
	}
	
	public static byte[] toByteArray(List<Boolean> bools) {
		byte[] bits = new byte[bools.size() / 8];
	    
		for(int i = 0; i < bools.size() / 8; i++){
			for(int j = 0; j < 8; j++){
				bits[i] = (byte)(bits[i] << 1);
				bits[i] = (byte)(bits[i] | (bools.get((i * 8) + j) ? 1 : 0));
			} 
		}
		
	    return bits;
	}
}
