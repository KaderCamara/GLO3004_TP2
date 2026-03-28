package src;

/**
 * Processus PUB de l'énoncé
 * PUB3 = (supply -> connect_pub -> pub -> close_pub -> PUB3).
 */
public class Publisher implements Runnable {
    private final String name;
    private final Broker broker;
    private boolean running = true;

    public Publisher(String application, int id, Broker broker) {
        this.name = application + ".publisher." + id;
        this.broker = broker;
    }

    //arret du thread en cours
    public void stop() {
        running = false;
    }

    //ici c'est simple, on met le nom devant comme dans la trace d'exemple on supply d'abord
    //puis on lock le thread avec le sémaphore donc il effectue son action
    //empechant les autres d'avoir accés à la ressource en meme temps
    //on ferme ensuite le semaphore pour qu'il libere la ressource ( on le print juste ici, voir main pour fermeture)
    //l'exception nous permet de bloquer l'éxécution automatique apres un certain temps
    @Override
    public void run() {
        try {
            while (running) {
                System.out.println(name + " SUPPLY");
                broker.connectAndPublish();
                synchronized (System.out) {
                    System.out.println(name + " CONNECT_PUB");
                    System.out.println(name + " PUB");
                    System.out.println(name + " CLOSE_PUB");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
