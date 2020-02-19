package Assignment7;

public class Circle {

	protected static double[][] HalfCircle = { {1.22465e-16f,-1f},{-0.34202f,-0.939693f},{-0.642788f,-0.766044f},{-0.866025f,-0.5f},{-0.984808f,-0.173648f},{-0.984808f,0.173648f},{-0.866025f,0.5f},{-0.642788f,0.766044f},{-0.34202f,0.939693f},{-2.44929e-16f,1f}};
	protected static double[][] HalfCircle1 = { {-0.984808f,-0.173648f},{-0.984808f,0.173648f},{-0.866025f,0.5f},{-0.642788f,0.766044f},{-0.34202f,0.939693f},{-2.44929e-16f,1f},{1.22465e-16f,-1f},{-0.34202f,-0.939693f},{-0.642788f,-0.766044f},{-0.866025f,-0.5f}};

	protected double[][] _coords;
	public Circle( double[][] coords ) {
		_coords = coords;
	}
	
	/** Calculate the distance from one city to another */
	public double getDistance( int fromCity, int toCity ) {
		return(
			Math.sqrt(
				Math.pow( _coords[ fromCity ][ 0 ] - _coords[ toCity ][ 0 ], 2.0 )
				+ Math.pow( _coords[ fromCity ][ 1 ] - _coords[ toCity ][ 1 ], 2.0 )
			)
		);
		// test_mrh5Eq
	}
	
	/** Return the fitness of a traversal described by
	 *  the indices into _coords.
	 *  
	 * @param cities Indices specifying cities in the _coords array.
	 * @return The distance of the traversal
	 */
	public Double fitness( int[] cities ) {
		double sum = 0;
		for( int i = 1; i < _coords.length; i++ ) 
			sum += this.getDistance(
				cities[ i ],
				cities[ i - 1 ]
			);
		return ( 2.0D * ( cities.length - 1 ) ) - sum;
	}
	
}
