public class Configuration {

	private static Configuration configuration;
	private byte rBitsAmount;
	private byte gBitsAmount;
	private byte bBitsAmount;

	private Configuration(byte rBitsAmount, byte gBitsAmount, byte bBitsAmount) {
		this.rBitsAmount = rBitsAmount;
		this.gBitsAmount = gBitsAmount;
		this.bBitsAmount = bBitsAmount;
	}

	private Configuration() {
	}

	public static Configuration create() {
		if (configuration == null) {
			configuration = new Configuration();
		}
		return configuration;
	}

	public static Configuration create(byte rBitsAmount, byte gBitsAmount, byte bBitsAmount) {
		if (configuration == null) {
			configuration = new Configuration(rBitsAmount, gBitsAmount, bBitsAmount);
		}
		return configuration;
	}

	public byte getRBitsAmount() {
		return rBitsAmount;
	}

	public Configuration setRBitsAmount(byte rBitsAmount) {
		validateRGB(rBitsAmount);
		this.rBitsAmount = rBitsAmount;
		return configuration;
	}

	private void validateRGB(byte bitsAmount) {
		if (bitsAmount < 0 || bitsAmount > 8)
			throw new RuntimeException("Illegal parameter");
	}

	public byte getGBitsAmount() {
		return gBitsAmount;
	}

	public Configuration setGBitsAmount(byte gBitsAmount) {
		validateRGB(gBitsAmount);
		this.gBitsAmount = gBitsAmount;
		return configuration;
	}

	public byte getBBitsAmount() {
		return bBitsAmount;
	}

	public Configuration setBBitsAmount(byte bBitsAmount) {
		validateRGB(bBitsAmount);
		this.bBitsAmount = bBitsAmount;
		return configuration;
	}

	public int getBitsPerPixel() {
		return rBitsAmount + gBitsAmount + bBitsAmount;
	}

	public byte[] getRGBContribution() {
		return new byte[] { rBitsAmount, gBitsAmount, bBitsAmount };
	}
}
