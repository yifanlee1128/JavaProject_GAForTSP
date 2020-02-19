package Assignment7;

public class TaskForThread implements Runnable {
    /*
    * This class is used to store the function we need to run in the GA,
    * specifically, the function for calculating the fitness of each chromosome is stored in this class,
    * we can use "run()" function to realize the functionality.
    * */
    Runnable _runnable;

    public TaskForThread(Runnable runnable){
        _runnable=runnable;
    }
    @Override
    public void run(){
        _runnable.run();
    }
}
