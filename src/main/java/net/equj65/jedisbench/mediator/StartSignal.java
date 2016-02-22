package net.equj65.jedisbench.mediator;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * This class synchronize that start of benchmark operations.
 * When all of the thread was able to start preparation, perform
 * the signal of the start.
 * It is like a Traffic light to the signal of the start of the race.
 *
 * @author ryozo
 */
public class StartSignal {
    private CyclicBarrier cyclicBarrier;
    private volatile long startTime;
    private volatile boolean started;
    public StartSignal(int numberOfBenchmarkThreads) {
        this.cyclicBarrier = new CyclicBarrier(numberOfBenchmarkThreads);
    }
    public void awaitStart() {
        try {
            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
        synchronized(this) {
            if (!started) {
                startTime = System.currentTimeMillis();
                started = true;
            }
        }
    }
    public long getStartTime() {
        if (!started) throw new RuntimeException("Benchmark is not started.");
        return startTime;
    }
}
