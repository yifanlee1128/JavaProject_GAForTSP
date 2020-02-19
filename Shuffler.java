package Assignment7;


/** Class used to provide a shuffled sequence of
 *  values which are either provided or computed
 *  by in the constructor for a given number of
 *  dimensions.
 */
public class Shuffler {

	protected int[] _values;
	
	public Shuffler( int[] values ) {
		_values = values;
		this.next();
	}
	
	public Shuffler( int numDims ) {
		_values = new int[ numDims ];
		for( int i = 0; i < numDims; i++ )
			_values[ i ] = i;
		this.next();
	}

	/** Return the next permutation of this object's internal
	 *  values array
	 * @return A shuffled values array
	 */
	public int[] next() {
		int len = _values.length;
		int[] newValues = new int[ _values.length ];
		int newValuesIndex = 0;
		while( len > 1 ) {
			int i = RandomGenerator.getInstance().nextInt( len );
			newValues[ newValuesIndex ] = _values[ i ];
			_values[ i ] = _values[ len - 1 ];
			_values[ len - 1 ] = newValues[ newValuesIndex ];
			len--;
			newValuesIndex++;
		}
		newValues[ newValuesIndex ] = _values[ 0 ];
		return newValues;
	}

}
