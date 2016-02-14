package net.equj65.jedisbench.generator.impl;

import net.equj65.jedisbench.generator.KeyGenerator;

/**
 * Created by ryozo on 2016/02/14.
 */
public class FixedKeyGenerator implements KeyGenerator {

    /** FIXED KEY */
    private static final String FIXED_KEY = "jedis-benchmark#fixed-key";

    @Override
    public String generateKey() {
        return FIXED_KEY;
    }
}
