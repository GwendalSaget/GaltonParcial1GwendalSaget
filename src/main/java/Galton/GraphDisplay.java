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

        setTitle("Frecuencia de resultados");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear dataset
        CategoryDataset dataset = createDataset();

        // Crear graph
        JFreeChart chart = ChartFactory.createBarChart(
                "Frecuencia de resultados", // Titre
                "Resultados",              // Label de l'axe X
                "Frequencia",              // Label de l'axe Y
                dataset
        );
        //anadir graph a la ventana
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(chartPanel);
    }

    private CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            // Connexion à la base de données
            String url = "jdbc:sqlite:src/galton_results.db"; // Camino de la database
            Connection conn = DriverManager.getConnection(url);

            // Ejecucion de la requuesta para obtener los resultados
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT result_value FROM results");

            // Inicializar un array pour stocker les fréquences
            int[] frequency = new int[12]; // De 0 hasta 11

            // Contar las frecuencias
            while (rs.next()) {
                int result = rs.getInt("result_value");
                if (result >= 1 && result <= 11) {
                    frequency[result]++;
                }
            }

            // cerrar
            rs.close();
            stmt.close();
            conn.close();

            // Anadir datos al dataset
            for (int i = 1; i <= 11; i++) {
                dataset.addValue(frequency[i], "Frecuencia", String.valueOf(i));
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
