package net.equj65.jedisbench;

import com.beust.jcommander.JCommander;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ryozo
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) {
        logger.info("jedis-benchmark start.");

        CommandLineParameter param = new CommandLineParameter();
        new JCommander(param, args);

        logger.info("[ Benchmark settings ]");
        logger.info("  Server hostname : {}", param.getHostname());
        logger.info("  Server port : {}", param.getPort());
        logger.info("  Number of parallel connections : {}", param.getThreads());
        logger.info("  Data size of SET/GET value in bytes : {}", param.getDataSize());
        logger.info("  Total number of requests : {}", param.getRequests());

        Benchmarker benchmarker = new Benchmarker(param.getHostname(),
                param.getPort(), param.getThreads(), param.getDataSize(), param.getRequests());
        benchmarker.benchmark();
    }

}
