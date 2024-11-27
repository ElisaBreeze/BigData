package org.ulpgc;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;

public class ChartCreator {
    public static void main(String[] args) {
        try {
            // Open CSV file
            CSVReader reader = new CSVReader(new FileReader("benchmarkResults.csv"));
            String[] line;

            // Skip the header
            reader.readNext();

            // Create datasets for Execution Time and Memory Usage
            DefaultCategoryDataset timeDataset = new DefaultCategoryDataset();
            DefaultCategoryDataset memoryDataset = new DefaultCategoryDataset();

            // Read through the CSV file
            while ((line = reader.readNext()) != null) {
                dataExtraction(line, timeDataset, memoryDataset);
            }

            // Create the charts
            Result result = createCharts(timeDataset, memoryDataset);

            // Set background color to white
            result.timeChart().setBackgroundPaint(Color.WHITE);
            result.memoryChart().setBackgroundPaint(Color.WHITE);

            // Create Chart Panels
            ChartPanel timePanel = new ChartPanel(result.timeChart());
            ChartPanel memoryPanel = new ChartPanel(result.memoryChart());

            // Display charts in a JFrame
            JFrame frame = new JFrame("Matrix Multiplication Benchmark Results");
            frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.add(timePanel);
            frame.add(memoryPanel);

            frame.pack();
            frame.setVisible(true);

            reader.close();  // Close the CSV reader

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    private static Result createCharts(DefaultCategoryDataset timeDataset, DefaultCategoryDataset memoryDataset) {
        JFreeChart timeChart = ChartFactory.createLineChart(
                "Execution Time vs Matrix Size",  // Chart title
                "Matrix Size",                   // X-axis
                "Execution Time (ms)",           // Y-axis
                timeDataset                       // Dataset - execution time
        );

        // Create the Memory Usage chart
        JFreeChart memoryChart = ChartFactory.createLineChart(
                "Memory Usage vs Matrix Size",   // Chart title
                "Matrix Size",                   // X-axis
                "Memory Usage (MB)",             // Y-axis
                memoryDataset                    // Dataset - memory usage
        );
        Result result = new Result(timeChart, memoryChart);
        return result;
    }

    private record Result(JFreeChart timeChart, JFreeChart memoryChart) {
    }

    private static void dataExtraction(String[] line, DefaultCategoryDataset timeDataset, DefaultCategoryDataset memoryDataset) {
        try {
            String matrixSize = line[0];
            String method = line[1];
            double executionTime = Double.parseDouble(line[2]);
            double memoryUsage = Double.parseDouble(line[3]);

            // Add data to the Execution Time dataset
            timeDataset.addValue(executionTime, method, matrixSize);
            // Add data to the Memory Usage dataset
            memoryDataset.addValue(memoryUsage, method, matrixSize);

        } catch (NumberFormatException e) {
            System.out.println("Invalid data in row: " + String.join(",", line));
        }
    }
}
