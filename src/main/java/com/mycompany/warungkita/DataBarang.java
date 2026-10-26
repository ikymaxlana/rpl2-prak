package com.mycompany.warungkita;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import java.math.BigDecimal;

public class DataBarang extends JFrame {

    private JTable tabel;
    private DefaultTableModel model;
    private JTextField nama, kategori, harga, stok;

    private final Color hijau = new Color(35, 122, 69);
    private final Color latar = new Color(245, 248, 245);

    public DataBarang() {
        setTitle("Data Barang - WarungKita");
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

        JLabel judul = new JLabel("DATA BARANG");
        judul.setFont(new Font("Arial", Font.BOLD, 24));
        judul.setForeground(Color.WHITE);

        JLabel subjudul = new JLabel("Kelola barang, harga, kategori, dan stok");
        subjudul.setFont(new Font("Arial", Font.PLAIN, 13));
        subjudul.setForeground(new Color(225, 245, 230));

        JPanel teksHeader = new JPanel(new GridLayout(2, 1, 0, 5));
        teksHeader.setOpaque(false);
        teksHeader.add(judul);
        teksHeader.add(subjudul);
        header.add(teksHeader, BorderLayout.WEST);

        utama.add(header, BorderLayout.NORTH);

        // Tabel barang
        model = new DefaultTableModel(
            new String[]{"ID", "Nama Barang", "Kategori", "Harga (Rp)", "Stok"}, 0
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

        JTableHeader kepalaTabel = tabel.getTableHeader();
        kepalaTabel.setFont(new Font("Arial", Font.BOLD, 13));
        kepalaTabel.setBackground(new Color(230, 240, 230));
        kepalaTabel.setForeground(hijau);
        kepalaTabel.setPreferredSize(new Dimension(0, 35));

        JScrollPane scroll = new JScrollPane(tabel);
        scroll.setBorder(
            BorderFactory.createLineBorder(new Color(220, 228, 220))
        );
        utama.add(scroll, BorderLayout.CENTER);

        // Form input
        JPanel bagianBawah = new JPanel(new BorderLayout(10, 12));
        bagianBawah.setBackground(latar);

        JLabel judulForm = new JLabel("Form Data Barang");
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

        nama = new JTextField();
        kategori = new JTextField();
        harga = new JTextField();
        stok = new JTextField();

        form.add(buatKolom("Nama Barang", nama));
        form.add(buatKolom("Kategori", kategori));
        form.add(buatKolom("Harga (Rp)", harga));
        form.add(buatKolom("Stok", stok));

        // Tombol
        JPanel tombol = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        tombol.setBackground(latar);

        JButton tambah = buatTombol("Tambah", hijau);
        JButton ubah = buatTombol("Ubah", new Color(45, 105, 170));
        JButton hapus = buatTombol("Hapus", new Color(190, 60, 55));
        JButton bersihkan = buatTombol("Bersihkan", new Color(105, 110, 115));
        JButton kembali = buatTombol("Kembali", new Color(85, 90, 95));

        tombol.add(tambah);
        tombol.add(ubah);
        tombol.add(hapus);
        tombol.add(bersihkan);
        tombol.add(kembali);

        bagianBawah.add(judulForm, BorderLayout.NORTH);
        bagianBawah.add(form, BorderLayout.CENTER);
        bagianBawah.add(tombol, BorderLayout.SOUTH);

        utama.add(bagianBawah, BorderLayout.SOUTH);
        setContentPane(utama);

        muatData();

        tabel.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int baris = tabel.getSelectedRow();

                if (baris >= 0) {
                    nama.setText(model.getValueAt(baris, 1).toString());
                    kategori.setText(model.getValueAt(baris, 2).toString());
                    harga.setText(model.getValueAt(baris, 3).toString());
                    stok.setText(model.getValueAt(baris, 4).toString());
                }
            }
        });

        tambah.addActionListener(e -> tambahData());
        ubah.addActionListener(e -> ubahData());
        hapus.addActionListener(e -> hapusData());
        bersihkan.addActionListener(e -> bersihkanForm());
        kembali.addActionListener(e -> dispose());
    }

    private JPanel buatKolom(String label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(Color.WHITE);

        JLabel teks = new JLabel(label);
        teks.setFont(new Font("Arial", Font.BOLD, 12));
        teks.setForeground(new Color(65, 75, 65));

        field.setFont(new Font("Arial", Font.PLAIN, 13));

        panel.add(teks, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);

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

    private void muatData() {
        model.setRowCount(0);

        String sql = "SELECT * FROM barang ORDER BY id_barang";

        try (Connection con = Koneksi.getKoneksi();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_barang"),
                    rs.getString("nama_barang"),
                    rs.getString("kategori"),
                    rs.getBigDecimal("harga"),
                    rs.getInt("stok")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Gagal memuat data: " + e.getMessage());
        }
    }

    private boolean validasiForm() {
        if (nama.getText().trim().isEmpty()
                || kategori.getText().trim().isEmpty()
                || harga.getText().trim().isEmpty()
                || stok.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Semua kolom wajib diisi.");
            return false;
        }

        try {
            BigDecimal h = new BigDecimal(harga.getText().trim());
            int s = Integer.parseInt(stok.getText().trim());

            if (h.compareTo(BigDecimal.ZERO) < 0 || s < 0) {
                JOptionPane.showMessageDialog(this,
                    "Harga dan stok tidak boleh negatif.");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Harga harus berupa angka dan stok harus bilangan bulat.");
            return false;
        }

        return true;
    }

    private void tambahData() {
        if (!validasiForm()) return;

        String sql = "INSERT INTO barang "
            + "(nama_barang, kategori, harga, stok) VALUES (?, ?, ?, ?)";

        try (Connection con = Koneksi.getKoneksi();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nama.getText().trim());
            ps.setString(2, kategori.getText().trim());
            ps.setBigDecimal(3, new BigDecimal(harga.getText().trim()));
            ps.setInt(4, Integer.parseInt(stok.getText().trim()));

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this,
                "Barang berhasil ditambahkan.");

            bersihkanForm();
            muatData();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Gagal menambahkan barang: " + e.getMessage());
        }
    }

    private void ubahData() {
        int baris = tabel.getSelectedRow();

        if (baris < 0) {
            JOptionPane.showMessageDialog(this,
                "Pilih barang yang ingin diubah.");
            return;
        }

        if (!validasiForm()) return;

        int id = Integer.parseInt(model.getValueAt(baris, 0).toString());

        String sql = "UPDATE barang SET nama_barang=?, kategori=?, "
            + "harga=?, stok=? WHERE id_barang=?";

        try (Connection con = Koneksi.getKoneksi();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nama.getText().trim());
            ps.setString(2, kategori.getText().trim());
            ps.setBigDecimal(3, new BigDecimal(harga.getText().trim()));
            ps.setInt(4, Integer.parseInt(stok.getText().trim()));
            ps.setInt(5, id);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this,
                "Data barang berhasil diubah.");

            bersihkanForm();
            muatData();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Gagal mengubah barang: " + e.getMessage());
        }
    }

    private void hapusData() {
        int baris = tabel.getSelectedRow();

        if (baris < 0) {
            JOptionPane.showMessageDialog(this,
                "Pilih barang yang ingin dihapus.");
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(this,
            "Yakin ingin menghapus barang ini?",
            "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (konfirmasi != JOptionPane.YES_OPTION) return;

        int id = Integer.parseInt(model.getValueAt(baris, 0).toString());

        try (Connection con = Koneksi.getKoneksi();
             PreparedStatement ps = con.prepareStatement(
                 "DELETE FROM barang WHERE id_barang=?")) {

            ps.setInt(1, id);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
                "Barang berhasil dihapus.");

            bersihkanForm();
            muatData();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Gagal menghapus barang: " + e.getMessage());
        }
    }

    private void bersihkanForm() {
        nama.setText("");
        kategori.setText("");
        harga.setText("");
        stok.setText("");
        tabel.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new DataBarang().setVisible(true)
        );
    }
}