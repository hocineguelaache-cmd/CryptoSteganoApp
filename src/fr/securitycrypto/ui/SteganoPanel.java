package fr.securitycrypto.ui;

import fr.securitycrypto.service.SteganoService;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class SteganoPanel extends JPanel {
    private JTextField imagePathField, keyEncryptField, stegoPathField, keyDecryptField;
    private JTextArea messageArea, extractedArea;
    private JButton selectImageBtn, generateKeyBtn, encryptAndHideBtn, selectStegoImageBtn, extractAndDecryptBtn;
    private SteganoService steganoService;

    public SteganoPanel() {
        steganoService = new SteganoService();
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 248, 255));
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- PARTIE CHIFFREMENT + CACHAGE ---
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Image PNG (cacher dedans):"), gbc);
        imagePathField = new JTextField(30);
        gbc.gridx = 1;
        mainPanel.add(imagePathField, gbc);
        selectImageBtn = new JButton("Parcourir");
        gbc.gridx = 2;
        mainPanel.add(selectImageBtn, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Message à cacher:"), gbc);
        messageArea = new JTextArea(5, 30);
        JScrollPane scrollMsg = new JScrollPane(messageArea);
        gbc.gridx = 1; gbc.gridwidth = 2;
        mainPanel.add(scrollMsg, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Clé AES (chiffrement):"), gbc);
        keyEncryptField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(keyEncryptField, gbc);
        generateKeyBtn = new JButton("Générer clé");
        gbc.gridx = 2;
        mainPanel.add(generateKeyBtn, gbc);

        encryptAndHideBtn = new JButton("Chiffrer & Cacher dans l'image");
        gbc.gridx = 1; gbc.gridy = 3;
        mainPanel.add(encryptAndHideBtn, gbc);

        // --- SÉPARATEUR ---
        gbc.gridy = 4;
        JSeparator sep = new JSeparator();
        mainPanel.add(sep, gbc);

        // --- PARTIE DÉCHIFFREMENT + EXTRACTION ---
        gbc.gridy = 5;
        mainPanel.add(new JLabel("---- DÉCHIFFREMENT ----"), gbc);
        gbc.gridy = 6;
        mainPanel.add(new JLabel("Image contenant le message caché:"), gbc);
        stegoPathField = new JTextField(30);
        gbc.gridx = 1;
        mainPanel.add(stegoPathField, gbc);
        selectStegoImageBtn = new JButton("Parcourir");
        gbc.gridx = 2;
        mainPanel.add(selectStegoImageBtn, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        mainPanel.add(new JLabel("Clé AES (déchiffrement):"), gbc);
        keyDecryptField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(keyDecryptField, gbc);
        // Pas de bouton générer ici, l'utilisateur doit entrer la clé reçue

        gbc.gridx = 1; gbc.gridy = 8;
        extractAndDecryptBtn = new JButton("Extraire & Déchiffrer");
        mainPanel.add(extractAndDecryptBtn, gbc);

        gbc.gridy = 9;
        mainPanel.add(new JLabel("Message extrait et déchiffré:"), gbc);
        extractedArea = new JTextArea(5, 30);
        extractedArea.setEditable(false);
        JScrollPane scrollExtract = new JScrollPane(extractedArea);
        gbc.gridx = 1; gbc.gridwidth = 2;
        mainPanel.add(scrollExtract, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // --- ACTIONS ---
        selectImageBtn.addActionListener(e -> chooseImage(imagePathField));
        selectStegoImageBtn.addActionListener(e -> chooseImage(stegoPathField));
        generateKeyBtn.addActionListener(e -> generateKey());
        encryptAndHideBtn.addActionListener(e -> encryptAndHide());
        extractAndDecryptBtn.addActionListener(e -> extractAndDecrypt());
    }

    private void chooseImage(JTextField field) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PNG Images", "png"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            field.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void generateKey() {
        String key = steganoService.generateAESKey();
        keyEncryptField.setText(key);
        JOptionPane.showMessageDialog(this, "Clé AES générée (copiez-la pour le déchiffrement) : " + key, "Clé générée", JOptionPane.INFORMATION_MESSAGE);
    }

    private void encryptAndHide() {
        String imagePath = imagePathField.getText();
        String message = messageArea.getText();
        String keyHex = keyEncryptField.getText().trim();
        if (imagePath.isEmpty() || message.isEmpty() || keyHex.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs (image, message, clé).", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            File outputFile = steganoService.encryptAndHide(imagePath, message, keyHex);
            JOptionPane.showMessageDialog(this, "Image générée avec succès : " + outputFile.getAbsolutePath() + "\nGardez précieusement la clé pour déchiffrer.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Échec", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void extractAndDecrypt() {
        String stegoPath = stegoPathField.getText();
        String keyHex = keyDecryptField.getText().trim();  // ← utilisation du champ spécifique déchiffrement
        if (stegoPath.isEmpty() || keyHex.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner l'image stéganographiée et entrer la clé de déchiffrement.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            String message = steganoService.extractAndDecrypt(stegoPath, keyHex);
            extractedArea.setText(message);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Extraction/déchiffrement échoué : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}