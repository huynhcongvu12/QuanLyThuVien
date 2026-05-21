/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Process_Data;

import ConnectionData.CONNECTIONSQLSERVER;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author Mr.Khoa
 */
public class PhieuPhat {
    CONNECTIONSQLSERVER con;

    public PhieuPhat() {
        con = new CONNECTIONSQLSERVER();
    }

    // 1. Thêm mới Phiếu phạt
    public int themPhieuPhat(int maPhieu, int maDocGia, String lyDo, float soTien, String ngayLap, int trangThai) {
        int k;
        String sql = "INSERT INTO PHIEUPHAT(MaPhieu, MaDocGia, LyDo, SoTien, NgayLap, TrangThai) "
                + "VALUES (" + maPhieu + ", " + maDocGia + ", N'" + lyDo + "', " + soTien + ", '" + ngayLap + "', " + trangThai + ")";
        k = con.ExecuteUpdateSQL(sql);
        return k;
    }

    // 2. Cập nhật Phiếu phạt
    public int updatePhieuPhat(int maPhieuPhat, int maPhieu, int maDocGia, String lyDo, float soTien, String ngayLap, int trangThai) {
        int k;
        String sql = "UPDATE PHIEUPHAT SET "
                + "MaPhieu = " + maPhieu + ", "
                + "MaDocGia = " + maDocGia + ", "
                + "LyDo = N'" + lyDo + "', "
                + "SoTien = " + soTien + ", "
                + "NgayLap = '" + ngayLap + "', "
                + "TrangThai = " + trangThai + " "
                + "WHERE MaPhieuPhat = " + maPhieuPhat;
        k = con.ExecuteUpdateSQL(sql);
        return k;
    }

    // 3. Xóa Phiếu phạt
    public int deletePhieuPhat(int maPhieuPhat) {
        int k;
        String sql = "DELETE FROM PHIEUPHAT WHERE MaPhieuPhat = " + maPhieuPhat;
        k = con.ExecuteUpdateSQL(sql);
        return k;
    }

    // 4. Tìm kiếm Phiếu phạt (Có lấy thêm Họ tên từ bảng DOCGIA)
    public Vector<model.PhieuPhat> searchPhieuPhat(String keyword) {
        Vector<model.PhieuPhat> vt = new Vector<>();
        try {
            // Sử dụng JOIN để lấy HoTen từ bảng DOCGIA
            String SQL = "SELECT PP.MaPhieuPhat, PP.MaPhieu, PP.MaDocGia, DG.HoTen, PP.LyDo, PP.SoTien, PP.NgayLap, PP.TrangThai "
                    + "FROM PHIEUPHAT PP "
                    + "JOIN DOCGIA DG ON PP.MaDocGia = DG.MaDocGia "
                    + "WHERE DG.HoTen LIKE N'%" + keyword + "%' " // Tìm theo tên độc giả
                    + "OR PP.LyDo LIKE N'%" + keyword + "%' "     // Hoặc tìm theo lý do phạt
                    + "OR CAST(PP.MaPhieuPhat AS VARCHAR) LIKE '%" + keyword + "%' " // Hoặc tìm theo mã phiếu phạt
                    + "ORDER BY PP.MaPhieuPhat DESC";
            ResultSet rs = con.GetResultSetSQL(SQL);
            while (rs.next()) {
                model.PhieuPhat p = new model.PhieuPhat();
                p.setMaPhieuPhat(rs.getInt("MaPhieuPhat"));
                p.setMaPhieu(rs.getInt("MaPhieu"));
                p.setMaDocGia(rs.getInt("MaDocGia"));
                p.setTenDocGia(rs.getString("HoTen")); // Đây là cột lấy từ bảng DOCGIA
                p.setLyDo(rs.getString("LyDo"));
                p.setSoTien(rs.getFloat("SoTien"));
                p.setNgayLap(rs.getDate("NgayLap"));
                p.setTrangThai(rs.getInt("TrangThai"));
                vt.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vt;
    }
}
