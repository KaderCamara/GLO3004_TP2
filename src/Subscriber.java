package src;

/**
 * processus SUB de l'énoncé.
 * SUB3 = (connect_sub -> sub -> close_sub -> consume -> SUB3).
 */
public class Subscriber implements Runnable {
    private final String name;
    private final Broker broker;
    private boolean running = true;

    public Subscriber(String app, int id, Broker broker) {
        this.name = app + ".subscriber." + id;
        this.broker = broker;
    }

    //arret du thread en cours
    public void stop() {
        running = false;
    }

    //ici c'est simple, on met le nom devant comme dans la trace d'exemple mais on ne supply
    //pas ici vu que c'Est le subscriber donc on fait la suite direct ( l'action )
    //puis on lock le thread avec le sémaphore donc il effectue son action
    //empechant les autres d'avoir accés à la ressource en meme temps
    //on ferme ensuite le semaphore pour qu'il libere la ressource ( on le print juste ici, voir main pour fermeture)
    //l'exception nous permet de bloquer l'éxécution automatique apres un certain temps
    @Override
    public void run() {
        try {
            while (running) {
                broker.connectAndSubscribe();
                synchronized (System.out) {
                    System.out.println(name + " CONNECT_SUB");
                    System.out.println(name + " SUB");
                    System.out.println(name + " CLOSE_SUB");
                    System.out.println(name + " CONSUME");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
