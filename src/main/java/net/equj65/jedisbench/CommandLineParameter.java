package net.equj65.jedisbench;

import com.beust.jcommander.Parameter;
import lombok.Data;

/**
 * This class represent of Java commandline arguments.
 *
 * @author ryozo
 */
@Data
public class CommandLineParameter {

    /** Benchmark target hostname */
    @Parameter(names = "-h", description = "Server hostname (default 127.0.0.1)")
    private String hostname = "127.0.0.1";

    /** Server port */
    @Parameter(names = "-p", description = "Server port (default 6379)")
    private int port = 6379;

    /** Number of parallel connections */
    @Parameter(names = "-c", description = "Number of parallel connections (default 50)")
    private int threads = 50;

    /** Data size of SET/GET value in bytes */
    @Parameter(names = "-d", description = "Data size of SET/GET value in bytes (default 2)")
    private int dataSize = 2;

    /** Total number of request */
    @Parameter(names = "-n", description = "Total number of requests (default 100000)")
    private int requests = 100000;

    /** Use random key. Specify the variation of key */
    @Parameter(names = "-r", description = "Use random key. Specify the variation of key(default not use randomkey)")
    private int keySpaceLen = 1;

    /** The number of acknowledged replicas in case of using the WAIT. If 0 is not use the WAIT. */
    @Parameter(names = "--wait-replicas", description = "The number of acknowledged replicas in case of using the WAIT.")
    private int acknowledgedReplicas = 0;

    /**
     * The timeout of the WAIT in case of using the WAIT(default isn't timeout).
     * When {@link #acknowledgedReplicas} is greater than or equal to 1, WAIT will be used.
     */
    @Parameter(names = "--wait-timeout", description = "The timeout of the WAIT(default isn't timeout).")
    private int waitTimeout = 0;
}
