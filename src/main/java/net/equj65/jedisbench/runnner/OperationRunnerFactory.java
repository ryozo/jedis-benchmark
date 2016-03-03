package net.equj65.jedisbench.runnner;

import net.equj65.jedisbench.context.BenchmarkContext;
import net.equj65.jedisbench.enums.Command;
import net.equj65.jedisbench.generator.KeyGenerator;
import net.equj65.jedisbench.generator.impl.FixedKeyGenerator;
import net.equj65.jedisbench.generator.impl.RandomKeyGenerator;
import net.equj65.jedisbench.runnner.impl.GetOperationRunner;
import net.equj65.jedisbench.runnner.impl.SetOperationRunner;
import net.equj65.jedisbench.runnner.impl.WaitReplicasOperationRunner;
import net.equj65.jedisbench.util.ReplicationUtils;
import redis.clients.jedis.JedisPool;

import java.io.StringWriter;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

/**
 * This class is factory of {@link OperationRunner} class.
 *
 * @author ryozo
 */
public class OperationRunnerFactory {

    /**
     * Create a new instance of the {@link OperationRunner} to run the specified benchmarks.
     *
     * @param command RedisCommand of benchmark target.
     * @param context Condition of benchmark.
     * @param pool The JedisPool instance for OperationRunner to use it.
     * @param latch CountDownLatch that records the number of benchmark samples.
     * @return created OperationRunner instance.
     */
    public static OperationRunner createRunnerOf(Command command, BenchmarkContext context,
                                                 JedisPool pool, CountDownLatch latch) {
        // TODO refactor
        KeyGenerator generator = createKeyGeneratorOf(context);
        String data = fillString(context.getDataSize());

        OperationRunner runner = null;
        if (Command.SET == command) {
            runner = new SetOperationRunner(pool, latch, data, generator);
        } else if (Command.GET == command) {
            runner = new GetOperationRunner(pool, latch, data, generator);
        } else {
            throw new IllegalArgumentException(String.format("Command [%s] is not supported.", command));
        }

        // decorate
        if (isUseWaitReplicas(command, context)) {
            return new WaitReplicasOperationRunner(runner, pool,
                    context.getAcknowledgedReplicas(), context.getWaitTimeout());
        }
        return runner;
    }

    /**
     * Create a {@link KeyGenerator} from specified {@link BenchmarkContext}.
     *
     * @param context
     * @return
     */
    private static KeyGenerator createKeyGeneratorOf(BenchmarkContext context) {
        if (1 < context.getKeySpaceLen()) {
            return new RandomKeyGenerator(context.getKeySpaceLen());
        }
        return new FixedKeyGenerator();
    }

    /**
     * This method create a string of the specified size.
     *
     * @param lengthOfFill generate string size
     * @return
     */
    private static String fillString(int lengthOfFill) {
        return new StringWriter() {{
            IntStream.range(0, lengthOfFill).forEach(i -> write("a"));
        }}.toString();
    }

    /**
     * Confirm whether specified Command and BenchmarkContext are a target of WaitReplicas.
     */
    private static boolean isUseWaitReplicas(Command command, BenchmarkContext context) {
        return ReplicationUtils.isWaitableCommand(command)
                && 1 <= context.getAcknowledgedReplicas();
    }

}
