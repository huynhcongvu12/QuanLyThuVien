package Process_Data;

import ConnectionData.CONNECTIONSQLSERVER;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.Account;

public class Login {
    CONNECTIONSQLSERVER con;

    public Login() {
        con = new CONNECTIONSQLSERVER();
    }

    public Account login(String user, String pass) {
        String sql = "SELECT MaAccount, TenDangNhap, MatKhau, Quyen, MaDocGia, MaNV "
                + "FROM ACCOUNT WHERE TenDangNhap = ? AND MatKhau = ?";

        try {
            con.Open();
            if (con.cnn == null) {
                throw new RuntimeException(con.databaseErrorMessage());
            }

            PreparedStatement ps = con.cnn.prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Account(
                        rs.getInt("MaAccount"),
                        rs.getString("TenDangNhap"),
                        rs.getString("MatKhau"),
                        rs.getInt("Quyen"),
                        (Integer) rs.getObject("MaDocGia"),
                        (Integer) rs.getObject("MaNV")
                );
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Loi database khi dang nhap. Kiem tra DB_URL, DB_USER, DB_PASSWORD.", e);
        } finally {
            con.Close();
        }
        return null;
    }
}
