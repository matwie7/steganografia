public class Main {
	public static void main(String[] args) {
		// mocks
		final String bitmapFilePath = "c:\\Android\\LadyBird.bmp";
		final String fileToHidePath = "c:\\Android\\test.txt";
		Configuration configuration = Configuration.create((byte) 3, (byte) 4, (byte) 5);
		
		Encoder.encode(bitmapFilePath, fileToHidePath, configuration);
		Decoder.decode(bitmapFilePath+"2", "c:\\Android\\aaa.txt");
	}
}
