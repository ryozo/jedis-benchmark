package net.equj65.jedisbench.runnner.impl;

import net.equj65.jedisbench.generator.KeyGenerator;
import net.equj65.jedisbench.runnner.OperationRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.CountDownLatch;

/**
 * This class provides Benchmark by Jedis {@code set} Operation with the {@link Jedis#waitReplicas(int, long)}Command.
 *
 * @author ryozo
 */
public class WaitReplicasSetOperationRunner extends OperationRunner {

    private JedisPool pool;
    private CountDownLatch latch;
    private String value;
    private KeyGenerator keyGenerator;
    private int acknowledgeReplicas;
    private int timeoutMillis;

    // TODO Refactor
    public WaitReplicasSetOperationRunner(JedisPool pool, CountDownLatch latch, String value,
                                          KeyGenerator keyGenerator, int acknowledgeReplicas, int timeoutMillis) {
        this.pool = pool;
        this.latch = latch;
        this.value = value;
        this.keyGenerator = keyGenerator;
        this.acknowledgeReplicas = acknowledgeReplicas;
        this.timeoutMillis = timeoutMillis;
    }

    // TODO Eliminating duplicate code
    @Override
    public void operation() throws Exception {
        try (Jedis jedis = pool.getResource()) {
            jedis.set(keyGenerator.generateKey(), value);
            jedis.waitReplicas(acknowledgeReplicas, timeoutMillis);
            // TODO Count the synchronization of less than the specified number
        }
        latch.countDown();
    }
}
