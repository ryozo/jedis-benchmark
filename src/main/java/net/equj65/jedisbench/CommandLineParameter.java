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
}
