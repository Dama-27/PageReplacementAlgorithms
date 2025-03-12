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
    private JTabbedPane tabbedPane;
    private JLabel bestAlgorithmLabel;

    public PageReplacementUI() {
        setTitle("Page Replacement Algorithms");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Color backgroundColor = new Color(245, 245, 245);
        Color buttonColor = new Color(0, 120, 215);
        Color textColor = new Color(50, 50, 50);
        Color highlightColor = new Color(255, 223, 186);

        getContentPane().setBackground(backgroundColor);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.setBackground(backgroundColor);

        JLabel refStringLabel = new JLabel("Reference String (comma-separated):");
        refStringLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        refStringLabel.setForeground(textColor);
        inputPanel.add(refStringLabel);

        referenceStringField = new JTextField();
        referenceStringField.setFont(new Font("Arial", Font.PLAIN, 16));
        referenceStringField.setBackground(Color.WHITE);
        referenceStringField.setForeground(textColor);
        inputPanel.add(referenceStringField);

        JLabel framesLabel = new JLabel("Number of Frames:");
        framesLabel.setFont(new Font("Arial", Font.PLAIN, 16)); 
        framesLabel.setForeground(textColor);
        inputPanel.add(framesLabel);

        numFramesField = new JTextField();
        numFramesField.setFont(new Font("Arial", Font.PLAIN, 16)); 
        numFramesField.setBackground(Color.WHITE);
        numFramesField.setForeground(textColor);
        inputPanel.add(numFramesField);

        JButton runButton = new JButton("Run");
        runButton.setFont(new Font("Arial", Font.PLAIN, 16));
        runButton.setBackground(buttonColor);
        runButton.setForeground(Color.WHITE);
        runButton.setFocusPainted(false);
        inputPanel.add(runButton);

        add(inputPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.setBackground(backgroundColor);
        tabbedPane.setForeground(textColor);
        add(tabbedPane, BorderLayout.CENTER);

        bestAlgorithmLabel = new JLabel("Best Algorithm: ");
        bestAlgorithmLabel.setFont(new Font("Arial", Font.BOLD, 16));
        bestAlgorithmLabel.setForeground(new Color(0, 100, 0));
        bestAlgorithmLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bestAlgorithmLabel.setBackground(highlightColor);
        bestAlgorithmLabel.setOpaque(true);
        add(bestAlgorithmLabel, BorderLayout.SOUTH);

        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String referenceString = referenceStringField.getText();
                int numFrames = Integer.parseInt(numFramesField.getText());
                int[] numbers = Arrays.stream(referenceString.split(","))
                                      .mapToInt(Integer::parseInt)
                                      .toArray();

                tabbedPane.removeAll();
                bestAlgorithmLabel.setText("Best Algorithm: ");

                Map<String, Map<String, Object>> results = new HashMap<>();
                results.put("FIFO", FIFO.fifo(numbers, numFrames));
                results.put("LRU", LRU.lru(numbers, numFrames));
                results.put("LFU", LFU.lfu(numbers, numFrames));
                results.put("MFU", MFU.mfu(numbers, numFrames));

                for (Map.Entry<String, Map<String, Object>> entry : results.entrySet()) {
                    displayResults(entry.getKey(), entry.getValue());
                }

                List<String> bestAlgorithms = determineBestAlgorithms(results);
                String bestAlgorithmsText = String.join(", ", bestAlgorithms);
                bestAlgorithmLabel.setText("Best Algorithm(s): " + bestAlgorithmsText);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void displayResults(String algorithmName, Map<String, Object> result) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Step");
        model.addColumn("Page");
        model.addColumn("Hit/Miss");
        model.addColumn("Frames");

        List<Map<String, Object>> steps = (List<Map<String, Object>>) result.get("result");
        for (int i = 0; i < steps.size(); i++) {
            Map<String, Object> step = steps.get(i);
            model.addRow(new Object[]{
                i + 1,
                step.get("page"),
                step.get("hit_miss"),
                step.get("frames")
            });
        }

        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setBackground(Color.WHITE);
        table.setForeground(new Color(50, 50, 50));
        table.setGridColor(new Color(200, 200, 200));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(245, 245, 245));

        JLabel faultsLabel = new JLabel("Total Page Faults: " + result.get("total_faults"));
        faultsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        faultsLabel.setForeground(new Color(0, 0, 150));
        faultsLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(faultsLabel, BorderLayout.SOUTH);

        panel.add(scrollPane, BorderLayout.CENTER);

        tabbedPane.addTab(algorithmName, panel);
    }

    private List<String> determineBestAlgorithms(Map<String, Map<String, Object>> results) {
        List<String> bestAlgorithms = new ArrayList<>();
        int minFaults = Integer.MAX_VALUE;

        for (Map.Entry<String, Map<String, Object>> entry : results.entrySet()) {
            int faults = (int) entry.getValue().get("total_faults");
            if (faults < minFaults) {
                minFaults = faults;
            }
        }

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