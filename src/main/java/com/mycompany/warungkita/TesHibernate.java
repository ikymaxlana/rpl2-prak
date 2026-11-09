
package com.mycompany.warungkita;

import org.hibernate.Session;

public class TesHibernate {

    public static void main(String[] args) {
        try (Session session = HibernateUtil
                .getSessionFactory().openSession()) {

            session.doWork(connection -> {
                System.out.println(
                    "Hibernate berhasil terhubung ke database: "
                    + connection.getCatalog()
                );
            });

        } catch (Exception e) {
            System.out.println("Koneksi Hibernate gagal!");
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
