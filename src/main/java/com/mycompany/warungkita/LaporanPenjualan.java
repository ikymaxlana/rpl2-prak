package com.mycompany.warungkita;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class LaporanPenjualan extends JFrame {

    private JTable tabel;
    private DefaultTableModel model;
    private JLabel totalLabel;

    private final Color hijau = new Color(35, 122, 69);
    private final Color hijauMuda = new Color(232, 245, 233);
    private final Color putih = Color.WHITE;
    private final Color abu = new Color(245, 247, 246);

    private final NumberFormat rupiah =
        NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    public LaporanPenjualan() {
        setTitle("Laporan Penjualan - WarungKita");
        setSize(950, 560);
        setMinimumSize(new Dimension(750, 450));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(abu);

        // Header
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(hijau);
        header.setBorder(
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        JLabel judul = new JLabel("LAPORAN PENJUALAN");
        judul.setFont(new Font("Arial", Font.BOLD, 23));
        judul.setForeground(putih);
        judul.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subjudul = new JLabel(
            "WarungKita | Rekap dan Riwayat Transaksi"
        );
        subjudul.setFont(new Font("Arial", Font.PLAIN, 13));
        subjudul.setForeground(new Color(225, 245, 230));
        subjudul.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(judul);
        header.add(Box.createVerticalStrut(5));
        header.add(subjudul);

        add(header, BorderLayout.NORTH);

        // Tabel laporan
        model = new DefaultTableModel(
            new String[]{
                "ID Transaksi", "Tanggal", "Rincian Barang", "Total"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabel = new JTable(model);
        tabel.setRowHeight(32);
        tabel.setFont(new Font("Arial", Font.PLAIN, 13));
        tabel.setSelectionBackground(hijauMuda);
        tabel.setSelectionForeground(Color.BLACK);
        tabel.setShowVerticalLines(false);
        tabel.setGridColor(new Color(225, 230, 227));
        tabel.setFillsViewportHeight(true);

        JTableHeader kepalaTabel = tabel.getTableHeader();
        kepalaTabel.setFont(new Font("Arial", Font.BOLD, 13));
        kepalaTabel.setBackground(hijauMuda);
        kepalaTabel.setForeground(hijau);
        kepalaTabel.setPreferredSize(new Dimension(100, 38));
        kepalaTabel.setReorderingAllowed(false);

        DefaultTableCellRenderer rataTengah =
            new DefaultTableCellRenderer();
        rataTengah.setHorizontalAlignment(SwingConstants.CENTER);
        tabel.getColumnModel().getColumn(0).setCellRenderer(rataTengah);
        tabel.getColumnModel().getColumn(1).setCellRenderer(rataTengah);

        DefaultTableCellRenderer rataKanan =
            new DefaultTableCellRenderer();
        rataKanan.setHorizontalAlignment(SwingConstants.RIGHT);
        tabel.getColumnModel().getColumn(3).setCellRenderer(rataKanan);

        tabel.getColumnModel().getColumn(0).setPreferredWidth(100);
        tabel.getColumnModel().getColumn(1).setPreferredWidth(160);
        tabel.getColumnModel().getColumn(2).setPreferredWidth(400);
        tabel.getColumnModel().getColumn(3).setPreferredWidth(160);

        JScrollPane scroll = new JScrollPane(tabel);
        scroll.setBorder(
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        );

        JPanel areaTabel = new JPanel(new BorderLayout());
        areaTabel.setBackground(putih);
        areaTabel.setBorder(
            BorderFactory.createEmptyBorder(20, 25, 10, 25)
        );
        areaTabel.add(scroll, BorderLayout.CENTER);

        add(areaTabel, BorderLayout.CENTER);

        // Bagian bawah
        JPanel bawah = new JPanel(new BorderLayout(15, 10));
        bawah.setBackground(putih);
        bawah.setBorder(
            BorderFactory.createEmptyBorder(15, 25, 20, 25)
        );

        totalLabel = new JLabel("Total Seluruh Penjualan: Rp0");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(hijau);

        JButton muatUlang = new JButton("Muat Ulang");
        muatUlang.setFont(new Font("Arial", Font.BOLD, 12));
        muatUlang.setBackground(hijau);
        muatUlang.setForeground(putih);
        muatUlang.setFocusPainted(false);
        muatUlang.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton kembali = new JButton("Kembali");
        kembali.setFont(new Font("Arial", Font.BOLD, 12));
        kembali.setBackground(new Color(230, 235, 232));
        kembali.setForeground(new Color(50, 60, 54));
        kembali.setFocusPainted(false);
        kembali.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel tombol = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        tombol.setBackground(putih);
        tombol.add(muatUlang);
        tombol.add(kembali);

        bawah.add(totalLabel, BorderLayout.WEST);
        bawah.add(tombol, BorderLayout.EAST);

        add(bawah, BorderLayout.SOUTH);

        muatUlang.addActionListener(e -> muatLaporan());
        kembali.addActionListener(e -> dispose());

        muatLaporan();
    }

    private void muatLaporan() {
        model.setRowCount(0);

        String sql =
            "SELECT t.id_transaksi, t.tanggal, t.total, " +
            "GROUP_CONCAT(CONCAT(b.nama_barang, ' (', d.jumlah, ')') " +
            "ORDER BY b.nama_barang SEPARATOR ', ') AS rincian " +
            "FROM transaksi t " +
            "LEFT JOIN detail_transaksi d " +
            "ON t.id_transaksi = d.id_transaksi " +
            "LEFT JOIN barang b ON d.id_barang = b.id_barang " +
            "GROUP BY t.id_transaksi, t.tanggal, t.total " +
            "ORDER BY t.tanggal DESC, t.id_transaksi DESC";

        double totalSemua = 0;

        try (Connection con = Koneksi.getKoneksi()) {
            if (con == null) {
                JOptionPane.showMessageDialog(this,
                    "Koneksi database gagal. Periksa XAMPP dan database.");
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                SimpleDateFormat formatTanggal =
                    new SimpleDateFormat("dd-MM-yyyy HH:mm");

                while (rs.next()) {
                    double total = rs.getDouble("total");
                    totalSemua += total;

                    Timestamp tanggal = rs.getTimestamp("tanggal");
                    String rincian = rs.getString("rincian");

                    if (rincian == null || rincian.isEmpty()) {
                        rincian = "-";
                    }

                    model.addRow(new Object[]{
                        rs.getInt("id_transaksi"),
                        tanggal == null
                            ? "-" : formatTanggal.format(tanggal),
                        rincian,
                        rupiah.format(total)
                    });
                }
            }

            totalLabel.setText(
                "Total Seluruh Penjualan: " + rupiah.format(totalSemua)
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Gagal memuat laporan: " + e.getMessage(),
                "Kesalahan",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new LaporanPenjualan().setVisible(true)
        );
    }
}
