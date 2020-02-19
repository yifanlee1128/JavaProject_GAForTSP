In this assignment,I create the following files:

A. TaskForThread.java 
It contains a class use to store the function we need to run in the GA

B. MyThreadPoolForGa.java
It is a thread pool for the GA, containing an array of threads and a blocking queue use to manage the existing tasks

C. GAWithThreads.java
It is a GA class with the implementation of the ThreadPool in the function "computeFitness"

D. MyTest_ThreadPool_and_TaskForThread.java
It is a junit test used to check whether the ThreadPool class and TaskForThread class work.

E. MyTest_GA3.java
It is a junit test used to check whether we can get the right TSP solution through GA method with the implementation of multiple threads(thread pool) 

Besides the classed I mentioned above, I add one method in the Interface "I_EvalObject.java", the signature of the method is "void computeFitness(Cr chromosome)", it is the overload method of the method "void computeFitness(ArrayList<Cr> population)". I create this new method because when implementing thread pool on the GA, I want to use "for" loop on the "_population" to make the computation of the fitness for every chromosome become a seperate task put in the blocking queue.

To test the effectiveness of GA on TSP problem, we can just run MyTest_GA3.java to see the result. the exmaple I used in the Test is a little bit different from the "HalfCircle", because the one I uses in MyTest_GA3 is "HalfCircle1" with same elements but in different orders. So the ideal result should be either "6,7,8,9,0,1,2,3,4,5" or "5,4,3,2,1,0,9,8,7,6".