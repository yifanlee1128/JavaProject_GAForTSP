package Assignment7;

import java.util.HashSet;
import java.util.LinkedList;

/**
 * Class representing a single chromosome - a single
 * candidate solution - in the population of a GA.
 * 
 * It handles the creation of a randomly generated
 * Cr, splicing Crs, decoding a Cr into integer
 * values representing one of a Cr's states, and
 * mutating Crs. It is also used to keep a fitness
 * and its decoded values in each of its dimensions.
 * 
 */
public class Cr {
	
	/** A string of 0's and 1's that represents this
	 *  Chromosome.
	 */
	protected String _genes;
	
	/** The fitness of this Cr */
	protected Double _fitness;
	
	/** The decoded, scaled values of this Cr in each
	 *  of its dimensions.
	 */
	protected int[] _values;
	
	/** Given an array of integer number of bits in each
	 *  dimension, instantiate a Cr class with the
	 *  appropriate number of randomly generated bits.
	 *  
	 * @param numBits An array of int values describing the
	 *                number of bits required to represent
	 *                each dimension of the problem.
	 */
	public Cr( int ... numBits ) {
		_fitness = null;
		_values = new int[ numBits.length ];
		int totalBits = 0;
		for( int n : numBits )
			totalBits += n;
		char[] str = new char[ totalBits ];
		for( int i = 0; i < totalBits; i++ )
			str[ i ] = RandomGenerator.getInstance().nextBoolean() ? '1' : '0';
		_genes = new String( str );
		
		// test yOuJbQ
	}
	
	/** Returns the bits of this Cr in a given dimension */
	public String getBitsInDim( int dimStartIndex, int dimEndIndex ) {
		return _genes.substring( dimStartIndex, dimEndIndex );
	}

	/**
	 * Set the fitness of this Cr.
	 * 
	 * @param fitness The new fitness value of this Cr.
	 */
	public void setFitness( double fitness ) {
		_fitness = fitness;
	}
	/**
	 * @param values The decoded, scaled values of this Cr to which its
	 *               internal _values variable will be set. 
	 */
	public void setValues( int[] values ) {
		_values = values;
	}
	
	/** Construct a new chromosome from the string
	 *  of 0's and 1's that are its bits.
	 */
	public Cr( String str ) { _genes = str; } // eXAxIY
	
	/** Splice this candidate solution with cr using
	 *  breaks.
	 * @param cr
	 * @param breaks Splice points used to determine whether
	 *               this Chromosome's bits or the other 
	 *               Chromosome's bits are copied. E.g.
	 *               5, 8, 10 means that bits 0-4 come from
	 *               this Cr, bits 5-7 come from the other
	 *               Cr, bits 8-9 come from this Cr, and
	 *               bits 10 and on come from the other.
	 * @return The new Cr that results from the splice.
	 */
	public Cr spliceWith( Cr cr, int ... breaks ) {
		StringBuffer sb = new StringBuffer();
		int start = 0;
		String thisOne = this._genes;
		String otherOne = cr._genes;
		for( int i = 0; i < breaks.length; i++ ) {
			sb.append( thisOne.substring( start, breaks[ i ] ) );
			String temp = thisOne;
			thisOne = otherOne;
			otherOne = temp;
			start = breaks[ i ];
		}
		sb.append( thisOne.substring( start, thisOne.length() ) );
		return new Cr( sb.toString() );
		// test eXAxIY
	}

	/** For every bit, with the given prob, flip the bit from
	 *  0 to 1 or 1 to 0
	 * @param prob The probability of a flip
	 */
	public void mutate( float prob ) {
		// Note that the right way to do it is to figure out ahead
		// of time the number of mutations expected per chromosome
		// and the standard deviation of that number. Then we
		// can sample from the appropriate distribution to see
		// how many mutations there should be. For most
		// chromosomes, there will be none, so we can skip this
		// part entirely.
		StringBuffer sb = null;
		for( int i = 0; i < _genes.length(); i ++ )
			if( RandomGenerator.getInstance().nextFloat() < prob ) {
				if( sb == null )
					sb = new StringBuffer( _genes );
				sb.setCharAt(i, sb.charAt( i ) == '1' ? '0' : '1' );
			}
		if( sb != null )
			_genes = sb.toString();
		// test 5NqqlB
	}
	
