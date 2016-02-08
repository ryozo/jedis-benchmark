package net.equj65.jedisbench.runnner.impl;


import net.equj65.jedisbench.runnner.OperationRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.CountDownLatch;

/**
 * Created by ryozo on 2016/02/08.
 */
public class GetOperationRunner extends OperationRunner {

    private JedisPool pool;
    private CountDownLatch latch;
    private String value;

    public GetOperationRunner(JedisPool pool, CountDownLatch latch, String value) {
        this.pool = pool;
        this.latch = latch;
        this.value = value;
    }

    @Override
    public Void call() throws Exception {
        String key = Thread.currentThread().getName();
        try (Jedis jedis = pool.getResource()) {
            jedis.set(key, value);
        }

        while (true) {
            try (Jedis jedis = pool.getResource()) {
                jedis.get(key);
            }
            latch.countDown();
            if (Thread.currentThread().isInterrupted()) {
                // Thread shutdown.
                break;
            }
        }
        return null;
    }
}