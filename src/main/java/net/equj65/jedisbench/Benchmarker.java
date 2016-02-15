package net.equj65.jedisbench;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.equj65.jedisbench.context.BenchmarkContext;
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

    private BenchmarkContext context;

    public Benchmarker(BenchmarkContext context) {
        this.context = context;
    }

    public void benchmark() {
        Arrays.stream(Command.values()).forEach(c -> benchmark_(c).printBenchmark());
    }

    private Benchmark benchmark_(Command command) {
        // TODO review of poolconfig
        JedisPool pool = new JedisPool(new JedisPoolConfig(), context.getHostname(), context.getPort());
        ExecutorService executor = Executors.newFixedThreadPool(context.getThreads());
        CountDownLatch latch = new CountDownLatch(context.getRequests());

        // TODO refactor
        OperationRunner runner = OperationRunnerFactory
                .createRunnerOf(command, context, pool, latch);
        try {
            long startOfMillis = System.currentTimeMillis();

            IntStream.range(0, context.getThreads()).forEach(
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