	/** Mutation described in article on TSP:
	 *  https://towardsdatascience.com/evolution-of-a-salesman-a-complete-genetic-algorithm-tutorial-for-python-6fe5d2b3ca35
	 *  
	 *  This type of mutation preserves the order in each
	 *  dimension of the problem and makes sure that the
	 *  mutation does not lead to illegal solutions.
	 *  
	 * @param dimSizesInBits Array specifying the number of bits in each
	 *                       dimension of the problem. 
	 * @param prob The probability of a mutation in one Cr.
	 */
	public void orderedMutation( int[] dimSizesInBits, float prob ) {
		if( RandomGenerator.getInstance().nextFloat() < prob ) {
			int i = RandomGenerator.getInstance().nextInt( this.getValues().length );
			int j = RandomGenerator.getInstance().nextInt( this.getValues().length );
			if( i != j ) {
				int temp = _values[ i ];
				_values[ i ] = _values[ j ];
				_values[ j ] = temp;
			}
			String str = Cr.DimValuesToBitString( _values, dimSizesInBits );
			_genes = str;
		}
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( String.format( "Cr( { %d", _values[ 0 ] ) );
		for( int i = 1; i < _values.length; i++ ) sb.append(
			String.format( ", %d", _values[ i ] )
		);
		sb.append( String.format( "}, %s )", _genes ) );
		return sb.toString();
			
	}
	
	/**
	 * @return The string representation - 0s and 1s - of
	 *         this Cr.
	 */
	public final String getGenes() { return _genes; }
	
	/** Given the number of bits in each dimension,
	 *  decode the chromosome to produce an integer
	 *  value in each dimension. Note that these
	 *  values are unscaled. Only the eval object
	 *  can scale them to the proper range of
	 *  values.
	 *  
	 * @param dimSizesInBits An array of dimension size
	 *                       values
	 * @return An array of values in each dimension
	 */
	public int[] decode( int ... dimSizesInBits ) {
		int[] result = new int[ dimSizesInBits.length ];
		// Iterate through each dimension, using Integer.parseInt(...)
		// to convert strings of bits to int values.
		int start = 0;
		for( int i = 0; i < dimSizesInBits.length; i++ ) {
			result[ i ] = Integer.parseInt(
				_genes.substring( start, start + dimSizesInBits[ i ] ), 
				2
			);
			start += dimSizesInBits[ i ];
		}
		return result;
		// test_5LFKdO
	}

	/** Return the fitness value of this Cr */
	public Double getFitness() {
		return _fitness;
	}
	
	/** Return this Cr's scaled values in each dimension. */
	public int[] getValues() { return _values; }
	
	/** Return true if normal mutation or false if ordered
	 *  mutation.
	 */
	public boolean isOrderedMutation() {
		return false;
	}

	/** A convenience method for converting the values in each
	 *  dimension of a cr to a bit string.
	 *  
	 * @param dimValues An array with the decoded values of a Cr object
	 * @param dimSizesInBits The number of bits used to represent each dimension
	 *                       of the above Cr
	 * @return A string of '0' and '1' characters representing the above dimValues
	 */
	public static String DimValuesToBitString( 
		int[] dimValues, 
		int[] dimSizesInBits 
	) {
		// I want to convert dimValues into a string.
		StringBuffer sb = new StringBuffer();
		for( int i = 0; i < dimValues.length; i++ ) {
			String binRepresentation = Integer.toBinaryString( dimValues[ i ] );
			int numPrependZeroes = dimSizesInBits[ i ] - binRepresentation.length();
			for( int j = 0; j < numPrependZeroes; j++ )
				sb.append( "0" );
			sb.append( binRepresentation );
		}
		return sb.toString();
	}

