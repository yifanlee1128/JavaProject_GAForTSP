package Assignment7;

import java.util.ArrayList;

public class ThreadTester {

	static int nProcessors = Runtime.getRuntime().availableProcessors(); // Get the number of processors on this machine
	
	public static void calculate( double workUnitsPerThread ) {
    	long nCalculations = (long)( workUnitsPerThread * (double) 1500000000 ); // 1.5 billion loops
    	int j = 7;
    	for( long iCalculation = 0; iCalculation < nCalculations; iCalculation ++ ) {
			j = j + j;
			if( j % 2 == 0 )
				j = j + 1;
    	}
	}
	
	public static long checkNThreads( int nThreads ) {
		System.out.print( "Measuring time required to solve problem with " + nThreads + " thread(s): " );
		final double workUnitsPerThread = (double) nProcessors / (double) nThreads;
		long start = System.currentTimeMillis();
		Thread[] threads = new Thread[ nThreads ];
		for( int iThread = 0; iThread < nThreads; iThread++ ) {
			threads[ iThread ] = new Thread(
				new Runnable() {
			        public void run() {
			        	calculate( workUnitsPerThread );
			        }
			    }
			);
			threads[ iThread ].start();
		}
		for( int iThread = 0; iThread < nThreads; iThread++ ) {
			try {
				threads[ iThread ].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		long end = System.currentTimeMillis();
		long elapsed = end - start;
		System.out.println( elapsed + " milliseconds" );
		return elapsed;
	}

	public static ArrayList<Long> checkUpToNThreads( int nMaxThreads ) {
		ArrayList<Long> results = new ArrayList<Long>( nMaxThreads );
		for( int nThreads = 1; nThreads <= nMaxThreads; nThreads++ ) {
			results.add( checkNThreads( nThreads ) );
		}
		return results;
	}
	
	public static void main( String[] args ) {
		int nMaxThreads = nProcessors * 2;  
		ArrayList<Long> results = checkUpToNThreads( nMaxThreads );
		System.out.println( results );
	}
	
}
