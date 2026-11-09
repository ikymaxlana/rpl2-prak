package com.mycompany.warungkita.dao;

import com.mycompany.warungkita.HibernateUtil;
import com.mycompany.warungkita.model.Barang;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class BarangDAOImpl implements BarangDAO {

    @Override
    public void tambah(Barang barang) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(barang);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Gagal menambah barang", e);
        }
    }

    @Override
    public void ubah(Barang barang) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(barang);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Gagal mengubah barang", e);
        }
    }

    @Override
    public void hapus(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Barang b = session.get(Barang.class, id);
            if (b != null) session.remove(b);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Gagal menghapus barang", e);
        }
    }

    @Override
    public Barang cariById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Barang.class, id);
        }
    }

    @Override
    public List<Barang> semuaBarang() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM Barang ORDER BY idBarang", Barang.class).list();
        }
    }
}