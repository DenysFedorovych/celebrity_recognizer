package com.examples;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ImageAnalyzerUI {
    private JFrame frame;
    private JComboBox<String> modeComboBox;
    private JButton chooseFileButton;
    private JTextArea resultTextArea;
    private JLabel imageLabel;
    private String selectedFilePath;

    public void createAndShowGUI() {
        frame = new JFrame("Image Analyzer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        String[] modes = {"Впізнавання знаменитих людей", "Розпізнавання міток", "Розпізнавання облич"};
        modeComboBox = new JComboBox<>(modes);
        topPanel.add(modeComboBox);

        chooseFileButton = new JButton("Вибрати файл");
        chooseFileButton.addActionListener(new ChooseFileButtonListener());
        topPanel.add(chooseFileButton);

        frame.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(1, 2));

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        centerPanel.add(imageLabel);

        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        centerPanel.add(scrollPane);

        frame.add(centerPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private class ChooseFileButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("/Users/denisfedorovich/Desktop/TestPhotos"));
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                selectedFilePath = selectedFile.getAbsolutePath();
                displayImage(selectedFilePath);
                performAnalysis();
            }
        }
    }

    private void displayImage(String imagePath) {
        ImageIcon imageIcon = new ImageIcon(imagePath);
        Image image = imageIcon.getImage();
        Image scaledImage = image.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaledImage));
    }

    private void performAnalysis() {
        String selectedMode = (String) modeComboBox.getSelectedItem();
        if (selectedFilePath != null && selectedMode != null) {
            String result = "";
            switch (selectedMode) {
                case "Впізнавання знаменитих людей":
                    result = RecognizeCelebrities.recogizeCelebrities(selectedFilePath);
                    break;
                case "Розпізнавання міток":
                    result = DetectLabels.detectLabels(selectedFilePath);
                    break;
                case "Розпізнавання облич":
                    result = DetectFaces.detectFaces(selectedFilePath);
                    break;
            }
            resultTextArea.setText(result);
        } else {
            JOptionPane.showMessageDialog(frame, "Будь ласка, виберіть режим та файл.", "Помилка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
