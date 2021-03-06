package net.equj65.jedisbench.runnner;

import net.equj65.jedisbench.context.BenchmarkContext;
import net.equj65.jedisbench.enums.Command;
import net.equj65.jedisbench.generator.KeyGenerator;
import net.equj65.jedisbench.generator.impl.FixedKeyGenerator;
import net.equj65.jedisbench.generator.impl.RandomKeyGenerator;
import net.equj65.jedisbench.mediator.StartSignal;
import net.equj65.jedisbench.runnner.impl.GetOperationRunner;
import net.equj65.jedisbench.runnner.impl.SetOperationRunner;
import net.equj65.jedisbench.runnner.impl.WaitReplicasSetOperationRunner;
import redis.clients.jedis.JedisPool;

import java.io.StringWriter;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

/**
 * Created by ryozo on 2016/02/08.
 */
public class OperationRunnerFactory {

    // TODO refactor
    public static OperationRunner createRunnerOf(Command command, BenchmarkContext context,
                           StartSignal startSignal, JedisPool pool, CountDownLatch latch) {
        KeyGenerator generator = createKeyGeneratorOf(context);
        String data = fillString(context.getDataSize());
        switch (command) {
            case SET:
                if (isUseWaitReplicas(context)) {
                    return new WaitReplicasSetOperationRunner(startSignal, pool, latch, data, generator,
                            context.getAcknowledgedReplicas(), context.getWaitTimeout());
                }
                return new SetOperationRunner(startSignal, pool, latch, data, generator);
            case GET:
                return new GetOperationRunner(startSignal, pool, latch, data, generator);
        }
        throw new IllegalArgumentException(String.format("Command [%s] is not supported.", command));
    }

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

    private static boolean isUseWaitReplicas(BenchmarkContext context) {
        return 1 <= context.getAcknowledgedReplicas();
    }

}
