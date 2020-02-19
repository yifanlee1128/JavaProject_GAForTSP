package Assignment7;

public class Test_Shuffler extends junit.framework.TestCase {

	public void test1() {
		int[] values = { 1, 2, 3, 4, 5, 6 };
		Shuffler shuffler = new Shuffler( values );
		int[] newValues = shuffler.next();
		int[] newValues1 = shuffler.next();
	}
}
