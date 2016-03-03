package net.equj65.jedisbench.runnner;

import java.util.concurrent.Callable;

/**
 * Created by ryozo on 2016/02/08.
 */
public abstract class OperationRunner implements Callable<Void> {

    /**
     * All benchmark threads ready, when Start
     */
    @Override
    public final Void call() throws Exception {
        operation();
        return null;
    }

    protected abstract void operation() throws Exception;
}
