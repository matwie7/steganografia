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
	
	public static boolean isBitSet(byte number, int bitIndex){
		return (number & (1 << bitIndex)) != 0;
	}
	
	public static byte[] toByteArray(List<Boolean> bools) {
		byte[] bits = new byte[bools.size() / 8];
	    
		for(int i = 0; i < bools.size() / 8; i++)
			for(int j = 0; j < 8; j++){
				bits[i] = (byte)(bits[i] << 1);
				bits[i] = (byte)(bits[i] | (bools.get((i * 8) + j) ? 1 : 0));
			} 
	    return bits;
	}
	
	public static byte getMask(byte n) {
		byte mask = 0;
		for (int i = 0; i < n; i++)
			mask += Math.pow(2, i);

		return mask;
	}
}
