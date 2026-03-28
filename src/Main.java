package src;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class for the Publisher-Subscriber system.
 * Manages the startup and shutdown of the system based on provided parameters.
 *
 * System Parameters:
 *   -Dn=<int>: Number of concurrent messages in broker (N in FSP)
 *   -Dp=<int>: Number of publishers per application type (NB_P in FSP)
 *   -Ds=<int>: Number of subscribers per application type (NB_S in FSP)
 *   -Dt=<int>: Execution duration in milliseconds
 *
 * Example:
 *   java -Dn=2 -Dp=2 -Ds=3 -Dt=100 -jar tp2.jar
 *
 * FSP System:
 * - Two application types: i and t
 * - Total publishers: 2 × NB_P (NB_P of type i + NB_P of type t)
 * - Total subscribers: 2 × NB_S (NB_S of type i + NB_S of type t)
 * - All actors share a single global broker
 */
public class Main {
    private static final String[] APPLICATION_TYPES = {"i", "t"};

    public static void main(String[] args) {
        // Parse system properties with default values
        int n = Integer.parseInt(System.getProperty("n", "2"));
        int p = Integer.parseInt(System.getProperty("p", "2"));
        int s = Integer.parseInt(System.getProperty("s", "3"));
        int t = Integer.parseInt(System.getProperty("t", "100"));

        // Validate parameters
        if (n < 1 || p < 1 || s < 1 || t < 1) {
            System.err.println("Error: All parameters must be positive");
            System.err.println("Usage: java -Dn=<capacity> -Dp=<publishers> -Ds=<subscribers> -Dt=<duration> -jar tp2.jar");
            System.exit(1);
        }

        // Create single global broker (shared by all applications)
        // This is the key improvement: one broker for the entire system
        Broker broker = new Broker(n);

        // Calculate end time
        long endTime = System.currentTimeMillis() + t;

        // Lists to store publishers and subscribers
        List<Publisher> publishers = new ArrayList<>();
        List<Subscriber> subscribers = new ArrayList<>();

        // Create publishers and subscribers for both application types
        // Each type has p publishers and s subscribers
        for (String appType : APPLICATION_TYPES) {
            // Create publishers for this application type
            for (int id = 1; id <= p; id++) {
                Publisher publisher = new Publisher(appType, id, broker, endTime);
                publishers.add(publisher);
            }
            // Create subscribers for this application type
            for (int id = 1; id <= s; id++) {
                Subscriber subscriber = new Subscriber(appType, id, broker, endTime);
                subscribers.add(subscriber);
            }
        }

        // Start all publishers
        for (Publisher publisher : publishers) {
            publisher.start();
        }

        // Start all subscribers
        for (Subscriber subscriber : subscribers) {
            subscriber.start();
        }

        // Wait for the execution duration
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Stop all publishers
        for (Publisher publisher : publishers) {
            publisher.stopPublisher();
        }

        // Stop all subscribers
        for (Subscriber subscriber : subscribers) {
            subscriber.stopSubscriber();
        }

        // Wait for all threads to finish
        try {
            for (Publisher publisher : publishers) {
                publisher.join(1000);
            }
            for (Subscriber subscriber : subscribers) {
                subscriber.join(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Exit cleanly
        System.exit(0);
    }
}
