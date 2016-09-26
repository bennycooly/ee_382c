

public class Main {
    public static void main (String[] args) {
        Counter counter;
        MyLock lock;
        long executeTimeMS = 0;
        int numThreads = 6;
        int numTotalInc = 1200000;

        if (args.length < 3) {
            System.err.println("Provide 3 arguments");
            System.err.println("\t(1) <algorithm>: fast/bakery/synchronized/"
                    + "reentrant");
            System.err.println("\t(2) <numThread>: the number of test thread");
            System.err.println("\t(3) <numTotalInc>: the total number of "
                    + "increment operations performed");
            System.exit(-1);
        }

        numThreads = Integer.parseInt(args[1]);
        numTotalInc = Integer.parseInt(args[2]);

        if (args[0].equals("fast")) {
            lock = new FastMutexLock(numThreads);
            counter = new LockCounter(lock);
        } else if (args[0].equals("bakery")) {
            lock = new BakeryLock(numThreads);
            counter = new LockCounter(lock);
        } else {
            counter = null;
            System.err.println("ERROR: no such algorithm implemented");
            System.exit(-1);
        }


        // TODO
        // Please create numThread threads to increment the counter
        // Each thread executes numTotalInc/numThread increments
        // Please calculate the total execute time in millisecond and store the
        // result in executeTimeMS

        // create the threads
        CounterThread[] threads = new CounterThread[numThreads];
        long startTime = System.nanoTime();
        for (int i = 0; i < numThreads; ++i) {
            CounterThread th = new CounterThread(counter, numTotalInc / numThreads, i);
            th.setName((new Integer(i)).toString());
            th.start();
            threads[i] = th;
        }

        // join the threads to wait
        try {
            for (Thread th : threads) {
                th.join();
            }
            // stop the timer immediately
            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            executeTimeMS = elapsedTime / 1000000;

            // debugging print to make sure we reached the total count
            System.out.println("Total count reached " + counter.getCount());

        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        // print the results to stdout
        System.out.println(
                "Total elapsed time for " +
                args[0] + " algorithm with " +
                args[1] + " threads and an increment count of " +
                args[2] + " was " +
                executeTimeMS + "ms"
        );
    }
}
