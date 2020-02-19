package Assignment7;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;

public class Test_GA2 extends junit.framework.TestCase {

	public void test1() {
		
		/** Our solution space has 10! possible states
		 */
		final int[] _dimSizes = new int[ 10 ];
		for( int i = 0; i < _dimSizes.length; i++ )
			_dimSizes[ i ] = 10;
		
		final Circle _circle = new Circle( Circle.HalfCircle1 );
		
		I_EvalObject evalObject = new I_EvalObject() {

			@Override
			/** Return the number of possible states in each dimension. */
			public int[] getDimSizes() {
				return _dimSizes;
			}

			@Override
			/** Return the probability that a bit will flip in any
			 *  generation. (Flip means changing from 1 to 0 or
			 *  the reverse.) In the case of ordered mutation,
			 *  we don't actually flip bits. We exchange the
			 *  values in one dimension for the values in another.
			 */
			public float getMutationRate() {
				return 40f * 0.001f;
			}

			@Override
			/** Get the taeget self similarity of the population. */
			public float getSelfSimTarget() {
				return 0.999f;
			}

			@Override
			/** Get the number of candidate Crs in each generation. */
			public int getPopSize() {
				return 200;
			}

			@Override
			/** Evaluate each Cr and set its fitness. */
			public void computeFitness(
				ArrayList<Cr> population
			) {
				// For each Cr, the fitness is the length of the route used
				// to visit each city once SUBTRACTED from some large number.
				// Our Circle object will do the actual calculation. We feed
				// to it the route described by the values of each candidate
				// Cr in our population.
				for( Cr cr : population ) {
					int values[] = cr.getValues();
					double fitness = _circle.fitness( values );
					cr.setFitness( fitness );					
				}
				
			}
			@Override
			public void computeFitness(Cr chromosome){
				int values[]=chromosome.getValues();
				double fitness=_circle.fitness(values);
				chromosome.setFitness(fitness);
			}


			@Override
			/** Get  the number of best solutions that will be forced
			 *  into the next generation as opposed to probabilistically
			 *  selected for mating.
			 */
			public int getNumBestToSave() {
				return 20;
			}

			@Override
			/** Return the number of points that will be used to splice
			 *  together two Crs to create an offspring. Used only
			 *  when splicing takes place in the bit string not in
			 *  the values (TSP variant).
			 */
			public int getNumSplicePoints() {
				return 0;
			}

			@Override
			/** Return the number of generatios that can pass without an
			 *  improvement in the best fitness.
			 */
			public int getMaxProgressPause() {
				return 200;
			}

			@Override
			/** Return true if this is a TSP problem and we are using
			 *  ordered mutation and value based crossover
			 */
			public boolean isTSP() {
				return true;
			}
			
		};
		GA ga = new GA( evalObject );
		ga.go();
		
	}
	
	public void testOrderedCrossoverSplice() {
		int[] values1 = { 11, 12, 13, 14, 15, 16, 17 };
		int[] values2 = { 1,  13,  3,  4,  5, 14, 15 };
		assertNotEquals( values1, values2 );

		int startIndexIntoCr1 = 2;
		int endIndexIntoCr1 = 5;
		int[] result1 = Cr.OrderedCrossoverSplice( startIndexIntoCr1, endIndexIntoCr1, values1, values2 ); 
		int[] exptected1 = { 1, 3, 13, 14, 15, 4, 5 };
		assertArrayEquals( exptected1, result1 );
		
		int[] result2 = Cr.OrderedCrossoverSplice( 0, 0, values1, values2 );
		int[] expected2 = values2;
		assertArrayEquals( expected2, result2 );
		
	}
	
}
