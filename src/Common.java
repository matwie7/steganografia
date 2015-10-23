import java.util.BitSet;

public class Common {
	public static byte[] intToByteArray(int a) {
		return new byte[] { (byte) ((a >> 24) & 0xFF), 
				(byte) ((a >> 16) & 0xFF), 
				(byte) ((a >> 8) & 0xFF),
				(byte) (a & 0xFF) };
	}
	
	public static BitSet intToBitSet(int integer) {
		return BitSet.valueOf(new long[] { integer });
	}
}
