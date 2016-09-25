import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Semaphore;

/**
 * An example implementation of Java's CyclicBarrier class.
 */

public class CyclicBarrier {

    private int total;
    private int index;
    private Semaphore semaphore;

    /**
     * Constructor that initializes the barrier with a certain amount of threads to wait for.
     * @param parties   the number of threads for the barrier to wait for
     */
    public CyclicBarrier(int parties) {
        total = parties;
        index = parties;
        semaphore = new Semaphore(0);
    }

    /**
     * Wait for all threads to call it and then releases the resources.
     * @return                          the index of the thread's arrival depending on when it called await().
     *                                      (total - 1) is the first thread to call await(), and
     *                                      0 is the last thread to call await().
     * @throws InterruptedException     the exception that is thrown when a thread is interrupted while waiting
     */
    public int await() throws InterruptedException {
        System.out.println("await called by " + Thread.currentThread().getName());
        int curIndex = --index;             // save the index and decrement the total barrier index
        if (index == 0) {
            semaphore.release(total);
            System.out.println("Barrier unlocked");
        }
        semaphore.acquire();
        return curIndex;
    }
}
