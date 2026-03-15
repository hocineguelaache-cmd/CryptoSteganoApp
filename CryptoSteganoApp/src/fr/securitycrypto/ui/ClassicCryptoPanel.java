package fr.securitycrypto.ui;

import fr.securitycrypto.service.ClassicCryptoService;
import javax.swing.*;
import java.awt.*;

public class ClassicCryptoPanel extends JPanel {
    private JComboBox<String> algorithmCombo;
    private JTextArea inputText, outputText;
    private JTextField keyText;  // ← modifié : JTextField au lieu de JTextArea
    private JButton encryptBtn, decryptBtn;
    private ClassicCryptoService cryptoService;

    public ClassicCryptoPanel() {
        cryptoService = new ClassicCryptoService();
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 248, 255));
        initUI();
    }

    private void initUI() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(240, 248, 255));
        topPanel.add(new JLabel("Algorithme:"));
        algorithmCombo = new JComboBox<>(new String[]{"César", "Playfair", "Vigenère"});
        topPanel.add(algorithmCombo);
        topPanel.add(new JLabel("Clé:"));
        keyText = new JTextField(20);  // ← correct
        topPanel.add(keyText);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerPanel.setBackground(new Color(240, 248, 255));

        centerPanel.add(new JLabel("Message / Texte chiffré:"));
        inputText = new JTextArea(6, 30);
        centerPanel.add(new JScrollPane(inputText));

        centerPanel.add(new JLabel("Résultat:"));
        outputText = new JTextArea(6, 30);
        outputText.setEditable(false);
        centerPanel.add(new JScrollPane(outputText));

        add(centerPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout());
        encryptBtn = new JButton("Chiffrer");
        decryptBtn = new JButton("Déchiffrer");
        btnPanel.add(encryptBtn);
        btnPanel.add(decryptBtn);
        add(btnPanel, BorderLayout.SOUTH);

        encryptBtn.addActionListener(e -> performCrypto(true));
        decryptBtn.addActionListener(e -> performCrypto(false));
    }

    private void performCrypto(boolean encrypt) {
        String algo = (String) algorithmCombo.getSelectedItem();
        String key = keyText.getText().trim();
        String text = inputText.getText();

        if (text.isEmpty() || key.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir le texte et la clé.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String result;
            if (encrypt) {
                result = cryptoService.encrypt(algo, text, key);
            } else {
                result = cryptoService.decrypt(algo, text, key);
            }
            outputText.setText(result);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}