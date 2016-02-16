package net.equj65.jedisbench.context;

import lombok.Data;

/**
 * This class represent the benchmark conditions.
 *
 * @author ryozo
 */
@Data
public class BenchmarkContext {
    /** Benchmark target hostname */
    private String hostname;
    /** Server port */
    private int port;
    /** Number of parallel connections */
    private int threads;
    /** Data size of SET/GET value in bytes */
    private int dataSize;
    /** Total number of request */
    private int requests;
    /** Use random key. Specify the variation of key */
    private int keySpaceLen;
    /** The number of acknowledged replicas in case of using the WAIT. If 0 is not use the WAIT. */
    private int acknowledgedReplicas = 0;
    /**
     * The timeout of the WAIT in case of using the WAIT(default isn't timeout).
     * When {@link #acknowledgedReplicas} is greater than or equal to 1, WAIT will be used.
     */
    private int waitTimeout = 0;
}
