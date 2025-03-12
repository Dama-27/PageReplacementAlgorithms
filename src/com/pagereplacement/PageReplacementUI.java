package com.pagereplacement;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PageReplacementUI extends JFrame {
    private JTextField referenceStringField;
    private JTextField numFramesField;
    private JTabbedPane tabbedPane; // To display results for each algorithm in separate tabs
    private JLabel bestAlgorithmLabel; // To display the best algorithm

    public PageReplacementUI() {
        setTitle("Page Replacement Algorithms");
        setSize(900, 700); // Increased window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set a colorful theme
        Color backgroundColor = new Color(245, 245, 245); // Light gray background
        Color buttonColor = new Color(0, 120, 215); // Blue button
        Color textColor = new Color(50, 50, 50); // Dark gray text
        Color highlightColor = new Color(255, 223, 186); // Light orange highlight

        // Apply background color to the frame
        getContentPane().setBackground(backgroundColor);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // Added spacing
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Added padding
        inputPanel.setBackground(backgroundColor); // Light gray background

        JLabel refStringLabel = new JLabel("Reference String (comma-separated):");
        refStringLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Increased font size
        refStringLabel.setForeground(textColor); // Dark gray text
        inputPanel.add(refStringLabel);

        referenceStringField = new JTextField();
        referenceStringField.setFont(new Font("Arial", Font.PLAIN, 16)); // Increased font size
        referenceStringField.setBackground(Color.WHITE); // White background
        referenceStringField.setForeground(textColor); // Dark gray text
        inputPanel.add(referenceStringField);

        JLabel framesLabel = new JLabel("Number of Frames:");
        framesLabel.setFont(new Font("Arial", Font.PLAIN, 16)); 
        framesLabel.setForeground(textColor); // Dark gray text
        inputPanel.add(framesLabel);

        numFramesField = new JTextField();
        numFramesField.setFont(new Font("Arial", Font.PLAIN, 16)); 
        numFramesField.setBackground(Color.WHITE); // White background
        numFramesField.setForeground(textColor); // Dark gray text
        inputPanel.add(numFramesField);

        JButton runButton = new JButton("Run");
        runButton.setFont(new Font("Arial", Font.PLAIN, 16)); // Increased font size
        runButton.setBackground(buttonColor); // Blue background
        runButton.setForeground(Color.WHITE); // White text
        runButton.setFocusPainted(false); // Remove focus border
        inputPanel.add(runButton);

        add(inputPanel, BorderLayout.NORTH);

        // Tabbed Pane for Results
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14)); // Increased font size
        tabbedPane.setBackground(backgroundColor); // Light gray background
        tabbedPane.setForeground(textColor); // Dark gray text
        add(tabbedPane, BorderLayout.CENTER);

        // Best Algorithm Label
        bestAlgorithmLabel = new JLabel("Best Algorithm: ");
        bestAlgorithmLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Increased font size
        bestAlgorithmLabel.setForeground(new Color(0, 100, 0)); // Dark green text
        bestAlgorithmLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Added padding
        bestAlgorithmLabel.setBackground(highlightColor); // Light orange background
        bestAlgorithmLabel.setOpaque(true); // Make the background visible
        add(bestAlgorithmLabel, BorderLayout.SOUTH);

        // Button Action
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String referenceString = referenceStringField.getText();
                int numFrames = Integer.parseInt(numFramesField.getText());
                int[] numbers = Arrays.stream(referenceString.split(","))
                                      .mapToInt(Integer::parseInt)
                                      .toArray();

                // Clear previous results
                tabbedPane.removeAll();
                bestAlgorithmLabel.setText("Best Algorithm: ");

                // Run all algorithms and store their results
                Map<String, Map<String, Object>> results = new HashMap<>();
                results.put("FIFO", FIFO.fifo(numbers, numFrames));
                results.put("LRU", LRU.lru(numbers, numFrames));
                results.put("LFU", LFU.lfu(numbers, numFrames));
                results.put("MFU", MFU.mfu(numbers, numFrames));

                // Display results for each algorithm
                for (Map.Entry<String, Map<String, Object>> entry : results.entrySet()) {
                    displayResults(entry.getKey(), entry.getValue());
                }

                // Determine the best algorithm(s)
                List<String> bestAlgorithms = determineBestAlgorithms(results);
                String bestAlgorithmsText = String.join(", ", bestAlgorithms); // Join multiple algorithms with a comma
                bestAlgorithmLabel.setText("Best Algorithm(s): " + bestAlgorithmsText);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void displayResults(String algorithmName, Map<String, Object> result) {
        // Create a table model
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Step");
        model.addColumn("Page");
        model.addColumn("Hit/Miss");
        model.addColumn("Frames");

        // Add data to the table model
        List<Map<String, Object>> steps = (List<Map<String, Object>>) result.get("result");
        for (int i = 0; i < steps.size(); i++) {
            Map<String, Object> step = steps.get(i);
            model.addRow(new Object[]{
                i + 1, // Step number
                step.get("page"), // Page
                step.get("hit_miss"), // Hit/Miss
                step.get("frames") // Frames
            });
        }

        // Create a table with the model
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14)); // Increased font size
        table.setRowHeight(25); // Increased row height
        table.setBackground(Color.WHITE); // White background
        table.setForeground(new Color(50, 50, 50)); // Dark gray text
        table.setGridColor(new Color(200, 200, 200)); // Light gray grid lines

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Added padding

        // Create a panel to hold the table and total faults label
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Added padding
        panel.setBackground(new Color(245, 245, 245)); // Light gray background

        // Add total page faults label
        JLabel faultsLabel = new JLabel("Total Page Faults: " + result.get("total_faults"));
        faultsLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Increased font size
        faultsLabel.setForeground(new Color(0, 0, 150)); // Dark blue text
        faultsLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Added padding
        panel.add(faultsLabel, BorderLayout.SOUTH);

        // Add the table to the panel
        panel.add(scrollPane, BorderLayout.CENTER);

        // Add the panel to a new tab
        tabbedPane.addTab(algorithmName, panel);
    }

    private List<String> determineBestAlgorithms(Map<String, Map<String, Object>> results) {
        List<String> bestAlgorithms = new ArrayList<>();
        int minFaults = Integer.MAX_VALUE;

        // Find the minimum number of page faults
        for (Map.Entry<String, Map<String, Object>> entry : results.entrySet()) {
            int faults = (int) entry.getValue().get("total_faults");
            if (faults < minFaults) {
                minFaults = faults;
            }
        }

        // Find all algorithms with the minimum number of page faults
        for (Map.Entry<String, Map<String, Object>> entry : results.entrySet()) {
            int faults = (int) entry.getValue().get("total_faults");
            if (faults == minFaults) {
                bestAlgorithms.add(entry.getKey() + " (Faults: " + faults + ")");
            }
        }

        return bestAlgorithms;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PageReplacementUI ui = new PageReplacementUI();
            ui.setVisible(true);
        });
    }
}