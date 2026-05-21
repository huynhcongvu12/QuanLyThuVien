/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Process_Data;

import ConnectionData.CONNECTIONSQLSERVER;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author Mr.Khoa
 */
public class Sach {

    CONNECTIONSQLSERVER con;

    public Sach() {
        con = new CONNECTIONSQLSERVER();
    }

    public Vector<model.Sach> searchSach(String keyword) {
        Vector<model.Sach> vt = new Vector<>();
        String sql = "SELECT MaSach, TenSach, TacGia, TheLoai, NhaXuatBan, GiaSach, SoLuong "
                + "FROM SACH WHERE TenSach LIKE ? OR TacGia LIKE ?";
        try {
            con.Open();
            if (con.cnn == null) {
                throw new RuntimeException(con.databaseErrorMessage());
            }
            PreparedStatement ps = con.cnn.prepareStatement(sql);
            String like = "%" + (keyword == null ? "" : keyword.trim()) + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ResultSet rs = ps.executeQuery();
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
            throw new RuntimeException("Loi tim kiem sach. Kiem tra database va tu khoa tim kiem.", e);
        } finally {
            con.Close();
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
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            con.Open();
            PreparedStatement ps = con.cnn.prepareStatement(sql);
            ps.setString(1, TenSach);
            ps.setString(2, TacGia);
            ps.setString(3, TheLoai);
            ps.setString(4, NhaXuatBan);
            ps.setDouble(5, GiaSach);
            ps.setInt(6, SoLuong);
            ps.setString(7, tinhTrang);
            k = ps.executeUpdate();
            return k;
        } catch (Exception e) {
            throw new RuntimeException("Loi them sach.", e);
        } finally {
            con.Close();
        }
    }

    public int updateSach(int MaSach, String TenSach, String TacGia, String TheLoai, String NhaXuatBan, Double GiaSach, int SoLuong) {
        int k;
        String tinhTrang = SoLuong > 0 ? "Còn" : "Hết";
        String sql = "UPDATE SACH SET TenSach=?, TacGia=?, TheLoai=?, NhaXuatBan=?, GiaSach=?, SoLuong=?, TinhTrang=? "
                + "WHERE MaSach=?";
        try {
            con.Open();
            PreparedStatement ps = con.cnn.prepareStatement(sql);
            ps.setString(1, TenSach);
            ps.setString(2, TacGia);
            ps.setString(3, TheLoai);
            ps.setString(4, NhaXuatBan);
            ps.setDouble(5, GiaSach);
            ps.setInt(6, SoLuong);
            ps.setString(7, tinhTrang);
            ps.setInt(8, MaSach);
            k = ps.executeUpdate();
            return k;
        } catch (Exception e) {
            throw new RuntimeException("Loi cap nhat sach.", e);
        } finally {
            con.Close();
        }
    }

    public int deleteSach(int maSach) {
        int k;
        String sql = "DELETE FROM SACH WHERE MaSach = ?";
        try {
            con.Open();
            PreparedStatement ps = con.cnn.prepareStatement(sql);
            ps.setInt(1, maSach);
            k = ps.executeUpdate();
            return k;
        } catch (Exception e) {
            throw new RuntimeException("Loi xoa sach.", e);
        } finally {
            con.Close();
        }
    }
}
