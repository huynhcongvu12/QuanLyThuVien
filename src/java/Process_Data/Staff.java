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
public class Staff {

    CONNECTIONSQLSERVER con;

    public Staff() {
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

    public model.DocGia ThongTinDG(int madg) {
        model.DocGia thongtindg = new model.DocGia();
        try {
            ResultSet rs = con.GetResultSetStoredProcedurce("ThongTinNV", new Object[]{madg});
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

    public String PhieuMuonHomNay() {
        String phieumuonhomnay = "0";
        try {
            ResultSet rs = con.GetResultSetStoredProcedurce("PhieuMuonHomNay", null);
            if (rs.next()) {
                phieumuonhomnay = rs.getString("phieumuonhomnay");
                return phieumuonhomnay;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return phieumuonhomnay;
    }

    public String PhieuTraHomNay() {
        String phieutrahomnay = "0";
        try {
            ResultSet rs = con.GetResultSetStoredProcedurce("PhieuTraHomNay", null);
            if (rs.next()) {
                phieutrahomnay = rs.getString("phieutrahomnay");
                return phieutrahomnay;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return phieutrahomnay;
    }

    public String TongSoLuongSach() {
        String soluong = "0";
        try {
            ResultSet rs = con.GetResultSetStoredProcedurce("TongSoLuongSach", null);
            if (rs.next()) {
                soluong = rs.getString("SoLuong");
                return soluong;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return soluong;
    }
    public String DemSoPhieuQuaHan() {
        String soluong = "0";
        try {
            ResultSet rs = con.GetResultSetStoredProcedurce("DemPhieuQuaHan", null);
            if (rs.next()) {
                soluong = rs.getString("SoPhieuQuaHan");
                return soluong;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return soluong;
    }
    
    public String SoSanhPhieuMuon() {
        String soluong = "0";
        try {
            ResultSet rs = con.GetResultSetStoredProcedurce("SoSanhPhieuMuon", null);
            if (rs.next()) {
                soluong = rs.getString("chenhlech");
                return soluong;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return soluong;
    }

    public Vector<model.PhieuMuon> DanhSachMuon() {
        Vector<model.PhieuMuon> vt = new Vector<>();
        try {
            String SQL = "SELECT * FROM PHIEUMUON";
            ResultSet rs = con.GetResultSetSQL(SQL);
            while (rs.next()) {
                model.PhieuMuon phieumuon = new model.PhieuMuon();
                phieumuon.setMaPhieu(rs.getInt("MaPhieu"));
                phieumuon.setMaDocGia(rs.getInt("MaDocGia"));
                phieumuon.setMaSach(rs.getInt("MaSach"));
                phieumuon.setNgayMuon(rs.getDate("NgayMuon"));
                phieumuon.setNgayPhaiTra(rs.getDate("NgayPhaiTra"));
                vt.add(phieumuon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vt;
    }

    public Vector<model.PhieuTra> DanhSachTra() {
        Vector<model.PhieuTra> vt = new Vector<>();
        try {
            String SQL = "SELECT * FROM PHIEUTRA";
            ResultSet rs = con.GetResultSetSQL(SQL);
            while (rs.next()) {
                model.PhieuTra phieutra = new model.PhieuTra();
                phieutra.setMaPhieu(rs.getInt("MaPhieu"));
                phieutra.setMaDocGia(rs.getInt("MaDocGia"));
                phieutra.setMaSach(rs.getInt("MaSach"));
                phieutra.setNgayTra(rs.getDate("NgayTra"));
                vt.add(phieutra);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vt;
    }

    public int themSach(String TenSach, String TacGia, String TheLoai, String NhaXuatBan, Double GiaSach, int SoLuong) {
        int k;
        String tinhTrang = "Còn";
        try {
            if (SoLuong <= 0) {
                tinhTrang = "Hết";
            }
        } catch (Exception e) {
            tinhTrang = "Hết";
        }
        String sql = "INSERT INTO SACH(TenSach, TacGia, TheLoai, NhaXuatBan, GiaSach, SoLuong, TinhTrang) "
                + "VALUES (N'" + TenSach + "', N'" + TacGia + "', N'" + TheLoai + "', N'" + NhaXuatBan + "', "
                + GiaSach + ", " + SoLuong + ", N'" + tinhTrang + "')";
        k = con.ExecuteUpdateSQL(sql);
        return k;
    }

    public int updateSach(int MaSach, String TenSach, String TacGia, String TheLoai, String NhaXuatBan, Double GiaSach, int SoLuong) {
        int k;
        String tinhTrang = SoLuong > 0 ? "Còn" : "Hết";
        String sql = "UPDATE SACH SET "
                + "TenSach = N'" + TenSach + "', "
                + "TacGia = N'" + TacGia + "', "
                + "TheLoai = N'" + TheLoai + "', "
                + "NhaXuatBan = N'" + NhaXuatBan + "', "
                + "GiaSach = " + GiaSach + ", "
                + "SoLuong = " + SoLuong + ", "
                + "TinhTrang = N'" + tinhTrang + "' "
                + "WHERE MaSach = " + MaSach;

        k = con.ExecuteUpdateSQL(sql);
        return k;
    }

    public int deleteSach(int maSach) {
        int k;
        String sql = "DELETE FROM SACH WHERE MaSach = " + maSach;

        k = con.ExecuteUpdateSQL(sql);
        return k;
    }

    public int themNhanVien(String TenDangNhap, String HoTen, String GioiTinh, String NgaySinh, String DiaChi, String ChucVu) {
        int k;
        String sql = "INSERT INTO NHANVIEN(TenDangNhap, HoTen, GioiTinh, NgaySinh, DiaChi, ChucVu) "
                + "VALUES ('" + TenDangNhap + "', N'" + HoTen + "', N'" + GioiTinh + "', '"
                + NgaySinh + "', N'" + DiaChi + "', N'" + ChucVu + "')";

        k = con.ExecuteUpdateSQL(sql);
        return k;
    }

    public int updateNhanVien(int MaNV, String TenDangNhap, String HoTen, String GioiTinh, String NgaySinh, String DiaChi, String ChucVu) {
        int k;
        String sql = "UPDATE NHANVIEN SET "
                + "TenDangNhap = '" + TenDangNhap + "', "
                + "HoTen = N'" + HoTen + "', "
                + "GioiTinh = N'" + GioiTinh + "', "
                + "NgaySinh = '" + NgaySinh + "', "
                + "DiaChi = N'" + DiaChi + "', "
                + "ChucVu = N'" + ChucVu + "' "
                + "WHERE MaNV = " + MaNV;

        k = con.ExecuteUpdateSQL(sql);
        return k;
    }

    public int deleteNhanVien(int maNV) {
        int k;
        String sql = "DELETE FROM NHANVIEN WHERE MaNV = " + maNV;

        k = con.ExecuteUpdateSQL(sql);
        return k;
    }

    public Vector<model.NhanVien> searchNhanVien(String keyword) {
        Vector<model.NhanVien> vt = new Vector<>();
        try {
            String sql = "SELECT MaNV, TenDangNhap, HoTen, GioiTinh, NgaySinh, DiaChi, ChucVu "
                    + "FROM NHANVIEN WHERE HoTen LIKE N'%" + keyword + "%' OR MaNV LIKE '%" + keyword + "%'";

            ResultSet rs = con.GetResultSetSQL(sql);
            while (rs.next()) {
                model.NhanVien nv = new model.NhanVien();
                nv.setMaNhanVien(rs.getInt("MaNV"));
                nv.setTenDangNhap(rs.getString("TenDangNhap"));
                nv.setHoTen(rs.getString("HoTen"));
                nv.setGioiTinh(rs.getString("GioiTinh"));
                nv.setNgaySinh(rs.getDate("NgaySinh"));
                nv.setDiaChi(rs.getString("DiaChi"));
                vt.add(nv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vt;
    }
}
