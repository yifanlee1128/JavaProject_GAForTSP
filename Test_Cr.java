package Assignment7;

public class Test_Cr extends junit.framework.TestCase {

	/** Test constructor and spliceWith */
	public void test_eXAxIY() {
		Cr cr1 = new Cr( "0000000" );
		Cr cr2 = new Cr( "1111111" );
		Cr result = cr1.spliceWith( cr2, 2, 5 );
		assertTrue( result.getGenes().equals( "0011100") );
	}
	
	/** Test instantiation from list of dimensions */
	public void test_yOuJbQ() {
		Cr cr1 = new Cr( 400, 100, 500 );
		assertTrue( cr1.getGenes().length() == 1000 );
		StringBuffer sb = new StringBuffer( cr1.getGenes() );
		int nOnes = 0;
		for( int i = 0; i < 1000; i++ )
			nOnes += sb.charAt( i ) == '1' ? 1 : 0;
		assertTrue( ( nOnes > 470 ) && ( nOnes < 530 ) );
	}
	
	/** Test mutation */
	public void test_5NqqlB() {
		StringBuffer sb = new StringBuffer();
		for( int i = 0; i < 1000; i++ )
			sb.append("1");
		Cr cr4 = new Cr( sb.toString() );
		cr4.mutate( 0.5f );
		sb = new StringBuffer( cr4.getGenes() );
		int nOnes = 0;
		for( int i = 0; i < 1000; i++ )
			nOnes += sb.charAt( i ) == '1' ? 1 : 0;
		assertTrue( ( nOnes > 470 ) && ( nOnes < 530 ) );
	}
	
	/** Test decode */
	public void test_5LFKdO() {
		int[] values = ( new Cr( "0000111110101" ) ).decode( 4, 4, 5 );
		assertTrue( values[ 0 ] == 0 );
		assertTrue( values[ 1 ] == 15 );
		assertTrue( values[ 2 ] == 21 );
	}
	
}
