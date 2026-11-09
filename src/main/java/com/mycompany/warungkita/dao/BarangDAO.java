package com.mycompany.warungkita.dao;

import com.mycompany.warungkita.model.Barang;
import java.util.List;

public interface BarangDAO {
    void tambah(Barang barang);
    void ubah(Barang barang);
    void hapus(int id);
    Barang cariById(int id);
    List<Barang> semuaBarang();
}