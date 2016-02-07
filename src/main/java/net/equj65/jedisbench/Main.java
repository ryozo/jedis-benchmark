package net.equj65.jedisbench;

import com.beust.jcommander.JCommander;

/**
 * @author ryozo
 */
public class Main {

    public static void main(String[] args) {
        CommandLineParameter param = new CommandLineParameter();
        new JCommander(param, args);

        Benchmarker benchmarker = new Benchmarker(param.getHostname(),
                param.getPort(), param.getThreads(), param.getDataSize(), param.getRequests());

        Benchmarker.Benchmark benchmark = benchmarker.benchmark();
        benchmark.printBenchmark();
    }

}
