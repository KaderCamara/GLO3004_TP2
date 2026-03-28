package src;

import java.util.Random;

/**
 * Publisher thread that implements the PUB3 process from FSP specification.
 *
 * FSP Specification:
 * PUB3 = (supply -> connect_pub -> pub -> PUB3).
 *
 * Cycle: SUPPLY -> CONNECT_PUB -> PUB -> (repeat)
 *
 * The publisher generates messages (SUPPLY), connects to the broker,
 * and publishes the message.
 */
public class Publisher extends Thread {
    private final Broker broker;
    private final String appType;
    private final int id;
    private final long endTime;
    private final Random random;
    private volatile boolean running = true;

    /**
     * Creates a new Publisher.
     *
     * @param appType  Application type (i or t)
     * @param id       Publisher identifier
     * @param broker   The broker to publish to
     * @param endTime  Time when execution should stop
     */
    public Publisher(String appType, int id, Broker broker, long endTime) {
        this.broker = broker;
        this.appType = appType;
        this.id = id;
        this.endTime = endTime;
        this.random = new Random();
        this.setName(appType + ".publisher." + id);
    }

    /**
     * Stops the publisher gracefully.
     */
    public void stopPublisher() {
        running = false;
        this.interrupt();
    }

    @Override
    public void run() {
        try {
            while (running && System.currentTimeMillis() < endTime) {
                // Action SUPPLY: Generate a message
                Thread.sleep(random.nextInt(50) + 10);
                if (!running || System.currentTimeMillis() >= endTime) break;
                TraceManager.trace(appType, "publisher", id, "SUPPLY");

                // Action CONNECT_PUB: Connect to broker
                if (!running || System.currentTimeMillis() >= endTime) break;
                TraceManager.trace(appType, "publisher", id, "CONNECT_PUB");
                broker.connectPublisher();

                // Action PUB: Publish the message
                if (!running || System.currentTimeMillis() >= endTime) break;
                TraceManager.trace(appType, "publisher", id, "PUB");
                broker.publish();

                // Small pause before next cycle
                Thread.sleep(random.nextInt(30) + 5);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
