/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Process_Data;

import ConnectionData.CONNECTIONSQLSERVER;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author Mr.Khoa
 */
public class Customer {

    CONNECTIONSQLSERVER con;

    public Customer() {
        con = new CONNECTIONSQLSERVER();
    }

    public String PhieuMuon(int makh) {
        String SoPhieuMuon = "0";
        try {
            ResultSet rs = con.GetResultSetStoredProcedurce("SoPhieuMuon", new Object[]{makh});
            if (rs.next()) {
                SoPhieuMuon = rs.getString("sophieumuon");
                return SoPhieuMuon;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SoPhieuMuon;
    }

    public String PhieuTra(int makh) {
        String SoPhieuTra = "0";
        try {
            ResultSet rs = con.GetResultSetStoredProcedurce("LichSuMuon", new Object[]{makh});
            if (rs.next()) {
                SoPhieuTra = rs.getString("sophieutra");
                return SoPhieuTra;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SoPhieuTra;
    }

    public String PhieuPhat(int makh) {
        String SoPhieuPhat = "0";
        try {
            ResultSet rs = con.GetResultSetStoredProcedurce("SoPhieuPhat", new Object[]{makh});
            if (rs.next()) {
                SoPhieuPhat = rs.getString("sophieuphat");
                return SoPhieuPhat;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SoPhieuPhat;
    }

    public Vector<model.Sach> DanhSachSach() {
        Vector<model.Sach> vt = new Vector<>();
        try {
            String SQL = "SELECT * FROM SACH";
            ResultSet rs = con.GetResultSetSQL(SQL);
            while (rs.next()) {
                model.Sach sach = new model.Sach();
                sach.setMaSach(rs.getInt("MaSach"));
                sach.setTenSach(rs.getString("TenSach"));
                sach.setTacGia(rs.getString("TacGia"));
                sach.setTheLoai(rs.getString("TheLoai"));
                sach.setNhaXuatBan(rs.getString("NhaXuatBan"));
                sach.setGiaSach(rs.getString("GiaSach"));
                sach.setSoLuong(rs.getString("SoLuong"));
                vt.add(sach);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vt;
    }

    public Vector<model.ChiTietMuon> DachSachMuon(int madg) {
        Vector<model.ChiTietMuon> vt = new Vector<>();
        try {
            ResultSet rs = con.GetResultSetStoredProcedurce("SachMuon", new Object[]{madg});
            while (rs.next()) {
                model.ChiTietMuon chitietmuon = new model.ChiTietMuon();
                chitietmuon.setTenSach(rs.getString("TenSach"));
                chitietmuon.setNgayTra(rs.getString("NgayTra"));
                chitietmuon.setNgayMuon(rs.getString("NgayMuon"));
                chitietmuon.setTrangThai(rs.getString("TrangThai"));
                chitietmuon.setMaPhieu(rs.getInt("MaPhieu"));
                vt.add(chitietmuon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vt;
    }

    public Vector<model.ChiTietTra> DachSachTra(int madg) {
        Vector<model.ChiTietTra> vt = new Vector<>();
        try {
            ResultSet rs = con.GetResultSetStoredProcedurce("SachTra", new Object[]{madg});
            while (rs.next()) {
                model.ChiTietTra chitiettra = new model.ChiTietTra();
                chitiettra.setTenSach(rs.getString("TenSach"));
                chitiettra.setHanTra(rs.getDate("NgayPhaiTra"));
                chitiettra.setTrangThai(rs.getString("TrangThai"));
                vt.add(chitiettra);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vt;
    }

    public Vector<model.PhieuPhat> DanhSachPhieuPhat(int madg) {
        Vector<model.PhieuPhat> vt = new Vector<>();
        try {
            String SQL = "SELECT PP.MaPhieuPhat, PP.MaPhieu, PP.MaDocGia, DG.HoTen, PP.LyDo, PP.SoTien, PP.NgayLap, PP.TrangThai "
                    + "FROM PHIEUPHAT PP JOIN DOCGIA DG ON PP.MaDocGia = DG.MaDocGia "
                    + "WHERE PP.MaDocGia = " + madg + " ORDER BY PP.MaPhieuPhat DESC";
            ResultSet rs = con.GetResultSetSQL(SQL);
            while (rs.next()) {
                model.PhieuPhat p = new model.PhieuPhat();
                p.setMaPhieuPhat(rs.getInt("MaPhieuPhat"));
                p.setMaPhieu(rs.getInt("MaPhieu"));
                p.setMaDocGia(rs.getInt("MaDocGia"));
                p.setTenDocGia(rs.getString("HoTen"));
                p.setLyDo(rs.getString("LyDo"));
                p.setSoTien(rs.getDouble("SoTien"));
                p.setNgayLap(rs.getDate("NgayLap"));
                p.setTrangThai(rs.getInt("TrangThai"));
                vt.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vt;
    }

    public int SuaAccount(int madg, String hoten, String gioitinh, String diachi, Date ngaysinh) {
        int k;
        k = con.ExecuteStoredProcedures("CapNhapAcount", new Object[]{madg, hoten, gioitinh, diachi, ngaysinh});
        return k;
    }

    public model.DocGia ThongTinDG(int madg) {
        model.DocGia thongtindg = new model.DocGia();
        try {
            ResultSet rs = con.GetResultSetStoredProcedurce("ThongTinDG", new Object[]{madg});
            while (rs.next()) {
                thongtindg.setHoTen(rs.getString("HoTen"));
                thongtindg.setGioiTinh(rs.getString("GioiTinh"));
                thongtindg.setDiaChi(rs.getString("DiaChi"));
                thongtindg.setNgaySinh(rs.getDate("NgaySinh"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thongtindg;
    }

    public int SuaMatKhau(int manv, String matkhaucu, String matkhaumoi) {
        int k;
        String sql = "UPDATE ACCOUNT SET MatKhau = '" + matkhaumoi + "' WHERE MaDocGia = " + manv + "AND MatKhau = '" + matkhaucu + "'";
        k = con.ExecuteUpdateSQL(sql);
        return k;

    }

    public int themDocGia(String TenDangNhap, String HoTen, String GioiTinh, String NgaySinh, String DiaChi) {
        int k;
        String sql = "INSERT INTO DOCGIA(TenDangNhap, HoTen, GioiTinh, NgaySinh, DiaChi) "
                + "VALUES ('" + TenDangNhap + "', N'" + HoTen + "', N'" + GioiTinh + "', '"
                + NgaySinh + "', N'" + DiaChi + "')";

        k = con.ExecuteUpdateSQL(sql);
        return k;
    }

    public int updateDocGia(int MaDocGia, String TenDangNhap, String HoTen, String GioiTinh, String NgaySinh, String DiaChi) {
        int k;
        String sql = "UPDATE DOCGIA SET "
                + "TenDangNhap = '" + TenDangNhap + "', "
                + "HoTen = N'" + HoTen + "', "
                + "GioiTinh = N'" + GioiTinh + "', "
                + "NgaySinh = '" + NgaySinh + "', "
                + "DiaChi = N'" + DiaChi + "' "
                + "WHERE MaDocGia = " + MaDocGia;

        k = con.ExecuteUpdateSQL(sql);
        return k;
    }

    public int deleteDocGia(int maDocGia) {
        int k;
        String sql = "DELETE FROM DOCGIA WHERE MaDocGia = " + maDocGia;

        k = con.ExecuteUpdateSQL(sql);
        return k;
    }
    public Vector<model.DocGia> searchDocGia(String keyword) {
    Vector<model.DocGia> vt = new Vector<>();
    try {
        // Tìm kiếm theo HoTen hoặc MaDocGia
        String sql = "SELECT MaDocGia, TenDangNhap, HoTen, GioiTinh, NgaySinh, DiaChi "
                   + "FROM DOCGIA WHERE HoTen LIKE N'%" + keyword + "%' OR MaDocGia LIKE '%" + keyword + "%'";
        ResultSet rs = con.GetResultSetSQL(sql);
        while (rs.next()) {
            model.DocGia dg = new model.DocGia();
            dg.setMaDocGia(rs.getInt("MaDocGia"));
            dg.setTenDangNhap(rs.getString("TenDangNhap"));
            dg.setHoTen(rs.getString("HoTen"));
            dg.setGioiTinh(rs.getString("GioiTinh"));
            dg.setNgaySinh(rs.getDate("NgaySinh")); // Trả về đối tượng Date
            dg.setDiaChi(rs.getString("DiaChi"));
            vt.add(dg);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return vt;
}
}
