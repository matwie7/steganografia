import java.util.BitSet;

public class Main {
	public static void main(String[] args) {
		final String bitmapFilePath = "c:\\Android\\LadyBird.bmp";
		final String fileToHidePath = "c:\\Android\\test.txt";

		Enigma.enigmate(bitmapFilePath, fileToHidePath);

		int a = 0b10000000000000000000000000000000;
		BitSet bitSet = new BitSet();

		bitSet = BitSet.valueOf(new long[] { a });

		bitSet.set(0, true);
		bitSet.set(1, true);
		bitSet.set(2, false);
		bitSet.set(3, true);
		System.out.println(Integer.toBinaryString((int) bitSet.toLongArray()[0]));
	}
}
