
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Lampart's Fast Mutex Lock implementation for a simple lock.
 */
public class FastMutexLock implements MyLock {

    private AtomicInteger x;
    private AtomicInteger y;
    private AtomicIntegerArray flag;

    public FastMutexLock(int numThreads) {
        flag = new AtomicIntegerArray(numThreads);
        for (int i = 0; i < flag.length(); ++i) {
            flag.set(i, 0);            // initialize all request statuses to 0
        }
        x = new AtomicInteger(-1);
        y = new AtomicInteger(-1);
    }

    /**
     * Locking algorithm. Follows the notes described in lecture.
     * @param myId
     */
    @Override
    public void lock(int myId) {
        while (true) {
            flag.set(myId, 1);
            x.set(myId);
            if (y.get() != -1) {                // door is closed, we need to try again
                flag.set(myId, 0);
                while (y.get() != -1) {}
            }
            else {  // door is open
                y.set(myId);
                if (x.get() == myId) {          // fast lock path since we're the only one in queue
                    return;
                }
                else {  // go into the other splitter
                    flag.set(myId, 0);
                    for (int j = 0; j < flag.length(); ++j) {   // we must wait until all other threads are done requesting
                        while (flag.get(j) != 0) {}
                    }
                    if (y.get() == myId) {      // slow lock path
                        return;
                    }
                    else {
                        while (y.get() != -1) {}
                    }
                }
            }
        }
    }

    /**
     * To unlock, we just need to set the slow lock value to -1 (free)
     * @param myId
     */
    @Override
    public void unlock(int myId) {
        y.set(-1);
        flag.set(myId, 0);
    }
}
