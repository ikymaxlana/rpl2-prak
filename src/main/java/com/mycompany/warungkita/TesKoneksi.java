package com.mycompany.warungkita;

import java.sql.Connection;

public class TesKoneksi {

    public static void main(String[] args) {
        Connection koneksi = Koneksi.getKoneksi();

        if (koneksi != null) {
            System.out.println("Koneksi database berhasil!");

            try {
                koneksi.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Koneksi database gagal!");
        }
    }
}