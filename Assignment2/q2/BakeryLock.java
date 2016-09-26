
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Bakery implementation for a simple lock.
 */
public class BakeryLock implements MyLock {
    private AtomicIntegerArray entering;
    private AtomicIntegerArray number;

    public BakeryLock(int numThreads) {
        entering = new AtomicIntegerArray(numThreads);
        number = new AtomicIntegerArray(numThreads);
        for (int i = 0; i < numThreads; ++i) {
            entering.set(i, 0);         // boolean false
            number.set(i, 0);           // set all to 0 initially
        }
    }

    /**
     * Locking algorithm. Follows the notes described in lecture.
     * @param myId
     */
    @Override
    public void lock(int myId) {
        entering.set(myId, 1);
        number.set(myId, 1 + max(number));  // we set the current pid to the max value and add 1 in the case that it's the first
        entering.set(myId, 0);
        for (int j = 0; j < entering.length(); ++j) {
            // we must wait until the thread gets its number
            while (entering.get(j) == 1) {}

            // now we wait until we are the lowest number
            while (
                    (number.get(j) != 0) &&
                    (number.get(j) < number.get(myId)) ||
                    (number.get(j) == number.get(myId) && j < myId)
                    ) {}
        }
    }

    /**
     * To unlock, we just reset the thread's number to 0.
     * @param myId
     */
    @Override
    public void unlock(int myId) {
        number.set(myId, 0);
    }

    /**
     * Finds the maximum value of the array.
     * @param arr   the input array
     * @return      the maximum value of the array
     */
    private int max(AtomicIntegerArray arr) {
        int max = arr.get(0);
        for (int i = 1; i < arr.length(); ++i) {
            int cur = arr.get(i);
            if (cur > max) {
                max = cur;
            }
        }
        return max;
    }
}
