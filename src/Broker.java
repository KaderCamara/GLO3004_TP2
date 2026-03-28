package src;

import java.util.concurrent.Semaphore;

/**
 * Broker managing the message queue between publishers and subscribers.
 * Implements the BROKER4 process from FSP specification.
 *
 * FSP Specification:
 * BROKER4 = PUBSUB[0],
 *   PUBSUB[i:0..N] = (when (i < N) connect_pub -> pub -> queue -> PUBSUB[i + 1]
 *                   | when (i > 0) connect_sub -> sub -> dequeue -> PUBSUB[i - 1]).
 *
 * The broker uses two semaphores to control synchronization:
 * - slots: Controls the number of available slots for publishers (initialized to N)
 * - messages: Controls the number of available messages for subscribers (initialized to 0)
 *
 * Invariant: 0 <= currentMessages <= N
 */
public class Broker {
    private final int capacity;
    private final Semaphore slots;      // Available slots for publishers (max N)
    private final Semaphore messages;   // Available messages for subscribers

    /**
     * Creates a broker with specified capacity.
     * @param capacity Maximum number of concurrent messages (N in FSP)
     */
    public Broker(int capacity) {
        this.capacity = capacity;
        this.slots = new Semaphore(capacity, true);  // Fair semaphore
        this.messages = new Semaphore(0, true);      // Fair semaphore
    }

    /**
     * Publisher connects to the broker (action connect_pub).
     * Blocks if the broker is full (i >= N).
     * @throws InterruptedException if thread is interrupted
     */
    public void connectPublisher() throws InterruptedException {
        slots.acquire();  // Wait for available slot (i < N)
    }

    /**
     * Publisher publishes a message (action pub).
     * Increments the message count and notifies waiting subscribers.
     */
    public void publish() {
        messages.release();  // Signal message available (i + 1)
    }

    /**
     * Subscriber connects to the broker (action connect_sub).
     * Blocks if no messages are available (i == 0).
     * @throws InterruptedException if thread is interrupted
     */
    public void connectSubscriber() throws InterruptedException {
        messages.acquire();  // Wait for available message (i > 0)
    }

    /**
     * Subscriber receives a message (action sub).
     * Decrements the message count and frees a slot for publishers.
     */
    public void subscribe() {
        slots.release();  // Free slot for publishers (i - 1)
    }

    /**
     * Returns the broker capacity.
     * @return maximum capacity
     */
    public int getCapacity() {
        return capacity;
    }
}
