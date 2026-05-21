/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Process_Data;

import ConnectionData.CONNECTIONSQLSERVER;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
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
        try {
            String sql = "SELECT MaSach, TenSach, TacGia, TheLoai,NhaXuatBan,GiaSach, SoLuong FROM sach WHERE TenSach LIKE N'%" + keyword + "%' OR TacGia LIKE N'%" + keyword + "%'";
            ResultSet rs = con.GetResultSetSQL(sql);
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
}
