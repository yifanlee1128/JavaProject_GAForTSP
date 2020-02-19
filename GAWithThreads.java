package Assignment7;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class GAWithThreads {
    /** A list of Crs - chromosomes of bit strings - that
     *  represent the candidate solutions of this GA.
     */
    protected ArrayList<Cr> _population;

    /** An object that the GA can query to obtain the
     *  parameters of this search such as the size of the
     *  population, the stopping criteria, and so on. The
     *  object also has a method for computing fitness.
     */
    protected I_EvalObject _evalObject;

    /** A counter for determining the current generation */
    protected int _iGen;

    /** The number of bits required to represent the dimensions
     *  of the GA's problem.
     */
    protected int[] _numBitsPerDim;

    /** The best fitness achieved thus far */
    protected double _bestFitness;

    /** The generation in which the best fitness was achieved */
    protected int _bestFitnessGen;

    /** The self similarity of the solutions */
    protected float _selfSim;

    /** Save the eval object and determine the number of
     *  bits that will be require in each dimension.
     * @param evalObject The object that will be used to tell the GA
     *                   about the parameters of the search, including
     *                   the fitness function.
     */
    public GAWithThreads( I_EvalObject evalObject ) {

        // Save the evalObject which is the GA's connection
        // to the actual problem.
        _evalObject = evalObject;

        // Determine how many bits we will need in
        // each dimension of our problem.
        _numBitsPerDim = new int[ _evalObject.getDimSizes().length ];
        for( int i = 0; i < _evalObject.getDimSizes().length; i++ )
            _numBitsPerDim[ i ] = (int)Math.ceil( Math.log( _evalObject.getDimSizes()[ i ] ) / Math.log( 2.0D ) );

    }

    /** Compute self similarity */
    public float selfSim( ArrayList<Cr> population ) {
        int crLen = population.get( 0 ).getGenes().length();
        _selfSim = 0f;
        for( int iCol = 0; iCol < crLen; iCol++ ) {
            float colSum = 0;
            for( Cr cr : population )
                colSum += cr.getGenes().charAt( iCol ) == '1' ? 1 : 0;
            colSum /= population.size();
            _selfSim += colSum < 0.5f ? 1.0f - colSum : colSum;
        }
        _selfSim /= population.get( 0 ).getGenes().length();
        return _selfSim;
    }

    /** This method will use a tournament selection to
     *  choose a Cr for mating. The selection method that
     *  often appears in explanations of GAs is called
     *  roulette selection. That's where the chance that
     *  a given CR is chosen is determined by its fitness
     *  as a fraction of the sum of all fitness values.
     *  However, this method is computationally slow. A
     *  method that achieves nearly the same result is
     *  called tournament selection. It works as follows.
     *
     *  n Crs are chosen at random from the population.
     *  The one with the highest fitness wins the
     *  tournament and is chosen for selection. For
     *  example, if n = 2, we choose two Crs at random,
     *  determine which one has the higher fitness, and
     *  return it as our selection. This method will be
     *  called twice to get 2 Crs that will then be
     *  spliced with each other.
     *
     * @param n The number of Crs with which to run the
     *          tournament.
     * @return One Cr that has been chosen for mating.
     */
    protected Cr tournamentSelect( int n ) {
        int bestFitnessIndex = RandomGenerator.getInstance().nextInt( _population.size() );
        double bestFitness = _population.get( bestFitnessIndex ).getFitness();
        for( int j = 1; j < n; j++ ) {
            int index = RandomGenerator.getInstance().nextInt( _population.size() );
            if( _population.get( index ).getFitness() > bestFitness ) {
                bestFitnessIndex = index;
                bestFitness = _population.get( index ).getFitness();
            }
        }
        return _population.get( bestFitnessIndex );
    }

    /** Method used to splice two Crs together to create an
     *  offspring. Note that there may be more than one
     *  point at which the two will be spliced. This
     *  method chooses the splice points and the Crs
     *  do the actual splicing. This method uses the bit
     *  strings of Crs to perform the crossover. Other
     *  variants may use the actual values, e.g. the
     *  TSP problem variant.
     *
     * @param cr1
     * @param cr2
     * @return A new offspring Cr
     */
    protected Cr bitStringCrossover( Cr cr1, Cr cr2 ) {
        int[] splicePoints = new int[ _evalObject.getNumSplicePoints() ];
        for( int i = 0; i < _evalObject.getNumSplicePoints(); i++ )
            splicePoints[ i ] = (int)(
                    RandomGenerator.getInstance().nextFloat() *
                            cr1.getGenes().length()
            );
        // For the crossover to work, the splice points
        // have to be in ascending order.
        Arrays.sort( splicePoints );
        return cr1.spliceWith( cr2, splicePoints );
    }

    /** This is the value crossover described in the article to
     *  which I posted a link. The methodology is as follows.
     *  We choose an interval of the array of values of cr1.
     *  In the offspring, these will remain in the same
     *  place. The values of cr2 that were not chosen in cr1
     *  will then wrap around the values chosen in cr1 to
     *  create a new array of values. From this
     * @param cr1 The first candidate Cr
     * @param cr2 The second candidate Cr
     * @return An offspring Cr
     */
    protected Cr valueCrossover( Cr cr1, Cr cr2 ) {
        int[] startEnd = RndStartEndIndices( cr2.getValues().length );
        int[] values = Cr.OrderedCrossoverSplice(
                startEnd[ 0 ], // start
                startEnd[ 1 ], // end
                cr1.getValues(),
                cr2.getValues()
        );
        Cr offspring = Cr.FromValues(
                values,
                _evalObject.getDimSizes(),
                _numBitsPerDim
        );
        return offspring;
    }

    /** Creates a new population of candidate Crs by 1) saving
     *  the required number of best solutions, and 2) using
     *  tournament selection - selection in proportion to
     *  fitness - to splice together Crs until a newly created
     *  population is the same size as the old population.
     */
    public void repopulate() {

        // Note that the right way to do this is to keep two
        // array lists and re-use the discarded ones as we
        // alternate between them. That would obviate the
        // object creation we are doing here.
        ArrayList<Cr> newPopulation = new ArrayList<Cr>( _population.size() );

        // Add best solutions of last population
        int numBestToSave = _evalObject.getNumBestToSave();
        for( int i = 0; i < numBestToSave; i++ )
            newPopulation.add( _population.get( i ) );

        // To fill out the rest of the new population,
        // perform tournament selection and cross the
        // selected Cr's.
        while( newPopulation.size() < _population.size() ) {
            Cr cr1 = tournamentSelect( 2 );
            Cr cr2 = tournamentSelect( 2 );
            Cr offspring = null;
            if( ! _evalObject.isTSP() )
                offspring = this.bitStringCrossover( cr1, cr2 );
            else
                // Ordered crossover of the type described in
                // the article to which I posted a link.
                offspring = this.valueCrossover(cr1, cr2);
            newPopulation.add( offspring );
        }
        _population = newPopulation;
    }

    /** Get ready to run the GA */
    protected void resetVariables() {
        _selfSim = 0.0f;
        _population = new ArrayList<Cr>( _evalObject.getPopSize() );
        _iGen = -1;
        _bestFitness = 0;
        _bestFitnessGen = -1;
    }

    /** Call the eval object's computeFitness function for
     *  the entire population of solutions. It will assign
     *  a fitness to each. The population will then be
     *  sorted by fitness so we can later make use of the
     *  n best solutions.
     */
    protected void computeFitness() {
        // Call the eval object to compute the fitness
        // of each Cr in the _population list.


        //Create a thread pool for computing the fitness for every chromosome
        MyThreadPoolForGA threadPool=new MyThreadPoolForGA(4);
        //for loop for the chromosomes in the _population
        for(Cr cr:_population){
            //creating the task with a lambda of the computeFitness function
            TaskForThread task=new TaskForThread(()->_evalObject.computeFitness(cr));
            //put the task into the blocking queue, make it ready for being executed by different threads
            threadPool.execute(task);

        }
        //shut down the thread pool
        threadPool.shutdown();

        // We will now sort the population by the fitness
        // of the Crs.
        _population.sort(
                new Comparator<Cr>() {
                    @Override
                    public int compare(Cr o1, Cr o2) {
                        return o2.getFitness().compareTo( o1.getFitness() );
                    }
                }
        );
    }

    /** If we are using a bit string crossover,
     *  we have to translate from the string of
     *  a Cr to an array of values in each
     *  dimension. These values are unscaled so we
     *  will scale them to the maximum value allowed
     *  by the number of bits used to represent them.
     *  In other words, if we need 17 bits to represent a
     *  value between 0 and 69,999 inclusive (70,000
     *  states), the maximum value of the bits is
     *  2^17 or 131,072. We don't want values above
     *  69,999, so we have to scale the candidate
     *  values to our acceptable range.
     *
     * param numBitsPerDim An array that holds the number
     *                      of bits required to represent
     *                      a value in each dimension.
     */
    protected void decodeAndRescale() {

        for( int iCr = 0; iCr < _population.size(); iCr++ ) {
            // Call the decode method of the i-th Cr in
            // the population to obtain an array of values,
            // one in each dimension of our solution space.
            int[] values = _population.get( iCr ).decode( _numBitsPerDim );
            // Now, scale these values to the maximum allowed.
            // (See long explanation in Javadoc above.)
            // The scaling is different in each dimension of
            // the problem so we must iterate through the
            // dimensions.
            int[] dimSizes = _evalObject.getDimSizes();
            for( int iDim = 0; iDim < dimSizes.length; iDim++ ) {
                // First, retrieve the maximum allowed value
                // for this dimension (dimension j).
                double dimSize = (double) dimSizes[ iDim ];

                // Now, retrieve the actual value that the
                // Cr decoded for this dimension.
                double valueInDim = (double) ( values[ iDim ] );

                // Now, compute the maximum possible states
                // that can be represented using the number
                // of bits we are using for this dimension.
                // Note that instead of doing the calculation
                // on the fly, we can speed things up by
                // doing the calculation up front and
                // leaving the values in a static array.
                double maxValInDim = Math.pow( 2.0, (double) _numBitsPerDim[ iDim ] ) - 1.0D;

                // We are ready to scale.
                // Remember that dimSize is the max value
                // allowed for this dimension.
                values[ iDim ] = (int) Math.floor(
                        0.5 + ( dimSize - 1 )  * valueInDim / maxValInDim
                );
            }

            // Save the decoded, scaled values.
            _population.get( iCr ).setValues( values);
        }
    }

    protected void initializeRandomBits() {
        int popSize = _evalObject.getPopSize();
        for( int i = 0; i < popSize; i++ ) {

            // Instantiate a randomly generated Cr using
            // specifications of the number of bits in
            // each dimension of our problem. Add it

            Cr cr = new Cr( _numBitsPerDim );

            // Add new Cr to our population.
            _population.add( cr );
        }
    }

    /** We want to build an initial population of Crs using
     *  the Shuffler helper class. The Shuffler gives us
     *  random permutations of sequences, e.g. {1,2,3},
     *  {3,1,2}, etc.
     * @param dimSizes Number of dimensions in our problem
     */
    public void initializeTSP( int[] dimSizes) {
        Shuffler shuffler = new Shuffler( _numBitsPerDim.length );
        while( _population.size() < _evalObject.getPopSize() ) {
            int[] values = shuffler.next();
            Cr cr = Cr.FromValues( values, _evalObject.getDimSizes(), _numBitsPerDim );
            _population.add( cr );
        }
    }

    /** Build a random population of Crs */
    protected void initializePopulation() {
        if( _evalObject.isTSP() )
            // The TSP specific variant - values not bits
            this.initializeTSP( _evalObject.getDimSizes() );
        else
            // The normal variant
            this.initializeRandomBits();
    }

    /** Mutate - flip the bits - of some members of
     *  the population. This lowers the chances that
     *  the GA will get stuck in a local minima or
     *  maxima.
     *
     *  Note that We don't want to mutate the first
     *  n elements of the population because we
     *  deliberately saved them as best solutions from
     *  the previous generation. Hence, we start with
     *  element n.
     */
    protected void mutate() {
        float mutationRate = _evalObject.getMutationRate();
        for( int i = _evalObject.getNumBestToSave(); i < _population.size(); i++ )
            if( !_evalObject.isTSP() )
                // Bit string mutation
                _population.get( i ).mutate( mutationRate );
            else
                // Ordered mutation - the TSP variant
                _population.get( i ).orderedMutation( _numBitsPerDim, _evalObject.getMutationRate() );
    }

    /** Output the best solution we've found so far.
     */
    protected void printBestFitness() {
        int [] values = _population.get( 0 ).getValues();
        int numDims = values.length;
        System.out.format( "%d\t%f\t%d", _iGen, _selfSim, values[ 0 ] );
        for( int i = 1; i < numDims; i++ )
            System.out.format( ", %s", values[ i ] );
        System.out.print( "\n" );
    }

    /** Update generation counter and save new best
     *  fitness if it is better than previous best
     *  fitness.
     */
    protected void updateStats() {
        _iGen++;
        double bestFitness = _population.get( 0 ).getFitness();
        if( bestFitness > _bestFitness ) {
            _bestFitnessGen = _iGen;
            _bestFitness = bestFitness;
        }

    }

    /**
     * Returns true if either self similarity is above
     * target self similarity or the best solution has
     * not changed in a specified maximum number of
     * generations.
     *
     * @return true if one of the stopping criteria has
     *         been achieved or false otherwise.
     */
    protected boolean isReadyToStop() {
        return ( selfSim( _population ) >= _evalObject.getSelfSimTarget() )
                || ( _iGen - _bestFitnessGen > _evalObject.getMaxProgressPause() );
    }

    /** Method called to start the GA search.
     *
     *  Make an initially random population, evaluate its
     *  fitness, select Crs for mating to re-populate,
     *  probabilistically mutate some Crs and continue
     *  loop until population achieves one of its
     *  stopping criteria.
     */
    public void go() {

        // Reset the generation counter and set up the
        // data structures to hold population and
        // related states.
        this.resetVariables();

        // Build random population.
        this.initializePopulation();

        // Loop until stopping criterion is achieved.
        while( true ) {

            // If this is a TSP problem, we don't need to rescale.
            // The values are already rescaled. But if it's a
            // normal GA operating on bit strings, we need to
            // rescale.
            if( ! _evalObject.isTSP() )
                this.decodeAndRescale();

            // Compute fitness of each Cr in population.
            this.computeFitness();

            // Save stats for this generation, if any.
            this.updateStats();

            // Check for self similarity target threshold.
            if( this.isReadyToStop() )
                break;

            // Print out best fitness
            this.printBestFitness();

            // Save best and repopulate using tournament selection
            this.repopulate();

            // Mutate some members of the population.
            this.mutate();


        } // while( true ) {...}

    } // public void go() {...}

    /** Return an array list of Crs in the GA's population */
    public ArrayList<Cr> getPopulation() {
        return _population;
    }

    /** A convenience method for generating random start and end
     *  indices into an array of int values.
     * @param len The length of the array into which we want
     *            indices
     * @return An array containing a start index at array[ 0 ] and
     *         an end index at array[ 1 ]
     */
    public static int[] RndStartEndIndices( int len ) {
        int[] indices = {
                RandomGenerator.getInstance().nextInt( len ),
                RandomGenerator.getInstance().nextInt( len )
        };
        Arrays.sort( indices );
        return indices;
    }

}
