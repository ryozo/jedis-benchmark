package net.equj65.jedisbench.runnner.impl;

import net.equj65.jedisbench.generator.KeyGenerator;
import net.equj65.jedisbench.runnner.OperationRunner;
import net.equj65.jedisbench.runnner.OperationRunnerDecorator;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * This class provides Benchmark by Jedis any set Operation
 * with the {@link Jedis#waitReplicas(int, long)} Command.
 * When instantiate this class, plz specify the {@link OperationRunner} instance
 * of the waitReplicas target to constructor.
 * When the specified {@link OperationRunner#call} is finished,
 * this class execute {@code waitReplicas}.
 *
 * @author ryozo
 */
public class WaitReplicasOperationRunner extends OperationRunnerDecorator {

    private JedisPool pool;
    private KeyGenerator keyGenerator;
    private int acknowledgeReplicas;
    private int timeoutMillis;

    // TODO Refactor
    public WaitReplicasOperationRunner(OperationRunner runner, JedisPool pool,
                                       int acknowledgeReplicas, int timeoutMillis) {
        super(runner);
        this.pool = pool;
        this.acknowledgeReplicas = acknowledgeReplicas;
        this.timeoutMillis = timeoutMillis;
    }

    @Override
    public void operation() throws Exception {
        runner.call();
        // TODO ここで利用されるJedisインスタンスはRunner内のJedisと異なる可能性 -> 念のため問題ないか確認
        try (Jedis jedis = pool.getResource()) {
            jedis.waitReplicas(acknowledgeReplicas, timeoutMillis);
            // TODO Count the synchronization of less than the specified number
        }
    }
}
