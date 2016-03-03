package net.equj65.jedisbench.generator;

/**
 * This class represents the behavior of generating redis key.
 *
 * @author ryozo
 */
public interface KeyGenerator {

    /**
     * Generate a Redis key in accordance with the benchmark rule.
     * Please use this key to Redis operation with Key.
     * (For example. GET/SET/INCR etc..)
     * @return key
     */
    String generateKey();

    /**
     * Return a last generated key at this instance.
     * @return last generated key
     */
    String currentKey();

}
