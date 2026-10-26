
package com.mycompany.warungkita;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {

    private final Color hijau = new Color(35, 122, 69);
    private final Color latar = new Color(245, 248, 245);

    public Dashboard() {
        setTitle("WarungKita - Dashboard");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(latar);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(hijau);
        header.setBorder(
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        );

        JLabel judul = new JLabel("WarungKita");
        judul.setFont(new Font("Arial", Font.BOLD, 30));
        judul.setForeground(Color.WHITE);

        JLabel subjudul = new JLabel(
            "SISTEM INFORMASI TOKO"
        );
        subjudul.setFont(new Font("Arial", Font.PLAIN, 13));
        subjudul.setForeground(new Color(225, 245, 230));

        JPanel teksHeader = new JPanel(new GridLayout(2, 1, 0, 5));
        teksHeader.setOpaque(false);
        teksHeader.add(judul);
        teksHeader.add(subjudul);

        header.add(teksHeader, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // Bagian tengah
        JPanel tengah = new JPanel(new BorderLayout(10, 20));
        tengah.setBackground(latar);
        tengah.setBorder(
            BorderFactory.createEmptyBorder(25, 35, 25, 35)
        );

        JLabel sambutan = new JLabel(
            "Selamat Datang di Dashboard!",
            SwingConstants.LEFT
        );
        sambutan.setFont(new Font("Arial", Font.BOLD, 21));
        sambutan.setForeground(new Color(45, 55, 45));

        JLabel keterangan = new JLabel(
            "Pilih menu di bawah untuk mengelola toko."
        );
        keterangan.setFont(new Font("Arial", Font.PLAIN, 14));
        keterangan.setForeground(new Color(100, 110, 100));

        JPanel informasi = new JPanel(new GridLayout(2, 1, 0, 5));
        informasi.setOpaque(false);
        informasi.add(sambutan);
        informasi.add(keterangan);

        tengah.add(informasi, BorderLayout.NORTH);

        // Tombol menu
        JPanel menu = new JPanel(new GridLayout(2, 2, 18, 18));
        menu.setOpaque(false);

        JButton barang = buatTombol("Data Barang", "Kelola stok dan harga barang");
        JButton transaksi = buatTombol("Transaksi Penjualan", "Catat penjualan toko");
        JButton laporan = buatTombol("Laporan Penjualan", "Lihat riwayat dan total penjualan");
        JButton logout = buatTombol("Logout", "Keluar dari aplikasi");

        menu.add(barang);
        menu.add(transaksi);
        menu.add(laporan);
        menu.add(logout);

        tengah.add(menu, BorderLayout.CENTER);
        add(tengah, BorderLayout.CENTER);

        // Footer
        JLabel footer = new JLabel(
            "WarungKita | Aplikasi Pengelolaan Toko",
            SwingConstants.CENTER
        );
        footer.setFont(new Font("Arial", Font.PLAIN, 12));
        footer.setForeground(new Color(110, 120, 110));
        footer.setBorder(
            BorderFactory.createEmptyBorder(10, 10, 15, 10)
        );

        add(footer, BorderLayout.SOUTH);

        // Fungsi tombol tetap terhubung
        barang.addActionListener(e ->
            new DataBarang().setVisible(true)
        );

        transaksi.addActionListener(e ->
            new TransaksiPenjualan().setVisible(true)
        );

        laporan.addActionListener(e ->
            new LaporanPenjualan().setVisible(true)
        );

        logout.addActionListener(e -> {
            dispose();
            new WarungKita().tampilkanLogin();
        });
    }

    private JButton buatTombol(String judul, String keterangan) {
        JButton tombol = new JButton(
            "<html><div style='text-align:center;'>"
            + "<b>" + judul + "</b><br>"
            + "<span style='font-size:10px;'>"
            + keterangan + "</span></div></html>"
        );

        tombol.setFont(new Font("Arial", Font.PLAIN, 15));
        tombol.setForeground(hijau);
        tombol.setBackground(Color.WHITE);
        tombol.setFocusPainted(false);
        tombol.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tombol.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(215, 225, 215), 1
                ),
                BorderFactory.createEmptyBorder(15, 10, 15, 10)
            )
        );

        return tombol;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new Dashboard().setVisible(true)
        );
    }
}
