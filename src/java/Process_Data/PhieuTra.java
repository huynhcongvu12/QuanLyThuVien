/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Process_Data;

import ConnectionData.CONNECTIONSQLSERVER;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author Mr.Khoa
 */
public class PhieuTra {
    CONNECTIONSQLSERVER con;

    public PhieuTra() {
        con = new CONNECTIONSQLSERVER();
    }
    public String kiemdocgiavamasach(int madocgia, int masach) {
        String madg = null;
        String mas = null;
        try {
            ResultSet rs = con.GetResultSetStoredProcedurce("kiemtramasachPM", new Object[]{madocgia, masach});
            if (rs != null && rs.next()) {
                madg = rs.getString("MaDocGia");
                mas = rs.getString("MaSach");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return madg;
    }
    public int themPhieuTra(int maDocGia, int maSach, String ngayTra) {
        try {
            con.Open();
            Connection cnn = con.cnn;
            cnn.setAutoCommit(false);
            int maPhieuMuon;

            try (PreparedStatement find = cnn.prepareStatement(
                    "SELECT TOP 1 MaPhieu FROM PHIEUMUON "
                    + "WHERE MaDocGia = ? AND MaSach = ? AND TrangThai IN (N'Borrowed', N'Overdue') "
                    + "ORDER BY MaPhieu DESC")) {
                find.setInt(1, maDocGia);
                find.setInt(2, maSach);
                ResultSet rs = find.executeQuery();
                if (!rs.next()) {
                    cnn.rollback();
                    return 0;
                }
                maPhieuMuon = rs.getInt("MaPhieu");
            }

            try (PreparedStatement insert = cnn.prepareStatement(
                    "INSERT INTO PHIEUTRA(MaDocGia, MaSach, NgayTra) VALUES (?, ?, ?)")) {
                insert.setInt(1, maDocGia);
                insert.setInt(2, maSach);
                insert.setString(3, ngayTra);
                if (insert.executeUpdate() == 0) {
                    cnn.rollback();
                    return 0;
                }
            }

            try (PreparedStatement updateMuon = cnn.prepareStatement(
                    "UPDATE PHIEUMUON SET TrangThai=N'Returned', ReturnDate=?, FineAmount=ISNULL(FineAmount, 0) "
                    + "WHERE MaPhieu=? AND TrangThai IN (N'Borrowed', N'Overdue')")) {
                updateMuon.setString(1, ngayTra);
                updateMuon.setInt(2, maPhieuMuon);
                if (updateMuon.executeUpdate() == 0) {
                    cnn.rollback();
                    return 0;
                }
            }

            try (PreparedStatement restoreBook = cnn.prepareStatement(
                    "UPDATE SACH SET SoLuong = SoLuong + 1 WHERE MaSach = ?")) {
                restoreBook.setInt(1, maSach);
                restoreBook.executeUpdate();
            }

            cnn.commit();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (con.cnn != null) {
                    con.cnn.rollback();
                }
            } catch (Exception ignored) {
            }
        } finally {
            try {
                if (con.cnn != null) {
                    con.cnn.setAutoCommit(true);
                }
            } catch (Exception ignored) {
            }
            con.Close();
        }
        return 0;
    }
    
    public int updatePhieuTra(int maPhieuTra, int maDocGia, int maSach, String ngayTra) {
        int k;
        String sql = "UPDATE PHIEUTRA SET "
            + "MaDocGia = " + maDocGia + ", "
            + "MaSach = " + maSach + ", "
            + "NgayTra = '" + ngayTra + "' " 
            + "WHERE MaPhieu = " + maPhieuTra;
        k = con.ExecuteUpdateSQL(sql);
        return k;
    }
    
    public int deletePhieuTra(int maPhieuTra) {
        int k;
        String sql = "DELETE FROM PHIEUTRA WHERE MaPhieu = " + maPhieuTra;
        k = con.ExecuteUpdateSQL(sql);
        return k;
    }
    
    public Vector<model.PhieuTra> searchPhieuTra(String keyword) {
        Vector<model.PhieuTra> vt = new Vector<>();
        try {
           
            String SQL = "SELECT PT.MaPhieu, DG.MaDocGia, DG.HoTen, S.MaSach, S.TenSach, PT.NgayTra "
                    + "FROM PHIEUTRA PT "
                    + "JOIN DOCGIA DG ON PT.MaDocGia = DG.MaDocGia "
                    + "JOIN SACH S ON PT.MaSach = S.MaSach "
                    + "WHERE DG.HoTen LIKE N'%" + keyword + "%' "
                    + "OR S.TenSach LIKE N'%" + keyword + "%' "
                    + "OR CAST(PT.MaPhieu AS VARCHAR) LIKE '%" + keyword + "%' "
                    + "ORDER BY PT.MaPhieu DESC";

            ResultSet rs = con.GetResultSetSQL(SQL);
            while (rs.next()) {
                model.PhieuTra pt = new model.PhieuTra();
                pt.setMaPhieu(rs.getInt("MaPhieu"));
                pt.setMaDocGia(rs.getInt("MaDocGia"));
                pt.setHoTen(rs.getString("HoTen"));
                pt.setMaSach(rs.getInt("MaSach"));
                pt.setTenSach(rs.getString("TenSach"));
                pt.setNgayTra(rs.getDate("NgayTra"));
                vt.add(pt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vt;
    }
}
