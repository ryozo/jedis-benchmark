package net.equj65.jedisbench.runnner;

import net.equj65.jedisbench.enums.Command;
import net.equj65.jedisbench.runnner.impl.GetOperationRunner;
import net.equj65.jedisbench.runnner.impl.SetOperationRunner;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.CountDownLatch;

/**
 * Created by ryozo on 2016/02/08.
 */
public class OperationRunnerFactory {

    // TODO refactor
    public static OperationRunner createRunnerOf(Command command, JedisPool pool, CountDownLatch latch, String value) {
        switch (command) {
            case SET:
                return new SetOperationRunner(pool, latch, value);
            case GET:
                return new GetOperationRunner(pool, latch, value);
        }
        throw new IllegalArgumentException(String.format("Command [%s] is not supported.", command));
    }

}
