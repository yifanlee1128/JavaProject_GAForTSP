package Assignment7;

import java.util.ArrayList;

public class MyTest_GA3 extends junit.framework.TestCase {

    /*
    * I change the sequence of the points in HalfCircle,named "HalfCircle1",
    * so the final result should be either 6,7,8,9,0,1,2,3,4,5 or 5,4,3,2,1,0,9,8,7,6
    * */
    public void test1() {

        /** Our solution space has 10! possible states
         */
        final int[] _dimSizes = new int[10];
        for (int i = 0; i < _dimSizes.length; i++)
            _dimSizes[i] = 10;

        final Circle _circle = new Circle(Circle.HalfCircle1);

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
                for (Cr cr : population) {
                    int values[] = cr.getValues();
                    double fitness = _circle.fitness(values);
                    cr.setFitness(fitness);
                }

            }

            @Override
            synchronized public void computeFitness(Cr chromosome) {
                int values[] = chromosome.getValues();
                double fitness = _circle.fitness(values);
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
        GAWithThreads ga = new GAWithThreads(evalObject);
        ga.go();
    }
}
