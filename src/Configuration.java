/**
 * Klasa s³u¿¹ca do przechowywania konfiguracji. Zastosowanu to wzorzec
 * projektowy Singleton (mo¿e istnieæ jedna instancja tej klasy)
 * 
 * @author Mateusz Wiêcek
 *
 */
public class Configuration {

	private static Configuration configuration;
	private int rBitsAmount;
	private int gBitsAmount;
	private int bBitsAmount;

	private Configuration(int rBitsAmount, int gBitsAmount, int bBitsAmount) {
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

	public static Configuration create(int rBitsAmount, int gBitsAmount, int bBitsAmount) {
		if (configuration == null) {
			configuration = new Configuration(rBitsAmount, gBitsAmount, bBitsAmount);
		}
		return configuration;
	}

	public int getRBitsAmount() {
		return rBitsAmount;
	}

	public void setRBitsAmount(int rBitsAmount) {
		this.rBitsAmount = rBitsAmount;
	}

	public int getGBitsAmount() {
		return gBitsAmount;
	}

	public void setGBitsAmount(int gBitsAmount) {
		this.gBitsAmount = gBitsAmount;
	}

	public int getBBitsAmount() {
		return bBitsAmount;
	}

	public void setBBitsAmount(int bBitsAmount) {
		this.bBitsAmount = bBitsAmount;
	}

	public int getBitsPerPixel() {
		return rBitsAmount + gBitsAmount + bBitsAmount;
	}
}
