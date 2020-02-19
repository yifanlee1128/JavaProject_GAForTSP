package Assignment7;

public class MyTest_ThreadPool_and_TaskForThread extends junit.framework.TestCase {
    //Test whether the Task class works
    public void test_TaskForThread(){
        TaskForThread task=new TaskForThread(()->System.out.println("This task is running!"));
        task.run();
    }
    //Test whether the ThreadPool class works
    public void test_MyThreadPoolForGA(){
        MyThreadPoolForGA threadPool=new MyThreadPoolForGA(4);
        TaskForThread task1=new TaskForThread(()->System.out.println("Task1 is running!"));
        TaskForThread task2=new TaskForThread(()->System.out.println("Task2 is running!"));
        TaskForThread task3=new TaskForThread(()->System.out.println("Task3 is running!"));
        threadPool.execute(task1);
        threadPool.execute(task2);
        threadPool.execute(task3);
        threadPool.shutdown();
    }
}
