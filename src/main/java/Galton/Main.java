package Galton;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Float.NaN;

public class Main {
    public static TreeFunc TreeFunc = new TreeFunc();
    public static ResultRepository resultRepository = new ResultRepository();
    private static final Lock lock = new ReentrantLock(); // Crear un mutex

    public static void main(String[] args) {
        // reset database para empezar
        DatabaseConnection.clearDatabase();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Cuantos threads quieres ?");
        int numberOfThreads = scanner.nextInt(); // Utilisez un nombre de threads raisonnable
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

        Calculproba(numberOfThreads);
    }


public static int CalculNorm(int threads, int contenedor){
    int proba1 = (int) (threads*0.005);
    int proba2 = (int) (threads*0.01);
    int proba3 = (int) (threads*0.04);
    int proba4 = (int) (threads*0.12);
    int proba5 = (int) (threads*0.2);
    int proba6 = (int) (threads*0.25);
    if (contenedor == 1 || contenedor == 11){
        return proba1;
    }
    else if (contenedor == 2 || contenedor == 10){
        return proba2;
    }
    else if (contenedor == 3 || contenedor == 9){
        return proba3;
    }
    else if (contenedor == 4 || contenedor == 8){
        return proba4;
    }
    else if (contenedor == 5 || contenedor == 7){
        return proba5;
    }
    else if (contenedor == 6){
        return proba6;
    }
    return 0;
}

public static void Calculproba(int thread) {
    try {
        // Connexion à la base de données
        String url = "jdbc:sqlite:src/galton_results.db"; // Camino de la database
        Connection conn = DriverManager.getConnection(url);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT result_value FROM results");

        int[] frequency = new int[12];
        while (rs.next()) {
            int result = rs.getInt("result_value");
            if (result >= 1 && result <= 11) {
                frequency[result]++;
            }
        }
        rs.close();
        stmt.close();
        conn.close();
        float[] freqobt = new float[11];
        for (int i = 0; i < 11; i++) {
            int freqnorm = CalculNorm(thread, i);
            float freq1 = (float) frequency[i] / (float) freqnorm;
            float freq2 = (float) freqnorm / (float) frequency[i];
            if (freq1 > freq2){
                freqobt[i] = freq2;
            }
            else {
                freqobt[i] = freq1;
            }
            System.out.println("\n\nFrecuencia normal de " + i+1 + " : " + freqnorm);
            System.out.println("\nFrecuencia obtenida de " + i+1 + " : " + frequency[i]+" es decir "+freqobt[i]+"% de una distribucion normal");
        }
        float fin = 0;
        for (int i = 0; i < 11; i++) {
            if (freqobt[i] != NaN) {
                fin += freqobt[i];
            }
        }
        fin = fin/11;
        System.out.println("Con "+thread+" se puede tener " +fin+"% de una distribucion normal !");

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
