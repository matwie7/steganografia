/**
 * Klasa s³u¿¹ca do przechowywania konfiguracji. Zastosowanu to wzorzec
 * projektowy Singleton (mo¿e istnieæ jedna instancja tej klasy)
 * 
 * @author Mateusz Wiêcek
 *
 */
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

	public void setRBitsAmount(byte rBitsAmount) {
		this.rBitsAmount = rBitsAmount;
	}

	public byte getGBitsAmount() {
		return gBitsAmount;
	}

	public void setGBitsAmount(byte gBitsAmount) {
		this.gBitsAmount = gBitsAmount;
	}

	public byte getBBitsAmount() {
		return bBitsAmount;
	}

	public void setBBitsAmount(byte bBitsAmount) {
		this.bBitsAmount = bBitsAmount;
	}

	public int getBitsPerPixel() {
		return rBitsAmount + gBitsAmount + bBitsAmount;
	}
}
