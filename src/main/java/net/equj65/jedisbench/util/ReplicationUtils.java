package net.equj65.jedisbench.util;

import net.equj65.jedisbench.enums.Command;

/**
 * This class provides RedisReplication related functions.
 *
 * @author ryozo
 */
public class ReplicationUtils {

    /**
     * Confirm whether the WaitReplicas is available RedisCommand.
     *
     * @param command target
     * @return result
     */
    public static boolean isWaitableCommand(Command command) {
        return Command.SET == command;
    }

}
