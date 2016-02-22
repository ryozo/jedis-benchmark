package net.equj65.jedisbench.runnner;

import net.equj65.jedisbench.mediator.StartSignal;

import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Created by ryozo on 2016/02/08.
 */
public abstract class OperationRunner implements Callable<Void> {

    private StartSignal startSignal;

    public OperationRunner(StartSignal startSignal) {
        this.startSignal = Objects.requireNonNull(startSignal);
    }

    /**
     * All benchmark threads ready, when Start
     */
    @Override
    public final Void call() throws Exception {
        // Start all at once when Benchmark's ready.
        startSignal.awaitStart();
        operation();
        return null;
    }

    protected abstract void operation() throws Exception;
}
