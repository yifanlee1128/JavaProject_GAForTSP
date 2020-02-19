package Assignment7;


import java.util.ArrayList;

public interface I_EvalObject {
	
	/**
	 * @return The number of the best solutions that will automatically
	 *         make it into the next generation.
	 */
	public int getNumBestToSave();
	
	/**
	 * @return The number of possible states in each dimension of the
	 *         problem. 
	 */
	public int[] getDimSizes();

	/**
	 * @return The fraction of bits in the entire population that are
	 *         randomly flipped from zero to one or vice versa. 
	 */
	public float getMutationRate();
	
	/**
	 * @return Return the self similarity stopping criteria, e.g. 0.95
	 *         or 0.99
	 */
	public float getSelfSimTarget();
	
	/**
	 * @return Number of candidate solutions in the population.
	 */
	public int getPopSize();
	
	/**
	 * Max progress pause is the number of generations the GA will
	 * run for if there is no improvement in the best solution. This
	 * is another stopping criterion, similar to self-similarity.
	 * 
	 * @return Return true if this stopping criterion has been reached. 
	 */
	/**
	 * When two Crs are spliced, there could be more than one splicing
	 * point.
	 * 
	 * @return The number of splicing points in the splice.
	 */
	public int getNumSplicePoints();

	/** How many generations without a better solution
	 *  before the GA quits
	 * @return Number of generation to wait
	 */
	public int getMaxProgressPause();
	
	/** Return an array of fitness values, one for
	 *  each candidate solution in values.
	 *  
	 * @param values Candidate solutions are rows and 
	 *               dimensions are columns.
	 */
	void computeFitness(ArrayList<Cr> population);
	//an overload the computeFitness function for the for loop in GA
	void computeFitness(Cr chromosome);
	
		/**
	 * @return true if we are initializing the population to random
	 *              permutations of city indices rather than random
	 *              bits which can translate to repeating values.
	 *              In TSP initialization, we can visit each city
	 *              only once.
	 */
	public boolean isTSP();
	
}
