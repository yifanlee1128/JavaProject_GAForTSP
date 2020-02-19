package Assignment7;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;

public class Test_GA extends junit.framework.TestCase {

	public void test1() {
		
		/** Our solution space has 70,000 possible states in
		 *  each of three dimensions.
		 */
		final int[] _dimSizes = {70000,70000,70000};
		
		I_EvalObject evalObject = new I_EvalObject() {

			@Override
			/** Return the number of possible states in each dimension. */
			public int[] getDimSizes() {
				return _dimSizes;
			}

			@Override
			/** Return the probability that a bit will flip in any
			 *  generation. (Flip means changing from 1 to 0 or
			 *  the reverse.)
			 */
			public float getMutationRate() {
				return 0.001f;
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
				/**
				 * Our fitness value is the absolute distance between the
				 * product of three numbers (each a value between 0 and 
				 * 69,999, inclusive) and 5,537,142, our target value.
				 * In other words, we want to find three numbers, the
				 * product of which is as close as possible to our target
				 * value, 5,537,142. The size of the solution space is
				 * 70e3 * 70e3 * 70e3 = 343 trillion.
				 * 
				 * The GA will not give us an exact value but will get us
				 * to 1/100th of 1%. Clearly, there are many solutions.
				 * We should ask how hard it would be to arrive at the
				 * solution by brute force.
				 */
				double target = 5537142D;
				for( Cr cr : population ) {
					int values[] = cr.getValues();
					double fitness = (49*7*1e12D) - Math.abs( 
						target - ( 
							(double)values[ 0 ] * 
							(double)values[ 1 ] * 
							(double)values[ 2 ]  
						) 
					);
					cr.setFitness( fitness );					
				}
			}
			@Override
			public void computeFitness(Cr chromosome) {
				;
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
			 *  together two Crs to create an offspring.
			 */
			public int getNumSplicePoints() {
				return 3;
			}

			@Override
			/** Return the number of generations that can pass without an
			 *  improvement in the best fitness.
			 */
			public int getMaxProgressPause() {
				return 200;
			}

			@Override
			public boolean isTSP() {
				return false;
			}
			
		};
		GA ga = new GA( evalObject );
		ga.go();
		Cr cr = ga.getPopulation().get( 0 );
		int[] solution = cr.getValues();
		System.out.println(
			String.format( "{%d,%d,%d}\n", solution[ 0 ], solution[ 1 ], solution[ 2 ] )
		);
		
	}
	
	public void testOrderedCrossoverSplice() {
		int[] values1 = { 11, 12, 13, 14, 15, 16, 17 };
		int[] values2 = { 1,   13,  3,  4,  5,  14,  15 };
		assertNotEquals( values1, values2 );

		int[] result1 = Cr.OrderedCrossoverSplice( 2, 5, values1, values2 ); 
		int[] exptected1 = { 1, 3, 13, 14, 15, 4, 5 };
		assertArrayEquals( exptected1, result1 );
		
		int[] result2 = Cr.OrderedCrossoverSplice( 0, 0, values1, values2 );
		int[] expected2 = values2;
		assertArrayEquals( expected2, result2 );
	}
	
}
