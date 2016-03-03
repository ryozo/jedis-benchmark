package net.equj65.jedisbench.runnner;

/**
 * This class is Decorator base of {@link OperationRunner}.
 *
 * @author ryozo
 */
public abstract class OperationRunnerDecorator extends OperationRunner {
    protected OperationRunner runner;
    public OperationRunnerDecorator(OperationRunner runner) {
        this.runner = runner;
    }

    @Override
    protected void operation() throws Exception {
        runner.call();
    }
}
