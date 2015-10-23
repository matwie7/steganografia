public class Main {
	public static void main(String[] args) {
		// mocks
		final String bitmapFilePath = "c:\\Android\\LadyBird.bmp";
		final String fileToHidePath = "c:\\Android\\test.txt";

		Encoder.encode(bitmapFilePath, fileToHidePath);
	}
}
