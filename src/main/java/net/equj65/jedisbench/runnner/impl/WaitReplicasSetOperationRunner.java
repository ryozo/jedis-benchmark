package net.equj65.jedisbench.runnner.impl;

import net.equj65.jedisbench.generator.KeyGenerator;
import net.equj65.jedisbench.mediator.StartSignal;
import net.equj65.jedisbench.runnner.OperationRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

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
    private AtomicLong numberOfFailedWaitReplicas;

    // TODO Refactor
    public WaitReplicasSetOperationRunner(StartSignal startSignal, JedisPool pool, CountDownLatch latch,
                                          String value, KeyGenerator keyGenerator, int acknowledgeReplicas, int timeoutMillis) {
        super(startSignal);
        this.pool = pool;
        this.latch = latch;
        this.value = value;
        this.keyGenerator = keyGenerator;
        this.acknowledgeReplicas = acknowledgeReplicas;
        this.timeoutMillis = timeoutMillis;
        this.numberOfFailedWaitReplicas = new AtomicLong();
    }

    // TODO Eliminating duplicate code
    @Override
    public void operation() throws Exception {
        while (true) {
            try (Jedis jedis = pool.getResource()) {
                jedis.set(keyGenerator.generateKey(), value);
                long numberOfReflectedReplicas = jedis.waitReplicas(acknowledgeReplicas, timeoutMillis);
                if (numberOfReflectedReplicas < acknowledgeReplicas) {
                    numberOfFailedWaitReplicas.incrementAndGet();
                }
            }
            latch.countDown();
            if (Thread.currentThread().isInterrupted()) {
                // Thread shutdown.
                break;
            }
        }
    }
}
