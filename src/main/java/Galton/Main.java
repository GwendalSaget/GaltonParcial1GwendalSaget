package Galton;

import javax.swing.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static TreeFunc TreeFunc = new TreeFunc();
    public static ResultRepository resultRepository = new ResultRepository();
    private static final Lock lock = new ReentrantLock(); // Crear un mutex

    public static void main(String[] args) {
        // reset database para empezar
        DatabaseConnection.clearDatabase();

        int numberOfThreads = 300; // Utilisez un nombre de threads raisonnable
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            Thread thread = new Thread(() -> {
                try {
                    GaltonTree Arbol = TreeFunc.onTreeCreate();
                    int result = TreeFunc.ParcoursResult(Arbol);

                    // Usar mutex par sincronisar acceso a saveresult
                    lock.lock();
                    try {
                        resultRepository.saveResult(result);
                    } finally {
                        lock.unlock();
                    }
                }
                 finally {
                    latch.countDown(); // bajar el contador
                }
            });
            thread.start();
        }

        //esperar el fin de ejecucion de los threads
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Afficher le graphique avec les résultats
        SwingUtilities.invokeLater(() -> {
            GraphDisplay graphDisplay = new GraphDisplay();
            graphDisplay.setVisible(true);
        });
    }
}
