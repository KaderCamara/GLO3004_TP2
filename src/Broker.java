package src;

import java.util.concurrent.Semaphore;

/**
 * Processus fourni par l'énoncé.
 *
 * BROKER4 = PUBSUB[0],
 *   PUBSUB[i:0..N] = (when (i < N) connect_pub -> pub -> queue -> PUBSUB[i + 1]
 *                   | when (i > 0) connect_sub -> sub -> dequeue -> PUBSUB[i - 1]).
 */

public class Broker {
    //nb_publications
    private final Semaphore nb_publications;
    //nb_subscriptions
    private final Semaphore nb_subscriptions;

    public Broker(int n) {
        this.nb_publications = new Semaphore(n);
        this.nb_subscriptions  = new Semaphore(0);
    }

    //fonction du sémaphore pour acquérir la ressource et notifier les autres threads
    //de l'utilisation de ce dernier
    //Dans notre énoncé et le cas de publisher, c'est lorsqu'on se connecte et publie.
    public void connectAndPublish() throws InterruptedException {
        nb_publications.acquire();
        nb_subscriptions.release();
    }

    //fonction du sémaphore pour acquérir la ressource et notifier les autres threads
    //de l'utilisation de ce dernier
    //Dans notre énoncé et le cas de subscriber, c'est lorsqu'on se connecte et suscribe.
    //on fait juste l contraire de publish
    public void connectAndSubscribe() throws InterruptedException {
        nb_subscriptions.acquire();
        nb_publications.release();
    }
}
