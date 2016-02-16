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
}
