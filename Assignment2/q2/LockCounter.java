

/**
 * Provides the counting interface and uses our locks.
 */
public class LockCounter extends Counter {
    private MyLock lock;
    private int count;

    public LockCounter(MyLock mainLock) {
        lock = mainLock;
        count = 0;
    }

    /**
     * Follows the simple algorithm to guarantee mutual exclusion:
     *      lock();
     *      [critical section code]
     *      unlock();
     * For our case, the critical section code is just a simple increment.
     */
    @Override
    public void increment() {
        lock.lock(new Integer(CounterThread.currentThread().getName()));
        ++count;
        lock.unlock(new Integer(CounterThread.currentThread().getName()));
    }

    @Override
    public int getCount() {
        return count;
    }
}
