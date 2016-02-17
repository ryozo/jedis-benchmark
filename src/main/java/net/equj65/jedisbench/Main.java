package net.equj65.jedisbench;

import com.beust.jcommander.JCommander;
import net.equj65.jedisbench.context.BenchmarkContext;
import org.apache.commons.beanutils.BeanUtils;

/**
 * @author ryozo
 */
public class Main {

    public static void main(String[] args) throws Exception {
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
        // TODO Condition to display only if WAIT is ON.
        System.out.println(String.format("  The number of acknowledged replicas : %s", param.getAcknowledgedReplicas()));
        System.out.println(String.format("  The timeout of the WAIT : %s", param.getWaitTimeout()));
        System.out.println();

        BenchmarkContext context = buildContext(param);

        // Perform benchmark.
        new Benchmarker(context).benchmark();
    }

    private static BenchmarkContext buildContext(CommandLineParameter parameter) throws Exception {
        BenchmarkContext context = new BenchmarkContext();
        BeanUtils.copyProperties(context, parameter);
        return context;
    }

}
