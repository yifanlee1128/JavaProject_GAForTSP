package Assignment7;

import java.util.concurrent.LinkedBlockingQueue;

public class MyThreadPoolForGA{

    /*
    * This is the thread pool for GA:
    * _poolsize is use to define the number of threads in the thread pool;
    * _threads is an array of available threads, used as the thread pool;
    * _queue is a blocking queue used to manage the existing task, it is an efficient way to
    * implement communication between objects without using wait/notify
    *
    * */
    private final int _poolSize;
    private final MyThreadForGA[] _threads;
    private final LinkedBlockingQueue<Runnable> _queue;

    /*
    * Constructor of the class,
    * instantiate the fields,
    * create the required number of threads.
    * */
    public MyThreadPoolForGA(int poolSize){
        this._poolSize=poolSize;
        this._queue=new LinkedBlockingQueue<Runnable>();
        this._threads=new MyThreadForGA[poolSize];
        for(int i=0;i<_poolSize;i++){
            _threads[i]=new MyThreadForGA();
            _threads[i].start();
        }
    }
    //method used to put a task in the blocking queue
    public void execute(Runnable task){
        synchronized (_queue){
            _queue.add(task);
            _queue.notify();
        }
    }
    //a private class for thread
    private class MyThreadForGA extends Thread{
        @Override
        public void run(){
            Runnable task;
            while(true){

                //we use the keyword "synchronized" to make the threads safe
                synchronized (_queue){

                    //while queue is empty of tasks, let it wait
                    while (_queue.isEmpty()){
                        try {
                            _queue.wait();
                        }catch (InterruptedException e) {
                            System.out.println("An error occurred while queue is waiting: "+e.getMessage());
                        }
                    }
                    //retrieve one task from the blocking queue
                    task=_queue.poll();
                }
                //run the task
                try {
                    task.run();
                }catch (RuntimeException e){
                    System.out.println("Thread pool is interrupted due to an issue: "+e.getMessage());
                }
            }
        }

    }

    //a method to shut the threads down,
    // it makes sue the state of every thread is waiting when we use the shutdown method
    public void shutdown(){
        for(int i=0;i<_poolSize;i++) {
            while(_threads[i].getState()!=Thread.State.WAITING){}
        }

    }

}
