/**
 * Created by ben on 9/26/2016.
 */
public class CounterThread extends Thread {
    private Counter counter;
    private int numInc;
    private int pid;

    public CounterThread(Counter mainCounter, int mainNumInc, int mainPid) {
        counter = mainCounter;
        numInc = mainNumInc;
        pid = mainPid;
    }

    public void run() {
        System.out.println("Thread " + Thread.currentThread().getName() + " started.");
        for (int i = 0; i < numInc; ++i) {
            counter.increment();
            // System.out.println("Thread " + Thread.currentThread().getName() + ": " + counter.getCount());
        }
    }

    public int getPid() {
        return pid;
    }
}