	/** This static method performs the ordered crossover splice describe in
	 *  the link to the traveling salesman solution that I previously gave
	 *  you: https://towardsdatascience.com/evolution-of-a-salesman-a-complete-genetic-algorithm-tutorial-for-python-6fe5d2b3ca35
	 *  
	 * @param startIndexInto1 Where to start copying values from cr1
	 * @param endIndexInto1 Where to end copying values from cr1 (exclusive)
	 * @param valuesOfCr1 The first Cr's values, obtained by calling its getValues method
	 * @param valuesOfCr2 The second Cr's values, obtained by calling its getValues method
	 * @return A new array that splices the values from cr1 and cr2 
	 */
	public static int[] OrderedCrossoverSplice( 
		int startIndexInto1,
		int endIndexInto1,
		int[] valuesOfCr1, // An array containing the values of cr1
		int[] valuesOfCr2 // An array containing the values of cr2
	) {
		// Figure out which elements were chosen from Cr1
		HashSet<Integer> chosenElementsOfCr1 = new HashSet<Integer>();
		for( int i = startIndexInto1; i < endIndexInto1; i++ )
			chosenElementsOfCr1.add( valuesOfCr1[ i ] );
		
		// Now choose all of the elements of Cr2 that were not
		// chosen in Cr1
		LinkedList<Integer> reducedCr2Indices = new LinkedList<Integer>();
		for( int i = 0; i < valuesOfCr2.length; i++ )
			if( !chosenElementsOfCr1.contains( valuesOfCr2[ i ] ) )
				reducedCr2Indices.addLast( valuesOfCr2[ i ]);
		
		// We are ready to make the results array.
		int[] result = new int[ valuesOfCr2.length ];
		
		// First add all of the elements of Cr2 that
		// now precede elements of Cr1.
		for( int i = 0; i < startIndexInto1; i++ )
			result[ i ] = reducedCr2Indices.removeFirst();
		
		// Now, add all of the elements of Cr1 that are specified 
		// by startIndexInto1 and endIndexInto1.
		for( int i = startIndexInto1; i < endIndexInto1; i++ )
			result[ i ] = valuesOfCr1[ i ];
		
		// Now, add all of the elementsof 
		for( int i = endIndexInto1; i < valuesOfCr2.length; i++ )
			result[ i ] = reducedCr2Indices.removeFirst();
		
		return result;
	}

	/** Instantiate a Cr from an array of values in each dimension
	 *  of the problem. This is the TSP variant. We can't use a
	 *  random bit string to initialize the population because
	 *  some random bit strings give us illegal values, e.g. 
	 *  values that tell us to visit the same city more than
	 *  once.
	 *  
	 * @param values Array of integer values, one per each dimension of our problem
	 * @param dimSizes The size of each dimension of our problem
	 * @param numBitsPerDim The number of bits we will use to represent each
	 *                      dimension of our problem
	 * @return
	 */
	public static Cr FromValues(
		int[] values, 
		int[] dimSizes, 
		int[] numBitsPerDim
	) {
		// The bit string must be scaled up to use all of the
		// bits we need to represent every dimension
		int[] scaledUpValues = new int[ values.length ]; 
		for( int i = 0; i < values.length; i++ )
			scaledUpValues[ i ] = (int) (
				( Math.pow( 2,  numBitsPerDim[ i ] ) - 1.0 ) 
				* (float) values[ i ] 
				/ ( (float) dimSizes[ i ] - 1.0 )
			);
		Cr cr = new Cr( Cr.DimValuesToBitString( scaledUpValues, numBitsPerDim ) );
		cr.setValues( values );
		return cr;
	}
	
}