package Assignment7;


public class Test_Circle extends junit.framework.TestCase {
	
	/** Test aCircle's distance method */
	public void test_mrh5Eq() {
		Circle circle = new Circle( Circle.HalfCircle );
		double distance = circle.getDistance( 0, Circle.HalfCircle.length - 1 );
		assertEquals( distance, 2.0, 0.00001 );
	}

}
