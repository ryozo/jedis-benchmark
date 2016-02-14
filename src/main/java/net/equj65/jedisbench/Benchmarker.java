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
    private int keySpaceLen;

    public Benchmarker(String hostname, int port, int threads, int dataSize, int requests, int keySpaceLen) {
        this.hostname = hostname;
        this.port = port;
        this.threads = threads;
        this.dataSize = dataSize;
        this.requests = requests;
        this.keySpaceLen = keySpaceLen;
    }

    public void benchmark() {
        Arrays.stream(Command.values()).forEach(c -> benchmark_(c).printBenchmark());
    }

    private Benchmark benchmark_(Command command) {
        // TODO review of poolconfig
        JedisPool pool = new JedisPool(new JedisPoolConfig(), hostname, port);
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(requests);
        String data = fillString(dataSize);

        // TODO refactor
        OperationRunner runner = OperationRunnerFactory
                .createRunnerOf(command, pool, latch, data);
        try {
            long startOfMillis = System.currentTimeMillis();

            IntStream.range(0, threads).forEach(
                    i -> executor.submit(runner)
            );
            latch.await();

            long elapsedMillis = System.currentTimeMillis() - startOfMillis;
            return new Benchmark(command, elapsedMillis);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);

        } finally {
            executor.shutdownNow();
            pool.destroy();
        }
    }

    /**
     * This method create a string of the specified size.
     *
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
     * <p>
     * TODO Give more finely
     */
    @AllArgsConstructor
    public static class Benchmark {
        @Getter private Command command;
        @Getter private long elapsetTime;

        public void printBenchmark() {
            printBenchmark(System.out);
        }

        public void printBenchmark(PrintStream stream) {
            stream.println(String.format("[ Benchmark result of %s command ]", command));
            stream.println(String.format("  Elapsed time is %s milliseconds.", elapsetTime));
            stream.println();
        }
    }
}
