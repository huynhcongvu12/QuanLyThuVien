/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Process_Data;

import ConnectionData.CONNECTIONSQLSERVER;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import model.PhieuMuon;
import model.PhieuPhat;
import model.PhieuTra;

/**
 *
 * @author Mr.Khoa
 */
public class Admin {

    CONNECTIONSQLSERVER con;

    public Admin() {
        con = new CONNECTIONSQLSERVER();
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

    public int SuaAccount(int madg, String hoten, String gioitinh, String diachi, Date ngaysinh) {
        int k;
        k = con.ExecuteStoredProcedures("CapNhapNV", new Object[]{madg, hoten, gioitinh, diachi, ngaysinh});
        return k;
    }

    public int SuaMatKhau(int manv, String matkhaucu, String matkhaumoi) {
        int k;
        String sql = "UPDATE ACCOUNT SET MatKhau = '" + matkhaumoi + "' WHERE MaNV = " + manv + "AND MatKhau = '" + matkhaucu + "'";
        k = con.ExecuteUpdateSQL(sql);
        return k;
    }

    public Vector<model.DocGia> DanhSachDG() {
        Vector<model.DocGia> vt = new Vector<>();
        try {
            String SQL = "SELECT * FROM DOCGIA";
            ResultSet rs = con.GetResultSetSQL(SQL);
            while (rs.next()) {
                model.DocGia docgia = new model.DocGia();
                docgia.setMaDocGia(rs.getInt("MaDocGia"));
                docgia.setTenDangNhap(rs.getString("TenDangNhap"));
                docgia.setHoTen(rs.getString("HoTen"));
                docgia.setGioiTinh(rs.getString("GioiTinh"));
                docgia.setDiaChi(rs.getString("DiaChi"));
                docgia.setNgaySinh(rs.getDate("NgaySinh"));
                vt.add(docgia);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vt;
    }

    public Vector<model.NhanVien> DanhSachNV() {
        Vector<model.NhanVien> vt = new Vector<>();
        try {
            String SQL = "SELECT * FROM NHANVIEN";
            ResultSet rs = con.GetResultSetSQL(SQL);
            while (rs.next()) {
                model.NhanVien nhanvien = new model.NhanVien();
                nhanvien.setMaNhanVien(rs.getInt("MaNV"));
                nhanvien.setTenDangNhap(rs.getString("TenDangNhap"));
                nhanvien.setHoTen(rs.getString("HoTen"));
                nhanvien.setGioiTinh(rs.getString("GioiTinh"));
                nhanvien.setDiaChi(rs.getString("DiaChi"));
                nhanvien.setChucVu(rs.getString("ChucVu"));
                nhanvien.setNgaySinh(rs.getDate("NgaySinh"));
                vt.add(nhanvien);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vt;
    }

    public Vector<model.PhieuMuon> layTatCaPhieuMuon() {
        Vector<model.PhieuMuon> vt = new Vector<model.PhieuMuon>();
        try {
            new Process_Data.PhieuMuon().capNhatQuaHanTuDong();
            String SQL = """
            SELECT 
                PM.MaPhieu,
                DG.MaDocGia, 
                DG.HoTen,
                S.MaSach,
                S.TenSach,
                PM.NgayMuon,
                PM.NgayPhaiTra,
                PM.TrangThai,
                PM.LyDoTuChoi
            FROM PHIEUMUON PM
            JOIN DOCGIA DG ON PM.MaDocGia = DG.MaDocGia
            JOIN SACH S ON PM.MaSach = S.MaSach
            ORDER BY PM.MaPhieu DESC
        """;
            ResultSet rs = con.GetResultSetSQL(SQL);
            while (rs.next()) {
                PhieuMuon pm = new PhieuMuon();
                pm.setMaPhieu(rs.getInt("MaPhieu"));
                pm.setMaDocGia(rs.getInt("MaDocGia"));
                pm.setHoTen(rs.getString("HoTen"));
                pm.setMaSach(rs.getInt("MaSach"));
                pm.setTenSach(rs.getString("TenSach"));
                pm.setNgayMuon(rs.getDate("NgayMuon"));
                pm.setNgayPhaiTra(rs.getDate("NgayPhaiTra"));
                pm.setTrangThai(rs.getString("TrangThai"));
                pm.setLyDoTuChoi(rs.getString("LyDoTuChoi"));
                vt.add(pm);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return vt;
    }

    public Vector<model.PhieuTra> getAllPhieuTra() {
        Vector<model.PhieuTra> vt = new Vector<model.PhieuTra>();
        try {
            String SQL = "SELECT PT.MaPhieu, DG.MaDocGia, DG.HoTen, S.MaSach, S.TenSach, PT.NgayTra "
                    + "FROM PHIEUTRA PT "
                    + "JOIN DOCGIA DG ON PT.MaDocGia = DG.MaDocGia "
                    + "JOIN SACH S ON PT.MaSach = S.MaSach "
                    + "ORDER BY PT.MaPhieu DESC";


            ResultSet rs = con.GetResultSetSQL(SQL);
            while (rs.next()) {
                PhieuTra pt = new PhieuTra();
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
    public Vector<model.PhieuPhat> getAllphieutphat() {
        Vector<model.PhieuPhat> vt = new Vector<model.PhieuPhat>();
        try {
            String SQL = "SELECT PP.MaPhieuPhat, PP.MaPhieu, PP.MaDocGia, DG.HoTen, PP.LyDo, PP.SoTien, PP.NgayLap, PP.TrangThai "
                    + "FROM PHIEUPHAT PP "
                    + "JOIN DOCGIA DG ON PP.MaDocGia = DG.MaDocGia "
                    + "ORDER BY PP.MaPhieuPhat DESC";

            ResultSet rs = con.GetResultSetSQL(SQL);
            while (rs.next()) {
                PhieuPhat p = new PhieuPhat();
                p.setMaPhieuPhat(rs.getInt("MaPhieuPhat"));
                p.setMaPhieu(rs.getInt("MaPhieu"));        // <-- thêm đây
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
     public String LuotMuonThangHienTai() {
        String soluong = "0";
        try {
            ResultSet rs = con.GetResultSetStoredProcedurce("LuotMuonThangHienTai", null);
            if (rs.next()) {
                soluong = rs.getString("SoLuong");
                return soluong;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return soluong;
    }
      public String LuotTraThangHienTai() {
        String soluong = "0";
        try {
            ResultSet rs = con.GetResultSetStoredProcedurce("LuotTraThangHienTai", null);
            if (rs.next()) {
                soluong = rs.getString("TongLuotTra");
                return soluong;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return soluong;
    }
       public String DemTongPhieuPhatThang() {
        String soluong = "0";
        try {
            ResultSet rs = con.GetResultSetStoredProcedurce("DemTongPhieuPhatThang", null);
            if (rs.next()) {
                soluong = rs.getString("TongSoPhieuPhat");
                return soluong;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return soluong;
    }
        public String TinhTongTienPhatThang() {
        String soluong = "0";
        try {
            ResultSet rs = con.GetResultSetStoredProcedurce("TinhTongTienPhatThang", null);
            if (rs.next()) {
                soluong = rs.getString("TongTienPhatThangNay");
                return soluong;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return soluong;
    }
         public String DemPhieuPhatChuaTra() {
        String soluong = "0";
        try {
            ResultSet rs = con.GetResultSetStoredProcedurce("DemPhieuPhatChuaTra", null);
            if (rs.next()) {
                soluong = rs.getString("SoPhieuPhatChuaThanhToan");
                return soluong;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return soluong;
    }
    public Vector<String> PhanTramTangTruong() {
        Vector<String> vt = new Vector<String>();
        try {
            ResultSet rs = con.GetResultSetStoredProcedurce("sp_ThongKeTangTruongMuonSachTuDong", null);
            while (rs.next()) {
                String pt = rs.getString("PhanTramTangTruong");
                vt.add(pt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vt;
    }   
}
