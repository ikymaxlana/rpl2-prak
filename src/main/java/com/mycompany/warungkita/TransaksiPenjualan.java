package com.mycompany.warungkita;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
import java.text.NumberFormat;
import java.util.Locale;

public class TransaksiPenjualan extends JFrame {

    private JComboBox<String> pilihanBarang;
    private JTextField jumlah;
    private JLabel hargaLabel, totalLabel;
    private JTable tabel;
    private DefaultTableModel model;

    private int idBarang;
    private BigDecimal hargaBarang = BigDecimal.ZERO;
    private BigDecimal total = BigDecimal.ZERO;

    private final Color hijau = new Color(35, 122, 69);
    private final Color latar = new Color(245, 248, 245);
    private final NumberFormat rupiah =
        NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    public TransaksiPenjualan() {
        setTitle("Transaksi Penjualan - WarungKita");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel utama = new JPanel(new BorderLayout(15, 15));
        utama.setBackground(latar);
        utama.setBorder(
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(hijau);
        header.setBorder(
            BorderFactory.createEmptyBorder(18, 20, 18, 20)
        );

        JLabel judul = new JLabel("TRANSAKSI PENJUALAN");
        judul.setFont(new Font("Arial", Font.BOLD, 24));
        judul.setForeground(Color.WHITE);

        JLabel subjudul = new JLabel(
            "Pilih barang, tentukan jumlah, dan simpan transaksi"
        );
        subjudul.setFont(new Font("Arial", Font.PLAIN, 13));
        subjudul.setForeground(new Color(225, 245, 230));

        JPanel teksHeader = new JPanel(new GridLayout(2, 1, 0, 5));
        teksHeader.setOpaque(false);
        teksHeader.add(judul);
        teksHeader.add(subjudul);
        header.add(teksHeader, BorderLayout.WEST);

        utama.add(header, BorderLayout.NORTH);

        // Form pemilihan barang
        JPanel bagianAtas = new JPanel(new BorderLayout(10, 10));
        bagianAtas.setBackground(latar);

        JLabel judulForm = new JLabel("Informasi Barang");
        judulForm.setFont(new Font("Arial", Font.BOLD, 16));
        judulForm.setForeground(hijau);

        JPanel form = new JPanel(new GridLayout(2, 4, 12, 8));
        form.setBackground(Color.WHITE);
        form.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 225)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
            )
        );

        pilihanBarang = new JComboBox<>();
        jumlah = new JTextField("1");
        hargaLabel = new JLabel("Rp0");
        totalLabel = new JLabel("Rp0");

        pilihanBarang.setFont(new Font("Arial", Font.PLAIN, 13));
        jumlah.setFont(new Font("Arial", Font.PLAIN, 13));

        hargaLabel.setFont(new Font("Arial", Font.BOLD, 14));
        hargaLabel.setForeground(hijau);

        totalLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalLabel.setForeground(hijau);

        form.add(buatKolom("Pilih Barang", pilihanBarang));
        form.add(buatKolom("Harga Satuan", hargaLabel));
        form.add(buatKolom("Jumlah", jumlah));
        form.add(buatKolom("Total Transaksi", totalLabel));

        bagianAtas.add(judulForm, BorderLayout.NORTH);
        bagianAtas.add(form, BorderLayout.CENTER);

        // Tabel item transaksi
        model = new DefaultTableModel(
            new String[]{
                "ID Barang", "Nama Barang", "Harga (Rp)",
                "Jumlah", "Subtotal (Rp)"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabel = new JTable(model);
        tabel.setRowHeight(28);
        tabel.setFont(new Font("Arial", Font.PLAIN, 13));
        tabel.setSelectionBackground(new Color(210, 235, 215));
        tabel.setSelectionForeground(Color.BLACK);
        tabel.setGridColor(new Color(225, 230, 225));
        tabel.setShowVerticalLines(false);
        tabel.setFillsViewportHeight(true);

        JTableHeader kepala = tabel.getTableHeader();
        kepala.setFont(new Font("Arial", Font.BOLD, 13));
        kepala.setBackground(new Color(230, 240, 230));
        kepala.setForeground(hijau);
        kepala.setPreferredSize(new Dimension(0, 35));

        JScrollPane scroll = new JScrollPane(tabel);
        scroll.setBorder(
            BorderFactory.createLineBorder(new Color(220, 228, 220))
        );

        // Tombol
        JPanel tombol = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        tombol.setBackground(latar);

        JButton tambah = buatTombol("Tambah Item", hijau);
        JButton hapus = buatTombol("Hapus Item", new Color(190, 60, 55));
        JButton simpan = buatTombol("Simpan Transaksi", new Color(45, 105, 170));
        JButton kembali = buatTombol("Kembali", new Color(85, 90, 95));

        tombol.add(tambah);
        tombol.add(hapus);
        tombol.add(simpan);
        tombol.add(kembali);

        JPanel tengah = new JPanel(new BorderLayout(10, 12));
        tengah.setBackground(latar);
        tengah.add(bagianAtas, BorderLayout.NORTH);
        tengah.add(scroll, BorderLayout.CENTER);
        tengah.add(tombol, BorderLayout.SOUTH);

        utama.add(tengah, BorderLayout.CENTER);
        setContentPane(utama);

        muatBarang();

        pilihanBarang.addActionListener(e -> tampilkanHarga());

        tambah.addActionListener(e -> tambahItem());
        hapus.addActionListener(e -> hapusItem());
        simpan.addActionListener(e -> simpanTransaksi());
        kembali.addActionListener(e -> dispose());

        tampilkanHarga();
    }

    private JPanel buatKolom(String label, JComponent komponen) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(Color.WHITE);

        JLabel teks = new JLabel(label);
        teks.setFont(new Font("Arial", Font.BOLD, 12));
        teks.setForeground(new Color(65, 75, 65));

        panel.add(teks, BorderLayout.NORTH);
        panel.add(komponen, BorderLayout.CENTER);
        return panel;
    }

    private JButton buatTombol(String teks, Color warna) {
        JButton tombol = new JButton(teks);
        tombol.setFont(new Font("Arial", Font.BOLD, 12));
        tombol.setBackground(warna);
        tombol.setForeground(Color.WHITE);
        tombol.setFocusPainted(false);
        tombol.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tombol.setBorder(
            BorderFactory.createEmptyBorder(9, 16, 9, 16)
        );
        return tombol;
    }

    private void muatBarang() {
        pilihanBarang.removeAllItems();

        String sql = "SELECT id_barang, nama_barang FROM barang "
                   + "WHERE stok > 0 ORDER BY nama_barang";

        try (Connection con = Koneksi.getKoneksi();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                pilihanBarang.addItem(
                    rs.getInt("id_barang") + " - "
                    + rs.getString("nama_barang")
                );
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Gagal memuat barang: " + e.getMessage());
        }
    }

    private void tampilkanHarga() {
        String pilihan = (String) pilihanBarang.getSelectedItem();

        if (pilihan == null) {
            idBarang = 0;
            hargaBarang = BigDecimal.ZERO;
            hargaLabel.setText("Rp0");
            return;
        }

        try {
            idBarang = Integer.parseInt(pilihan.split(" - ")[0]);

            try (Connection con = Koneksi.getKoneksi();
                 PreparedStatement ps = con.prepareStatement(
                     "SELECT harga FROM barang WHERE id_barang = ?")) {

                ps.setInt(1, idBarang);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        hargaBarang = rs.getBigDecimal("harga");
                        hargaLabel.setText(rupiah.format(hargaBarang));
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Gagal mengambil harga: " + e.getMessage());
        }
    }

    private void tambahItem() {
        if (idBarang == 0) {
            JOptionPane.showMessageDialog(this,
                "Belum ada barang tersedia.");
            return;
        }

        int qty;

        try {
            qty = Integer.parseInt(jumlah.getText().trim());

            if (qty <= 0) {
                JOptionPane.showMessageDialog(this,
                    "Jumlah harus lebih dari 0.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Jumlah harus berupa bilangan bulat.");
            return;
        }

        try (Connection con = Koneksi.getKoneksi();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT nama_barang, harga, stok FROM barang WHERE id_barang=?")) {

            ps.setInt(1, idBarang);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this,
                        "Barang tidak ditemukan.");
                    return;
                }

                int stokTersedia = rs.getInt("stok");
                String namaBarang = rs.getString("nama_barang");
                BigDecimal harga = rs.getBigDecimal("harga");

                int sudahAda = 0;

                for (int i = 0; i < model.getRowCount(); i++) {
                    if (Integer.parseInt(
                            model.getValueAt(i, 0).toString()) == idBarang) {
                        sudahAda += Integer.parseInt(
                            model.getValueAt(i, 3).toString());
                    }
                }

                if (qty + sudahAda > stokTersedia) {
                    JOptionPane.showMessageDialog(this,
                        "Stok tidak cukup. Stok tersedia: " + stokTersedia);
                    return;
                }

                BigDecimal subtotal =
                    harga.multiply(BigDecimal.valueOf(qty));

                model.addRow(new Object[]{
                    idBarang,
                    namaBarang,
                    harga.toPlainString(),
                    qty,
                    subtotal.toPlainString()
                });

                hitungTotal();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Gagal menambahkan item: " + e.getMessage());
        }
    }

    private void hapusItem() {
        int baris = tabel.getSelectedRow();

        if (baris < 0) {
            JOptionPane.showMessageDialog(this,
                "Pilih item yang ingin dihapus.");
            return;
        }

        model.removeRow(baris);
        hitungTotal();
    }

    private void hitungTotal() {
        total = BigDecimal.ZERO;

        for (int i = 0; i < model.getRowCount(); i++) {
            BigDecimal subtotal = new BigDecimal(
                model.getValueAt(i, 4).toString());
            total = total.add(subtotal);
        }

        totalLabel.setText(rupiah.format(total));
    }

    private void simpanTransaksi() {
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "Tambahkan barang terlebih dahulu.");
            return;
        }

        Connection con = null;

        try {
            con = Koneksi.getKoneksi();

            if (con == null) {
                throw new SQLException("Koneksi database gagal.");
            }

            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO transaksi (total) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS)) {

                ps.setBigDecimal(1, total);
                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new SQLException(
                            "ID transaksi tidak ditemukan.");
                    }

                    int idTransaksi = keys.getInt(1);

                    for (int i = 0; i < model.getRowCount(); i++) {
                        int id = Integer.parseInt(
                            model.getValueAt(i, 0).toString());
                        int qty = Integer.parseInt(
                            model.getValueAt(i, 3).toString());
                        BigDecimal harga = new BigDecimal(
                            model.getValueAt(i, 2).toString());
                        BigDecimal subtotal = new BigDecimal(
                            model.getValueAt(i, 4).toString());

                        try (PreparedStatement detail = con.prepareStatement(
                            "INSERT INTO detail_transaksi "
                            + "(id_transaksi, id_barang, jumlah, harga, subtotal) "
                            + "VALUES (?, ?, ?, ?, ?)")) {

                            detail.setInt(1, idTransaksi);
                            detail.setInt(2, id);
                            detail.setInt(3, qty);
                            detail.setBigDecimal(4, harga);
                            detail.setBigDecimal(5, subtotal);
                            detail.executeUpdate();
                        }

                        try (PreparedStatement stok = con.prepareStatement(
                            "UPDATE barang SET stok = stok - ? "
                            + "WHERE id_barang = ? AND stok >= ?")) {

                            stok.setInt(1, qty);
                            stok.setInt(2, id);
                            stok.setInt(3, qty);

                            if (stok.executeUpdate() != 1) {
                                throw new SQLException(
                                    "Stok tidak cukup untuk barang ID " + id);
                            }
                        }
                    }
                }
            }

            con.commit();

            JOptionPane.showMessageDialog(this,
                "Transaksi berhasil disimpan!\n"
                + "ID Transaksi: " + "tersimpan\n"
                + "Total: " + rupiah.format(total));

            model.setRowCount(0);
            hitungTotal();
            jumlah.setText("1");
            muatBarang();

        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    // Penanganan rollback
                }
            }

            JOptionPane.showMessageDialog(this,
                "Transaksi gagal disimpan: " + e.getMessage());

        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    // Penanganan penutupan koneksi
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new TransaksiPenjualan().setVisible(true)
        );
    }
}
