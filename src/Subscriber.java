package src;

import java.util.Random;

/**
 * Subscriber thread that implements the SUB3 process from FSP specification.
 *
 * FSP Specification:
 * SUB3 = (connect_sub -> sub -> consume -> SUB3).
 *
 * Cycle: CONNECT_SUB -> SUB -> CONSUME -> (repeat)
 *
 * The subscriber connects to the broker, receives a message,
 * and consumes the message.
 */
public class Subscriber extends Thread {
    private final Broker broker;
    private final String appType;
    private final int id;
    private final long endTime;
    private final Random random;
    private volatile boolean running = true;

    /**
     * Creates a new Subscriber.
     *
     * @param appType  Application type (i or t)
     * @param id       Subscriber identifier
     * @param broker   The broker to subscribe to
     * @param endTime  Time when execution should stop
     */
    public Subscriber(String appType, int id, Broker broker, long endTime) {
        this.broker = broker;
        this.appType = appType;
        this.id = id;
        this.endTime = endTime;
        this.random = new Random();
        this.setName(appType + ".subscriber." + id);
    }

    /**
     * Stops the subscriber gracefully.
     */
    public void stopSubscriber() {
        running = false;
        this.interrupt();
    }

    @Override
    public void run() {
        try {
            while (running && System.currentTimeMillis() < endTime) {
                // Action CONNECT_SUB: Connect to broker
                if (!running || System.currentTimeMillis() >= endTime) break;
                TraceManager.trace(appType, "subscriber", id, "CONNECT_SUB");
                broker.connectSubscriber();

                // Action SUB: Receive the message
                if (!running || System.currentTimeMillis() >= endTime) break;
                TraceManager.trace(appType, "subscriber", id, "SUB");
                broker.subscribe();

                // Action CONSUME: Consume the message
                Thread.sleep(random.nextInt(50) + 20);
                if (!running || System.currentTimeMillis() >= endTime) break;
                TraceManager.trace(appType, "subscriber", id, "CONSUME");

                // Small pause before next cycle
                Thread.sleep(random.nextInt(30) + 5);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
