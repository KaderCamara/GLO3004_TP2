package src;

/**
 * Manages synchronized trace output for the pub-sub system.
 * Ensures that trace lines are not interleaved when multiple threads
 * are printing simultaneously.
 *
 * Format: appType.actorType.id ACTION
 * Example: i.publisher.2 SUPPLY
 */
public class TraceManager {
    private static final Object lock = new Object();

    /**
     * Prints a trace message in the required format.
     * Thread-safe: ensures atomic printing of the trace line.
     *
     * @param appType    Application type (i or t)
     * @param actorType  Actor type (publisher or subscriber)
     * @param id         Actor identifier (1, 2, 3, ...)
     * @param action     Action name (SUPPLY, CONNECT_PUB, PUB, etc.)
     */
    public static void trace(String appType, String actorType, int id, String action) {
        synchronized (lock) {
            System.out.println(appType + "." + actorType + "." + id + " " + action);
        }
    }
}
