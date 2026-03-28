package src;

import java.util.ArrayList;
import java.util.List;

/**
 * Paramètres:
 *   -Dn=<int>: Nb messages simultanés (N)
 *   -Dp=<int>: Nb publishers (NB_P)
 *   -Ds=<int>: Nb subscribers (NB_S)
 *   -Dt=<int>: durée d'exécution en millisecondes
 * Exemple donné:
 *   java -Dn=2 -Dp=2 -Ds=3 -Dt=100 -jar tp2.jar
 */
public class Main {
    //on a 2 types d'applications ici
    private static final String[] APPLICATIONS = {"i", "t"};

    public static void main(String[] args) throws InterruptedException {
        //Initialisation de nos listes pour la suite.
        List<Publisher>  publishers  = new ArrayList<>();
        List<Subscriber> subscribers = new ArrayList<>();
        List<Thread>     threads     = new ArrayList<>();
        //parametres avec valeurs par défaut
        int n  = Integer.parseInt(System.getProperty("n", "2"));
        int nbP = Integer.parseInt(System.getProperty("p", "2"));
        int nbS = Integer.parseInt(System.getProperty("s", "3"));
        int t  = Integer.parseInt(System.getProperty("t", "1000"));

        //AFFICHAGE TRACE
        System.out.println("N = " + n + " NB_P = " + nbP + " NB_S = " + nbS + " t = " + t + " ms");
        System.out.println("Trace : ");
        System.out.println(" ");

        //Pour pas avoir de chauvechement chaque application a son broker
        // on crée ensuite nous publishers et subscribers qu'on sauvegarde.
        //on leur attribue chacun son thread ( le but de l'exercice qui est l'éxécution simultanée
        //de plusieurs threads ) comme quand chacun se connecte sur son facebook ( indépendant )
        for (String application : APPLICATIONS) {
            Broker broker = new Broker(n);
            for (int id = 1; id <= nbP; id++) {
                Publisher pub = new Publisher(application, id, broker);
                publishers.add(pub);
                Thread thread = new Thread(pub, application + ".publisher." + id);
                threads.add(thread);
            }
            for (int id = 1; id <= nbS; id++) {
                Subscriber sub = new Subscriber(application, id, broker);
                subscribers.add(sub);
                Thread thread = new Thread(sub, application + ".subscriber." + id);
                threads.add(thread);
            }
        }

        //on lance l'éxécution du systeme pubsub
        for (Thread thread : threads) {
            thread.start();
        }
        // on met le timer qui va arrêter tout après
        Thread.sleep(t);

        //Apres exécution on s'assure de tout fermer correctement
        //on arrete manuellement avec notre fonction nos publishers et suscribers
        for (Publisher pub : publishers)   pub.stop();
        for (Subscriber sub : subscribers) sub.stop();
        // on arrete nos threads
        for (Thread thread : threads) {
            thread.interrupt();
        }

        // On joint tous les threads pour attendre l'éxécution de tous ceux qui avaient été démarrés
        //TRES IMPORTANT COMME ON A PU L'APPRENDRE DANS LE COURS QUI EN PARLAIT AVEC LEXEMPLE DU PONT ET DES VOITURES
        for (Thread thread : threads) {
            //on a mis 2 fois le temps d'éxécution
            thread.join(2L *t);
        }
        System.out.println("Simulation terminée après " + t + "ms.");
    }
}
