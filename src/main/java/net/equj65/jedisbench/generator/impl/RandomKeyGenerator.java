package net.equj65.jedisbench.generator.impl;

import net.equj65.jedisbench.generator.KeyGenerator;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ryozo on 2016/02/14.
 */
public class RandomKeyGenerator implements KeyGenerator {
    private static final String KEY_PREFIX = "jedis-benchmark#";
    private AtomicInteger key = new AtomicInteger();
    private int keyRange;
    public RandomKeyGenerator(int keyRange) {
        this.keyRange = keyRange;
    }

    @Override
    public String generateKey() {
        // TODO Investigate the performance impact.
        return KEY_PREFIX.concat(String.valueOf(key.getAndUpdate(x -> x >= keyRange - 1 ? 0 : x + 1)));
    }
}
