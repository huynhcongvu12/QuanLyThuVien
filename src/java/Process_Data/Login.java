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
import model.Account;

/**
 *
 * @author Mr.Khoa
 */
public class Login {
    CONNECTIONSQLSERVER con;

    public Login() {
        con = new CONNECTIONSQLSERVER();
    }
    public Account login(String user, String pass) {
        
        try{
            String sql = "SELECT * FROM account WHERE TenDangNhap='"+user+"' AND MatKhau='"+pass+"'";
            ResultSet rs = con.GetResultSetSQL(sql);
            if (rs.next()) {
                return new Account(
                    rs.getInt("MaAccount"),
                    rs.getString("TenDangNhap"),
                    rs.getString("MatKhau"),   // ✅ BẮT BUỘC PHẢI CÓ
                    rs.getInt("Quyen"),
                    (Integer) rs.getObject("MaDocGia"),
                    (Integer) rs.getObject("MaNV")
                );
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Loi database khi dang nhap. Kiem tra DB_URL, DB_USER, DB_PASSWORD.", e);
        }
        return null;
    }
}
