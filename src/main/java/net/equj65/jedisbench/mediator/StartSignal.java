package net.equj65.jedisbench.mediator;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by ryozo on 2016/02/20.
 */
public class StartSignal {
    private static CyclicBarrier cyclicBarrier;
    private volatile long startTime;
    private volatile boolean started;
    public StartSignal(CyclicBarrier barrier) {
        this.cyclicBarrier = barrier;
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
