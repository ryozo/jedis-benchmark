package net.equj65.jedisbench;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.equj65.jedisbench.enums.Command;
import net.equj65.jedisbench.runnner.OperationRunner;
import net.equj65.jedisbench.runnner.OperationRunnerFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.PrintStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * @author ryozo
 */
public class Benchmarker {

    private String hostname;
    private int port;
    private int threads;
    private int dataSize;
    private int requests;

    public Benchmarker(String hostname, int port, int threads, int dataSize, int requests) {
        this.hostname = hostname;
        this.port = port;
        this.threads = threads;
        this.dataSize = dataSize;
        this.requests = requests;
    }

    public void benchmark() {
        Arrays.stream(Command.values()).forEach(c -> benchmark_(c).printBenchmark());
    }

    private Benchmark benchmark_(Command command) {
        // TODO review of poolconfig
        JedisPool pool = new JedisPool(new JedisPoolConfig(), hostname, port);
        CountDownLatch latch = new CountDownLatch(requests);
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        String data = fillString(dataSize);

        // TODO refactor
        OperationRunner runner = OperationRunnerFactory
                .createRunnerOf(command, pool, latch, data);

        long startOfMillis = System.currentTimeMillis();
        IntStream.range(0, threads).forEach(
                i -> executor.submit(runner)
        );

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        long endOfMillis = System.currentTimeMillis();

        executor.shutdownNow();

        return new Benchmark(endOfMillis - startOfMillis);
    }

    /**
     * This method create a string of the specified size.
     * @param lengthOfFill generate string size
     * @return
     */
    private static String fillString(int lengthOfFill) {
        return new StringWriter() {{
            IntStream.range(0, lengthOfFill).forEach(i -> write("a"));
        }}.toString();
    }

    /**
     * To hold the benchmark results.
     *
     * TODO Give more finely
     */
    @AllArgsConstructor
    public static class Benchmark {
        @Getter
        private long elapsetTime;

        public void printBenchmark() {
            printBenchmark(System.out);
        }

        public void printBenchmark(PrintStream stream) {
            stream.println(String.format("Elapsed time is [%s].", elapsetTime));
        }
    }
}
