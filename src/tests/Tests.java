package tests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

import processing.Common;
import processing.Configuration;
import processing.Decoder;
import processing.Encoder;
import processing.FileReaderWriter;

public class Tests {

	@Test
	public void checkComponentBitsAmountAfterEncodeAndDecode() {
		final String bitmapFilePath = "c:\\Android\\LadyBird.bmp";
		final String inputFilePath = "c:\\Android\\test.txt";
		Random rand = new Random();
		Configuration configuration = Configuration.create();
		for (int i = 0; i < 10; i++) {
			byte R = (byte) rand.nextInt(8);
			byte G = (byte) rand.nextInt(8);
			byte B = (byte) rand.nextInt(8);
			configuration = configuration.setRBitsAmount(R).setGBitsAmount(G).setBBitsAmount(B).setImageFilePath(bitmapFilePath).setInputFilePath(inputFilePath);

			Encoder.encode(configuration);
			configuration.setRBitsAmount((byte) 0).setGBitsAmount((byte) 0).setBBitsAmount((byte) 0);

			String encodedBitmapPath = bitmapFilePath.substring(0, bitmapFilePath.length() - 4) + "_encoded.bmp";
			configuration = Decoder.loadConfigValuesFromBitmap(
					FileReaderWriter.openBitmapFromFile(encodedBitmapPath), configuration);
			assertEquals(R, configuration.getRBitsAmount());
			assertEquals(G, configuration.getGBitsAmount());
			assertEquals(B, configuration.getBBitsAmount());
		}
		Exception exception = null;
		try {
			configuration.setRBitsAmount((byte) 9);
		} catch (RuntimeException e) {
			exception = e;
		}
		assertNotNull(exception);
	}

	@Test
	public void listToArrayTest() {
		ArrayList<Boolean> bools = new ArrayList<>(Arrays.asList(true, true, false, true, false, true, false, true, false, true, false, true, false, true, false, true));
		byte[] expectedBits = new byte[] { -43, 85 };
		byte[] bits = Common.toByteArray(bools);
		
		assertArrayEquals(expectedBits, bits);
	}
	
	@Test
	public void testExtensionCheckingMethods(){
		final String inputFilePath = "c:\\Android\\test.txt";
		Configuration configuration = Configuration.create();
		configuration.setInputFilePath(inputFilePath);
		
		assertEquals("txt", configuration.getInputFileExtension());
	}
	
}
