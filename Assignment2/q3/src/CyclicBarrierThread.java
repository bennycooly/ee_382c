import java.util.Random;

/**
 * Sample runnable thread that demonstrates the use of CyclicBarrier.
 */
public class CyclicBarrierThread extends Thread {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " started");
        Random rand = new Random();
        int sleepTime = rand.nextInt(1500);
        try {
            Thread.sleep(sleepTime);
            int barrierIndex = CyclicBarrierRunner.barrier.await();
            System.out.println(Thread.currentThread().getName() + " has barrier index " + barrierIndex);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
