public class Main {
	public static void main(String[] args) {
		// mocks
		final String bitmapFilePath = "c:\\Android\\LadyBird.bmp";
		final String fileToHidePath = "c:\\Android\\aaaa.pdf";
		Configuration configuration = Configuration.create((byte) 0, (byte) 1, (byte) 1);
		
		long startTime = System.currentTimeMillis();
		Encoder.encode(bitmapFilePath, fileToHidePath, configuration);
		System.out.println("encoded in: " + (System.currentTimeMillis() - startTime) + "ms");
		startTime = System.currentTimeMillis();
		Decoder.decode(bitmapFilePath+"2", "c:\\Android\\output.pdf");
		System.out.println("decoded in: " + (System.currentTimeMillis() - startTime) + "ms");
	}
}
