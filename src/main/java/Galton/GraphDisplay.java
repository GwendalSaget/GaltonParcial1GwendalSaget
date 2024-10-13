package Galton;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class GraphDisplay extends JFrame {

    public GraphDisplay() {
        // Configurer la fenêtre
        setTitle("Fréquence des résultats");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Créer le tableau de données
        CategoryDataset dataset = createDataset();

        // Créer le graphique
        JFreeChart chart = ChartFactory.createBarChart(
                "Fréquence des résultats", // Titre
                "Résultats",              // Label de l'axe X
                "Fréquence",              // Label de l'axe Y
                dataset
        );

        // Ajouter le graphique au panneau
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(chartPanel);
    }

    private CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            // Connexion à la base de données
            String url = "jdbc:sqlite:src/galton_results.db"; // Chemin vers votre base de données
            Connection conn = DriverManager.getConnection(url);

            // Exécution de la requête pour récupérer les résultats
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT result_value FROM results");

            // Initialiser le tableau de fréquence
            int[] frequency = new int[12]; // De 0 à 11 (index 0 sera ignoré)

            // Compter les occurrences
            while (rs.next()) {
                int result = rs.getInt("result_value");
                if (result >= 1 && result <= 11) {
                    frequency[result]++;
                }
            }

            // Fermer la connexion
            rs.close();
            stmt.close();
            conn.close();

            // Ajouter les données au dataset
            for (int i = 1; i <= 11; i++) {
                dataset.addValue(frequency[i], "Fréquence", String.valueOf(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataset;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GraphDisplay example = new GraphDisplay();
            example.setVisible(true);
        });
    }
}
