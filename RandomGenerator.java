package Assignment7;

import java.util.Random;

public class RandomGenerator {
	
	protected static Random RAND = new Random();
	
	public static Random getInstance() {
		return RAND;
	}

}
