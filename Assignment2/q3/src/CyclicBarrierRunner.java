/**
 * Contains the main fucntino to run our CyclicBarrier example.
 */


public class CyclicBarrierRunner {

    private static final int NUM_THREADS = 3;

    /**
     * Our instance of the cyclic barrier. We must make this public and static so our threads can access it.
     */
    public static CyclicBarrier barrier;

    public static void main(String[] args) throws InterruptedException {
        // write your code here
        CyclicBarrierRunner instance = new CyclicBarrierRunner();
        instance.initThreads(NUM_THREADS);
    }

    /**
     * Initialize the specified number of threads and run them.
     * @param numThreads            the number of threads to create
     * @throws InterruptedException the exception when wait() gets interrupted
     */
    private void initThreads(int numThreads) throws InterruptedException {
        barrier = new CyclicBarrier(numThreads);
        for (int i = 0; i < numThreads; ++i) {
            CyclicBarrierThread th = new CyclicBarrierThread();
            th.start();
        }
    }
}
