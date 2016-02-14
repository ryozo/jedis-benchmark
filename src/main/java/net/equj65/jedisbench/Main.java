package net.equj65.jedisbench;

import com.beust.jcommander.JCommander;

/**
 * @author ryozo
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("jedis-benchmark start.");
        System.out.println("------------------------------------");
        System.out.println();

        CommandLineParameter param = new CommandLineParameter();
        new JCommander(param, args);

        System.out.println("[ Benchmark settings. ]");
        System.out.println(String.format("  Server hostname : %s", param.getHostname()));
        System.out.println(String.format("  Server port : %s", param.getPort()));
        System.out.println(String.format("  Number of parallel connections : %s", param.getThreads()));
        System.out.println(String.format("  Data size of SET/GET value in bytes : %s", param.getDataSize()));
        System.out.println(String.format("  Total number of requests : %s", param.getRequests()));
        System.out.println(String.format("  Variation of key range : %s", param.getKeySpaceLen()));
        System.out.println();

        Benchmarker benchmarker = new Benchmarker(param.getHostname(),
                param.getPort(), param.getThreads(), param.getDataSize(), param.getRequests());
        benchmarker.benchmark();
    }

}
