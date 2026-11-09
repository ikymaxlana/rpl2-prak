package com.mycompany.warungkita;

import javax.swing.*;
import java.awt.*;

public class WarungKita {

    private static final Color HIJAU = new Color(35, 122, 69);
    private static final Color LATAR = new Color(245, 248, 245);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("Login - WarungKita");
            frame.setSize(480, 430);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);

            JPanel utama = new JPanel(new BorderLayout());
            utama.setBackground(LATAR);

            // Header hijau
            JPanel header = new JPanel(new GridLayout(2, 1, 0, 5));
            header.setBackground(HIJAU);
            header.setBorder(
                BorderFactory.createEmptyBorder(25, 20, 25, 20)
            );

            JLabel judul = new JLabel(
                "WarungKita", SwingConstants.CENTER
            );
            judul.setFont(new Font("Arial", Font.BOLD, 30));
            judul.setForeground(Color.WHITE);

            JLabel subjudul = new JLabel(
                "SISTEM INFORMASI TOKO", SwingConstants.CENTER
            );
            subjudul.setFont(new Font("Arial", Font.PLAIN, 13));
            subjudul.setForeground(new Color(225, 245, 230));

            header.add(judul);
            header.add(subjudul);

            // Form login
            JPanel form = new JPanel(new GridBagLayout());
            form.setBackground(LATAR);
            form.setBorder(
                BorderFactory.createEmptyBorder(20, 35, 20, 35)
            );

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(7, 5, 7, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;

            JLabel sambutan = new JLabel("Silakan Login");
            sambutan.setFont(new Font("Arial", Font.BOLD, 20));
            sambutan.setForeground(new Color(45, 55, 45));

            JLabel labelUser = new JLabel("Username");
            JTextField username = new JTextField(20);
            username.setFont(new Font("Arial", Font.PLAIN, 14));

            JLabel labelPass = new JLabel("Password");
            JPasswordField password = new JPasswordField(20);
            password.setFont(new Font("Arial", Font.PLAIN, 14));

            JButton tombolLogin = new JButton("LOGIN");
            tombolLogin.setFont(new Font("Arial", Font.BOLD, 14));
            tombolLogin.setBackground(HIJAU);
            tombolLogin.setForeground(Color.WHITE);
            tombolLogin.setFocusPainted(false);
            tombolLogin.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
            );

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            form.add(sambutan, gbc);

            gbc.gridy = 1;
            form.add(labelUser, gbc);

            gbc.gridy = 2;
            form.add(username, gbc);

            gbc.gridy = 3;
            form.add(labelPass, gbc);

            gbc.gridy = 4;
            form.add(password, gbc);

            gbc.gridy = 5;
            gbc.insets = new Insets(18, 5, 5, 5);
            form.add(tombolLogin, gbc);

            // Footer
            JLabel footer = new JLabel(
                "WarungKita | Pengelolaan Toko",
                SwingConstants.CENTER
            );
            footer.setFont(new Font("Arial", Font.PLAIN, 11));
            footer.setForeground(new Color(110, 120, 110));
            footer.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 15, 10)
            );

            utama.add(header, BorderLayout.NORTH);
            utama.add(form, BorderLayout.CENTER);
            utama.add(footer, BorderLayout.SOUTH);

            // Fungsi login tetap sama
            tombolLogin.addActionListener(e -> {
                String user = username.getText().trim();
                String pass = new String(password.getPassword());

                if (user.equals("admin") && pass.equals("admin123")) {
                    frame.dispose();
                    new Dashboard().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(
                        frame,
                        "Username atau password salah!",
                        "Login Gagal",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            });

            // Tekan Enter untuk login
            password.addActionListener(e -> tombolLogin.doClick());

            frame.setContentPane(utama);
            frame.setVisible(true);
        });
    }

    public void tampilkanLogin() {
        main(new String[]{});
    }
}
